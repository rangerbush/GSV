/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

import com.childcare.entity.Anchorgroup;

/**
 *
 * @author New User
 */
public class AnchorGroupWrapper {
    private long uid;
    private String access;
    private Anchorgroup group;
    
    public AnchorGroupWrapper()
    {
        
    }
    
    public AnchorGroupWrapper(int uid,String access,Anchorgroup group)
    {
        this.access = access;
        this.uid = uid;
        this.group = group;
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
     * @return the group
     */
    public Anchorgroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Anchorgroup group) {
        this.group = group;
    }
}
