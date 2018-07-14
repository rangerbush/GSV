/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.websocket;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.structure.Response;
import com.childcare.model.support.DeviceFetchMission;
import com.childcare.model.support.JsonWebTokenUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author New User
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {
    
    private static Map<Long,WebSocketSession> sessionMap = new ConcurrentHashMap<Long,WebSocketSession>();
    private static Map<Long,DeviceFetchMission> threadPool = new ConcurrentHashMap();
    private static Map<Long,Boolean> challengeMap = new ConcurrentHashMap();
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    
    /**
     * 供调度器调用以对所有查询结果进行一次推送
     */
    public void push()
    {
        threadPool.keySet().forEach((uid) -> {
            try {
                this.sendMessageToUser(uid, this.fetch(uid));  //推送
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(MyWebSocketHandler.class.getName()).log(Level.SEVERE, "IO exception when pushing query results to uid "+uid, ex);
            }
        }); //对于所有存在查询线程的用户
    }
    
    /**
     * 自毁回调方法。当线程执行完毕后调用此回调。将此线程自Map中移除。若Map不包含线程owner记录或记录的线程不是当前线程，则不做任何事。
     * @param uid
     * @param threadID 
     */
    public static void destruction(Long uid,long threadID)
    {
        
        if (!threadPool.containsKey(uid))
            return;
        if (threadPool.get(uid).getId()!=threadID)
            return;
        threadPool.remove(uid);
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long uid = (Long) (session.getAttributes().get("uid"));
        logger.info("Session {} connected.", uid);
        System.out.println("Session "+uid+" connected.");
        try {
            if (sessionMap.containsKey(uid))
                sessionMap.get(uid).close();
            sessionMap.put(uid, session);
            challengeMap.put(uid, Boolean.FALSE);
            System.out.println("当前在线用户数: "+ sessionMap.size());
            String msg = Base64.getEncoder().encodeToString(("UID "+uid+" connected.").getBytes());
            //this.reply(uid, "b4"+msg);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MyWebSocketHandler.class.getName()).log(Level.SEVERE, "IO exception when closing existing session for uid "+uid, ex);
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> message) throws Exception {
        Long uid = (Long) (wss.getAttributes().get("uid"));
        standardMessage msg = new Gson().fromJson(message.getPayload().toString(), standardMessage.class);
    //0 - new query 开始查询携带的DeviceID，终止之前执行的查询。deviceID不可为空
    //1 - heartbeat 心跳包，deviceID可为空
    //2 - stop service 停止UID对应的thread。deviceID可为空
        switch (msg.getCommand())
        {
            case 0:
                if (challengeMap.containsKey(uid)&challengeMap.get(uid))  
                {
                    this.startQuery(msg.getPayload(), uid);
                    this.reply(uid,"Query started.");
                }
                else
                {
                    this.reply(uid,"You need to verify identity first.");
                }
                break;
            case 1:
                if (challengeMap.containsKey(uid)&challengeMap.get(uid))  
                {
                    this.refresh(uid);
                    this.reply(uid,"Query refreshed.");
                }
                else
                {
                    this.reply(uid,"You need to verify identity first.");
                }
                break;
            case 2:
                if (challengeMap.containsKey(uid)&challengeMap.get(uid))  
                {
                    this.stopQuery(uid);
                    this.reply(uid,"Query stopped.");
                }
                else
                {
                    this.reply(uid,"You need to verify identity first.");
                }
                break;
            case 9:
                this.reply(uid,challenge(uid,msg));
                break;
            default:
                this.reply(uid, "Invalid command.");
        }
    }

    /**
     * 关闭Session，从map中移除session，终结并移除可能纯在的线程
     * @param wss
     * @param thrwbl
     * @throws Exception 
     */
    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        Long uid = (Long) (wss.getAttributes().get("uid"));
        if (wss.isOpen())
            wss.close();
        sessionMap.remove(uid); //移除session
        if (threadPool.containsKey(uid))
        {
        threadPool.get(uid).terminate(); //终止线程
        threadPool.remove(uid);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        Long uid = (Long) (wss.getAttributes().get("uid"));
        sessionMap.remove(uid); //移除session
        challengeMap.remove(uid);
        if (threadPool.containsKey(uid))
        {
        threadPool.get(uid).terminate(); //终止线程
        threadPool.remove(uid);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 创建并替换线程
     * @param did
     * @param uid 
     */
    private void startQuery(String did,Long uid)
    {
        DeviceFetchMission thread = new DeviceFetchMission(did,uid);
        if (threadPool.containsKey(uid))
            threadPool.get(uid).terminate();
        threadPool.put(uid, thread);
        thread.start();
    }
    
    private void refresh(Long uid)
    {
        if (threadPool.containsKey(uid))
            threadPool.get(uid).refreshTTL();
    }
    
    private Response fetch(Long uid)
    {
        if (threadPool.containsKey(uid))
             return threadPool.get(uid).getResponse();
        return new Response(Response.EXCEPTION_NULL_POINTER,"No query exists for UID "+uid);
    }
    
    private void stopQuery(Long uid)
    {
        if (threadPool.containsKey(uid))
        {
            threadPool.get(uid).terminate();
            threadPool.remove(uid);
        }
    }
    
    private String challenge(Long uid,standardMessage msg)
    {
        if (msg.getPayload()==null)
            return "Payload field should not be empty. Please place your access token in payload and try again.";
        String access = msg.getPayload();
        try {
            if (!JsonWebTokenUtil.checkAccess(uid, access))
            {
                challengeMap.put(uid, false);
                return "Access token validation failed.";
            }
            else
            {
                challengeMap.put(uid, true);
                return "Access token validated successfully. Welcome UID "+uid+".";
            }
        } catch (JWTVerificationException ex) {
            challengeMap.put(uid, false);
            return "JWTVerificationException:"+ex.getMessage()+". \nPlease check your token.";
        } catch (UnsupportedEncodingException ex) {
            challengeMap.put(uid, false);
            return "UnsupportedEncodingException. Contact Administrator for help.";
        }
        
    }
    
    
    /**

     * 给某个用户发送消息

     *

     * @param userName

     * @param message

     * @throws IOException

     */

    private void sendMessageToUser(Long uid, Response response) throws IOException {
        try {
                WebSocketSession session = sessionMap.get(uid);
                String str = new Gson().toJson(response);
                TextMessage tm = new TextMessage(str);
                session.sendMessage(tm);
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    
        private void reply(Long uid, String response) throws IOException {
        try {
                WebSocketSession session = sessionMap.get(uid);
                TextMessage tm = new TextMessage(response);
                //BinaryMessage tm = new BinaryMessage(response.getBytes());
                session.sendMessage(tm);
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }

    
}
