/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.support;

import com.childcare.entity.AccountFamilyPK;

/**
 *
 * @author New User
 */
public class AccountFamilyTrim {
    private Integer status;
    private long uid;
    private int fid;

    public AccountFamilyTrim(AccountFamilyPK pk,Integer status)
    {
        this.fid = pk.getFid();
        this.uid = pk.getUid();
        this.status = status;
    }
    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return the fid
     */
    public int getFid() {
        return fid;
    }

    /**
     * @param fid the fid to set
     */
    public void setFid(int fid) {
        this.fid = fid;
    }

 
    
}
