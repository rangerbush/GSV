/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.EnhancedApnsNotification;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author New User
 */
public class ApnsEntity implements Runnable{
    private String deviceID;
    private String rawBody;
    private long timestamp;
    
    public ApnsEntity(String deviceID,String raw,long time)
    {
        this.deviceID = deviceID;
        this.rawBody = raw;
        this.timestamp = time;
    }
    
    public ApnsEntity(String deviceID,String raw)
    {
        this.deviceID = deviceID;
        this.rawBody = raw;
        this.timestamp = 0;
    }
    
    @Override
    public void run() {
    ApnsService service =
    APNS.newService()
    //.withCert("D:/Certificates.p12", "123456")
    .withCert("/mnt/efs/glassfish5/Certificates.p12", "123456")
           // "/WEB-INF/Certificates.p12"
    .withSandboxDestination()
    .asQueued()
   // .withNoErrorDetection()
    .build();
    service.start();
   int now =  (int)(new Date().getTime()/1000);
    String payload = APNS.newPayload()
            .alertBody(this.getRawBody())
            .alertTitle("Global Safety Village")
            .badge(1)
            .build();
    EnhancedApnsNotification e = new EnhancedApnsNotification(EnhancedApnsNotification.INCREMENT_ID(),now+3600,this.deviceID,payload);
   // service.push(this.deviceID, payload);
    service.push(e);
   // Map map = service.getInactiveDevices();
  //  System.out.println("Map: "+map.toString());
  //  System.out.println(payload);
    service.stop();
    }

    /**
     * @return the deviceID
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * @param deviceID the deviceID to set
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * @return the rawBody
     */
    public String getRawBody() {
        return rawBody;
    }

    /**
     * @param rawBody the rawBody to set
     */
    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }
    
}
