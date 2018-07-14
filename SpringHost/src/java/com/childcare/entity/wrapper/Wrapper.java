/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author New User
 */

public class Wrapper<T> {
    private long uid;
    private String access;
    private T payload;
    
    public Wrapper(long uid,String access,T t)
    {
        this.uid = uid;
        this.access = access;
        this.payload = t;
    }
    
    public Wrapper()
    {
        
    }

    /**
     * @return the uid
     */
    public long getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * @return the access
     */
    public String getAccess() {
        return access;
    }

    /**
     * @param access the access to set
     */
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * @return the payload
     */
    public T getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(T payload) {
        this.payload = payload;
    }
    
    
}
