/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.structure;

import com.childcare.entity.Anchor;

/**
 *
 * @author New User
 */
public class ResponsePayload extends Response {
    
    private Object payload;
    
    /**
     * 
     * @param code
     * @param msg
     * @param payload 
     */
    public ResponsePayload(int code,String msg,Object payload)
    {
        super.message = msg;
        super.status_code = code;
        this.payload = payload;
    }
    
    public ResponsePayload(Object payload)
    {
        super.message = "";
        super.status_code = 200;
        this.payload = payload;
    }

    public ResponsePayload(Exception e,Object payload)
    {
        super(e);
        this.payload = payload;
    }
    /**
     * @return the payload
     */
    public Object getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(Object payload) {
        
        this.payload = payload;
    }
    
}
