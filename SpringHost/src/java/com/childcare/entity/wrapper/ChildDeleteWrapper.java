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
public class ChildDeleteWrapper {
    private int cid;
    private int uid;
    private String token;
    private String family_password;
    public ChildDeleteWrapper()
    {

    }
    
    public ChildDeleteWrapper(int cid,int uid,String token,String password)
    {
        this.cid = cid;
        this.uid = uid;
        this.token = token;
        this.family_password = password;
    }

    /**
     * @return the cid
     */
    public int getCid() {
        return cid;
    }

    /**
     * @param cid the cid to set
     */
    public void setCid(int cid) {
        this.cid = cid;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the family_password
     */
    public String getFamily_password() {
        return family_password;
    }

    /**
     * @param family_password the family_password to set
     */
    public void setFamily_password(String family_password) {
        this.family_password = family_password;
    }
}
