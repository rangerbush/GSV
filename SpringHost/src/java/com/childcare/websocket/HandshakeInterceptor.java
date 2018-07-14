/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.websocket;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 *
 * @author New User
 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{
        private static final Logger logger = LoggerFactory.getLogger(HandshakeInterceptor.class);
      // 握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        System.out.println("A new connection request is received.");
        logger.debug("A new connection request is received.");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            // 标记用户
            String param = servletRequest.getServletRequest().getParameter("uid");
            Long uid = Long.parseLong(param);
            //Long uid = (Long) session.getAttribute("uid");
           // System.out.println("UID ="+uid);
            /*String access = (String)session.getAttribute("access");
            if (!JsonWebTokenUtil.checkAccess(uid, access))
            {
                logger.error("UID "+uid+" does not hold valid access token thus connection is denied.");
                return false;
            }
            logger.debug("token check successful");
*/
            if (uid != null) {
                attributes.put("uid", uid);
            } else {
                //System.out.println("before handshake denied");
                return false;
            }
        }
       // System.out.println("before handshake checkpoint");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }



    // 握手后
    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception ex) {
        logger.debug("after handshake checkpoint");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
