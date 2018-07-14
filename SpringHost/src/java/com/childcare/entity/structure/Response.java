/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.structure;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.model.exception.NullException;
import com.childcare.model.exception.RedAlert;
import com.childcare.model.exception.YellowAlert;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.init.CannotReadScriptException;
import org.springframework.jdbc.datasource.init.ScriptParseException;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;

/**
 *
 * @author New User
 */
public class Response {
    protected String message;
    protected int status_code;
    protected String readable;
    public static final int GENERAL_SUCC = 200;
    public static final int ERROR_ACCOUNT_PASSWORD = 300; //Errors occured when authoration of account failed, including login, changing password etc.

    public static final int ERROR_TOKEN_VERIFY_FAILURE = 301;    //token type is not correct, or token is blacklisted.
    public static final int ERROR_TOKEN_ACCESS_UID = 302;    //AccessToken's authoration failed. UID in token does not match the request one.
    public static final int ERROR_TOKEN_REFRESH_PSW = 303;  //Password in refresh token is missing or does not match the record.
    public static final int ERROR_INTEGRITY = 304;
    public static final int ERROR_FAMILY_PERMISSION = 310; //uid of requester does not own requested family, is not allowed to do requested operation on this family.
    public static final int ERROR_FAMILY_PASSWORD = 311; //requester did not provided correct family password of this family.
    public static final int ERROR_FAMILY_STATUS = 312;  //family status is out of boundary
    public static final int ERROR_NULL_FIELD = 350; //one or more fields in input are null, which is not acceptable.
    public static final int ERROR_INVALID_FILE_TYPE = 403;
    public static final int EXCEPTION_OTHER = 600;
    public static final int EXCEPTION_DATA_ACCESS = 601;
    public static final int EXCEPTION_ILLEGAL_AGRUEMENT = 602;
    public static final int EXCEPTION_RUNTIME = 603;
    public static final int EXCEPTION_RUNTIME_JWTVerificationException = 604;
    public static final int EXCEPTION_MessagingException = 605;
    public static final int EXCEPTION_CLASSCAST = 606;
    public static final int EXCEPTION_NUMBER_FORMAT = 607;
    public static final int EXCEPTION_UNSUPPORTED_ENCODING =608;
    public static final int EXCEPTION_NULL = 609;  //there is field in reqeust is null, which is not acceptable.
    public static final int EXCEPTION_NULL_POINTER = 610;  //there is field in reqeust is null, which is not acceptable.  
    public static final int EXCEPTION_IO = 611;
    public static final int ALERT_YELLOW = 700; //device's latest location has broke safety constraints/Device is in danger zone
    public static final int ALERT_RED = 701;
    public static final int ALERT_MISSING = 710;
    /**
     * default constructor to report success
     */
    public Response()
    {
        this.message = "SUCC";
        this.status_code = GENERAL_SUCC;
        this.message = "";
    }
    
    public Response(String msg,int code)
    {
        this.status_code = code;
        if (!msg.equalsIgnoreCase("pf"))
        this.message = msg;
        else
            this.message = "Password validation failed. Transaction abort.";
    }
    
    public Response (Exception e,ExceptionExplainer ee)
    {
        init(e,ee);
    }
    
    
    public Response(Exception e)
    {
        init(e,new ExceptionExplainerBuilder().build() );
    }
    
    private void init(Exception e,ExceptionExplainer ee)
    {
                this.message = e.getMessage();
        if (e instanceof DataAccessException)
        {
            this.status_code = EXCEPTION_DATA_ACCESS;
            this.readable = ee.explain((DataAccessException)e);
        }

        else if (e instanceof JWTVerificationException)
        {
            this.status_code = EXCEPTION_RUNTIME_JWTVerificationException;
            this.readable = ee.explain((JWTVerificationException)e);
        }
        else if (e instanceof IllegalArgumentException)
            this.status_code = EXCEPTION_ILLEGAL_AGRUEMENT;
        else if (e.getClass() == IOException.class)
            this.status_code = EXCEPTION_IO;
        else if (e.getClass() == MessagingException.class)
            this.status_code = EXCEPTION_MessagingException;
        else if (e.getClass() == ClassCastException.class)
            this.status_code = EXCEPTION_CLASSCAST;
        else if (e.getClass() == NumberFormatException.class)
            this.status_code = EXCEPTION_NUMBER_FORMAT;
        else if (e.getClass() == UnsupportedEncodingException.class)
            this.status_code=EXCEPTION_UNSUPPORTED_ENCODING;
        else if (e.getClass() == NullException.class)
            this.status_code = EXCEPTION_NULL;
        else if (e.getClass() == NullPointerException.class)
            this.status_code = EXCEPTION_NULL_POINTER;
        else if (e.getClass() == YellowAlert.class)
            this.status_code = ALERT_YELLOW;
        else if (e.getClass() == RedAlert.class)
            this.status_code = ALERT_RED;
        else if (e.getClass()==RuntimeException.class)
            this.status_code = EXCEPTION_RUNTIME;
        else this.status_code = EXCEPTION_OTHER;
    }
    
    public Response(int code,String msg)
    {
        this.message = msg;
        this.status_code = code;
    }
    


 
    /**
     * @return the status_code
     */
    public int getStatus_code() {
        return status_code;
    }

    /**
     * @param status_code the status_code to set
     */
    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
