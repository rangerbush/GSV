/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.exception;

import com.childcare.entity.Device;


/**
 *
 * @author New User
 */
public class YellowAlert extends Alert{
    
    public YellowAlert(String msg,Device token,long gid)
    {
        super(msg,token,gid);
    }
}
