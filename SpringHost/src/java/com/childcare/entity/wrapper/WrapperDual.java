/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

/**
 *
 * @author New User
 */
public class WrapperDual<T,P> {
    private long uid;
    private String access;
    private T payload1;
    private P payload2;
    
    public WrapperDual(long uid,String access,T t,P p)
    {
        this.uid = uid;
        this.access = access;
        this.payload1 = t;
        this.payload2 = p;
    }
    
    public WrapperDual()
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
     * @return the payload1
     */
    public T getPayload1() {
        return payload1;
    }

    /**
     * @param payload1 the payload1 to set
     */
    public void setPayload1(T payload1) {
        this.payload1 = payload1;
    }

    /**
     * @return the payload2
     */
    public P getPayload2() {
        return payload2;
    }

    /**
     * @param payload2 the payload2 to set
     */
    public void setPayload2(P payload2) {
        this.payload2 = payload2;
    }
}
