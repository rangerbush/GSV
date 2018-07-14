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
public class MailandPsw {
    private String email;
    private String password;
    private String device;
    
    public MailandPsw(String email,String psw,String device)
    {
        this.password = psw;
        this.email = email;
        this.device = device;
    }
    
    public MailandPsw()
    {
        
    }

 

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device) {
        this.device = device;
    }
    
    
}
