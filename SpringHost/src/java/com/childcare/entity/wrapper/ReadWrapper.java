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
public class ReadWrapper {
    private long uid;
    private String access;
    
    public ReadWrapper()
    {
        
    }
    public ReadWrapper(long UID, String access)
    {
        this.access = access;
        this.uid=UID;
    }

    /**
     * @return the UID
     */
    public long getUID() {
        return uid;
    }

    /**
     * @param UID the UID to set
     */
    public void setUID(long UID) {
        this.uid = UID;
    }

    /**
     * @return the Access
     */
    public String getAccess() {
        return access;
    }

    /**
     * @param Access the Access to set
     */
    public void setAccess(String Access) {
        this.access = Access;
    }
}
