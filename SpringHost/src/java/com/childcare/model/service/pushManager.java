/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Device;
import com.childcare.model.support.ApnsEntity;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service("pushManager")
/**
 *
 * @author New User
 */
public class pushManager implements IpushManager {
    
    
    public void push()
    {
        System.out.println("Push not implemented.");
    }
    
    @Override
    public void push(Device device)
    {
        Gson gson = new Gson();
        String raw = gson.toJson(device);
        Thread thread = new Thread(new ApnsEntity(device.getDeviceID(),raw));
        thread.start();
    }
}
