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
    
    public MailandPsw(String email,String psw)
    {
        this.password = psw;
        this.email = email;
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
    
    
}
