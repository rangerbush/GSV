/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

/**
 *
 * @author New User
 */
public class PasswdChanger {

    /**
     * @return the UID
     */
    public long getUID() {
        return UID;
    }

    /**
     * @param UID the UID to set
     */
    public void setUID(long UID) {
        this.UID = UID;
    }

    /**
     * @return the orgPasswd
     */
    public String getOrgPasswd() {
        return orgPasswd;
    }

    /**
     * @param orgPasswd the orgPasswd to set
     */
    public void setOrgPasswd(String orgPasswd) {
        this.orgPasswd = orgPasswd;
    }

    /**
     * @return the newPasswd
     */
    public String getNewPasswd() {
        return newPasswd;
    }

    /**
     * @param newPasswd the newPasswd to set
     */
    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }
    private long UID;
    private String orgPasswd;
    private String newPasswd;
    public PasswdChanger()
    {
       
    }
    
    public PasswdChanger(long uid,String oP,String nP)
    {
        this.UID = uid;
        this.newPasswd = nP;
        this.orgPasswd = oP;
    }
    
    
}
