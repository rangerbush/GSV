/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity.structure;

/**
 *
 * @author New User
 */
public class Response {
    private Object response;
    
    public Response()
    {
        this.response = "SUCC";
    }
    
    public Response(String msg)
    {
        this.response = msg;
    }
    
    public Response(Exception e)
    {
        this.response = e.getMessage();
    }
    
    public Response(Object o)
    {
        this.response = o;
    }

    /**
     * @return the response
     */
    public Object getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }

}
