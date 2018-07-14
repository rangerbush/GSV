/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.structure;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.Getter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 *
 * @author New User
 */
public class ExceptionExplainerBuilder {
    @Getter
    private String IncorrectResultSizeDataAccess;
    @Getter
    private String DataIntegrityViolation;
    @Getter
    private String JWTVerification;
    
    public ExceptionExplainerBuilder()
    {
        this.DataIntegrityViolation = "Oops, seems that you have submitted something violating constrainsts.";
        this.IncorrectResultSizeDataAccess ="Hmm, there seems to be an unexpected empty query result.";
        this.JWTVerification  = "Sorry, we cannot identify your identity. Please login again.";
    }
    
    
    public final ExceptionExplainerBuilder setDataIntegrityViolationException(String readable)
    {
        DataIntegrityViolation = readable;
        return this;
    }
    
    public final ExceptionExplainerBuilder setJWTVerification(String readable)
    {
        JWTVerification = readable;
        return this;
    }
    
        /**
     * Data access exception thrown when a result was expected to have at least one row (or element) but zero rows (or elements) were actually returned.
     * @param readable
     * @return 
     */
    public final ExceptionExplainerBuilder setIncorrectResultSizeDataAccessException(String readable)
    { 
        IncorrectResultSizeDataAccess = readable;
        return this;
    }
    
    
    public final ExceptionExplainer build()
    {
        return new ExceptionExplainer(this);
    }
    
    
}
