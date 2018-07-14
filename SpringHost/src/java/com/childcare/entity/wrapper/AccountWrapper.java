/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.wrapper;

import com.childcare.entity.Account;

/**
 *
 * @author New User
 */
public class AccountWrapper {
    private Account account;
    private String token;
    
    public AccountWrapper()
    {
        
    }
    
    public AccountWrapper(String token,Account account)
    {
        this.account = account;
        this.token = token;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
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
