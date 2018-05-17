/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

/**
 * Use this class to change password
 * @author New User
 */
public class PasswdChanger {



    /**
     * @return the olgPasswd
     */
    public String getOldPasswd() {
        return oldPasswd;
    }

    /**
     * @param olgPasswd the olgPasswd to set
     */
    public void setOldPasswd(String olgPasswd) {
        this.oldPasswd = olgPasswd;
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
    private long uid;
    private String oldPasswd;
    private String newPasswd;
    private String auth;
    public PasswdChanger()
    {
       
    }
    
    /**
     * 
     * @param uid Account UID
     * @param oP old password. Only necessary while auth is used to hold AccessToken
     * @param nP new password
     * @param auth PIN or AccessToken
     */
    public PasswdChanger(long uid,String oP,String nP,String auth)
    {
        this.uid = uid;
        this.newPasswd = nP;
        this.oldPasswd = oP;
        this.auth = auth;
    }

    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(String auth) {
        this.auth = auth;
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

 
    
    
}
