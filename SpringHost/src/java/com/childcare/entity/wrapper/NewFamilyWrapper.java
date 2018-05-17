/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

import com.childcare.entity.Family;

/**
 *
 * @author New User
 */
public class NewFamilyWrapper {
    private Family family;
    private int uid;      
    private String token;
    public NewFamilyWrapper()
    {
        
    }
    public NewFamilyWrapper(Family f,int uid,String token)
    {
        this.family = f;
        this.uid = uid;
        this.token = token;
    }

    /**
     * @return the family
     */
    public Family getFamily() {
        return family;
    }

    /**
     * @param family the family to set
     */
    public void setFamily(Family family) {
        this.family = family;
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
}
