/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.exception;

import com.childcare.entity.Device;
import lombok.Getter;

/**
 *
 * @author New User
 */
public class Alert extends RuntimeException{
    @Getter private final Device device;
    @Getter private final long gid;
    
    public Alert(String msg,Device token,long gid)
    {
        super(msg);
        this.device=token;
        this.gid = gid;
    }
}
