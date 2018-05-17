/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;

/**
 *
 * @author New User
 */
public interface IGroupHandler {
    
    public boolean handle(Device device,Anchorgroup group);
    
}
