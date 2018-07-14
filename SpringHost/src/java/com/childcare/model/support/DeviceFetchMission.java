/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import com.childcare.entity.structure.Response;
import com.childcare.model.service.serviceDevice;
import com.childcare.websocket.MyWebSocketHandler;
import java.util.Calendar;
import static java.util.Calendar.MINUTE;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import lombok.Getter;

/**
 * 创建一个线程，不定期抓取给定deviceToken对应信息，更新在自身response域。将terminateFlag设置为true来终止运行。使用refreshTTL方法刷新TTL，线程将在5分钟未刷新后死亡
 * @author New User
 */
    public class DeviceFetchMission extends Thread {

    private final serviceDevice service;
    @Getter
    private Long owner;
    private boolean terminateFlag;
    private Response response;
    private final String did;
    private Calendar startSince;
    private boolean greenlight;
    
    public DeviceFetchMission(String did,Long uid)
    {
        this.terminateFlag = false;
        this.response = new Response(Response.EXCEPTION_NULL,"No data has been fetched yet.");
        this.did = did;
        this.startSince = Calendar.getInstance();
        this.owner = uid;
        this.service = (serviceDevice)SpringContextUtil.getBean("serviceDevice");
        this.greenlight = false;
    }
    
    @Override
    public void run() {
        Calendar now;
        this.greenlight = this.service.grantGreenLight(did, owner);
        if (!greenlight)
        {
            MyWebSocketHandler.destruction(owner, this.getId());
            return;
        }
        while (!terminateFlag)
        {
            //System.out.println("Thread for "+did+" by UID "+owner+" starts."+ " service is null? "+this.service==null);
            now = Calendar.getInstance();//当前时间
            now.add(MINUTE, -5);//当前时间回滚5分钟
            if (now.after(this.startSince))  //若5分钟前仍然在开始时间之后，则意味着TTL超时，线程应当终止
                return;
            setResponse(this.service.fetch(getDid()));
            //System.out.println("Thread for "+did+" by UID "+owner+" prepares to yield.");
            DeviceFetchMission.currentThread().yield(); //释放控制，随机竞争执行下一个循环
        }
        MyWebSocketHandler.destruction(owner, this.getId()); //线程结束前回调
        //System.out.println("Thread for device "+did+" is now terminated.");
    }

    /**
     * 查看线程是否将终止。当值为true时线程循环将在下一次循环头终止。
     * @return the terminateFlag
     */
    public boolean isTerminateFlag() {
        return terminateFlag;
    }

    /**
     * 终止线程。线程将在下一次循环时结束。
     * 
     */
    public void terminate() {
        this.terminateFlag = true;
    }


    /**
     * 抓取最新查询结果
     * @return the response
     */
    public Response getResponse() {
        return response;
    }


    /**
     * @return the did
     */
    public String getDid() {
        return did;
    }

    /**
     * @param response the response to set
     */
    private void setResponse(Response response) {
        this.response = response;
    }
    
    /**
     * 用来刷新TTL，TTL默认5分钟
     */
    public void refreshTTL()
    {
        this.startSince = Calendar.getInstance();
    }

}
