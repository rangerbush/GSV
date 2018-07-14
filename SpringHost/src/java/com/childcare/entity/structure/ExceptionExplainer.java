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
public class ExceptionExplainer {
    @Getter
    private final String IncorrectResultSizeDataAccess;
    @Getter
    private final String DataIntegrityViolation;
    @Getter
    private final String JWTVerification;
    
    private final String defaultAnswer;
    

    
    public ExceptionExplainer(ExceptionExplainerBuilder builder)
    {
        this.IncorrectResultSizeDataAccess = builder.getIncorrectResultSizeDataAccess();
        this.DataIntegrityViolation = builder.getDataIntegrityViolation();
        this.JWTVerification = builder.getJWTVerification();
        this.defaultAnswer = "There seems to be some errors unknown whne processing your request. Please try again later or contact support for help.";
    }
    
    public final String explain(DataAccessException e)
    {
        return work(e);
    }
    
    public final String explain(JWTVerificationException e)
    {
        return work(e);
    }
    
    private String work(Exception e)
    {
        if (e instanceof IncorrectResultSizeDataAccessException)
            return this.IncorrectResultSizeDataAccess;
        if (e instanceof DataIntegrityViolationException)
            return this.DataIntegrityViolation;
        if (e instanceof JWTVerificationException)
            return this.JWTVerification;  
        return this.defaultAnswer;
    }
    
}
