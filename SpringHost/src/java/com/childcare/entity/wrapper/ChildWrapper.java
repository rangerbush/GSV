/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

import com.childcare.entity.Child;

/**
 *
 * @author New User
 */
public class ChildWrapper {
    private Child child;
    private int uid;
    private String token;
    public ChildWrapper()
    {

    }
    
    public ChildWrapper(Child child,int uid,String token)
    {
        this.child = child;
        this.uid = uid;
        this.token = token;
    }

    /**
     * @return the child
     */
    public Child getChild() {
        return child;
    }

    /**
     * @param child the child to set
     */
    public void setChild(Child child) {
        this.child = child;
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
