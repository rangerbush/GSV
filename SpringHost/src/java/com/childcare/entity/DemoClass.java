/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DemoClass implements java.io.Serializable{
    private static final long serialVersionUID = -6254553465205204221L;
    @XmlElement
    private String code;
    @XmlElement
    private String message;
    public DemoClass()
    {
        
    }
    /**
     * 
     * @param code
     * @param msg 
     */
    public DemoClass(String code,String msg)
    {
        this.code = code;
        this.message = msg;
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
