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
public class UpdateWrapper {
    private Account account;
    private String accessToken;
    
    public UpdateWrapper()
    {
        
    }
    
        public UpdateWrapper(Account account, String token)
    {
        this.account = account;
        this.accessToken = token;
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
     * @return the accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    
}
