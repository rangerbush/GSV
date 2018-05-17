/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

/**
 *
 * @author New User
 */
public class ApnsEntity implements Runnable{
    private String deviceID;
    private String rawBody;
    
    public ApnsEntity(String deviceID,String raw)
    {
        this.deviceID = deviceID;
        this.rawBody = raw;
    }
    
    @Override
    public void run() {
    System.out.println("Push: "+this.deviceID+" with payload -> "+this.rawBody);
    ApnsService service =
    APNS.newService()
    .withCert("/path/to/certificate.p12", "MyCertPassword")
    .withSandboxDestination()
    .asQueued()
    .withNoErrorDetection()
    .build();
    String payload = APNS.newPayload()
            .alertBody(this.getRawBody())
            .alertTitle("Monitored watches have gone dark for too long!")
            .badge(1)
            .customField("Missing",this.rawBody)
            .build();
    service.push(this.deviceID, payload);
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
