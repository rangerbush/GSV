/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.Account;
import com.childcare.entity.Contact;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.ContactWrapper;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author New User
 */
@Service("serviceContact")
public class serviceContact {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    /**
     * 
     * @param uid
     * @param token
     * @param fid
     * @param familyPassword
     * @return Null if passed authorization, corresponding response if failed
     */
    private Response authorize(long uid,String access,Contact contact)
    {
        try{
        //提供token和uid 提供fid且必须是fid的owner 提供密码
        if (!JsonWebTokenUtil.checkAccess(uid, access))
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"You do not own this token, or this is not an access token.");
        return null;
        }
         catch (JWTVerificationException|UnsupportedEncodingException|IllegalArgumentException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
            return new Response(e);
        }
    }
    
    private Response authorize(ContactWrapper wrapper)
    {
        try{
        return this.authorize(wrapper.getUid(),wrapper.getAccess(),wrapper.getContact());
        }
        catch (NullPointerException e)
        {
            return new Response(e);
        }
    }

    /**
     * 已有的记录修改需要检验记录中UID记录
     * @param uid
     * @param access
     * @param contact
     * @return 
     */
    private Response authorizeEdit(long uid,String access,Contact contact)
    {
        //override input fid with fid fetched from DB.Contact
        try{
        //提供token和uid 提供fid且必须是fid的owner 提供密码
        if (!JsonWebTokenUtil.checkAccess(uid, access))
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"You do not own this token, or this is not an access token.");
        //第一步 令牌验证通过
        Contact c = this.jdbcDataDAO.getDaoContact().getInstance(contact.getContactId()); //获取数据库中fid记录，数据库中fid需要满足权限要求
        boolean flag = c.getUid().equals(new Account(uid));
        if (!flag) //第二步 此uid下所有owner状态下家庭中可以找到指定fid
            return new Response(Response.ERROR_INTEGRITY,"Permission denied. You do not own this record.");
        return null;
        }
         catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|IllegalArgumentException e) { //令牌损坏或过期 | deviceID or Family is null | UTF-8不支持
            return new Response(e);
        }
    }
    
    private Response authorizeEdit(ContactWrapper wrapper)
    {
        try{
        return this.authorizeEdit(wrapper.getUid(),wrapper.getAccess(),wrapper.getContact());
        }
        catch (NullPointerException e)
        {
            return new Response(e);
        }
    }
    
    
    public Response create(ContactWrapper wrapper)
    {
        Response response = this.authorize(wrapper);
        if(response!=null)
            return response;
        try
        {
            Contact contact = wrapper.getContact();
            contact.setUid(new Account(wrapper.getUid()));
            wrapper.setContact(contact);
        int id = this.jdbcDataDAO.getDaoContact().create(contact);
        return new ResponsePayload(Response.GENERAL_SUCC,"Contact created.",id);
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    /**
     * Fetch contact list containing given fid
     * @param wrapper
     * @return 
     */
    
    public Response retrieve(ContactWrapper wrapper)
    {
        Response response = this.authorize(wrapper);
        if(response!=null)
            return response;
        try
        {
        List<Contact> list = this.jdbcDataDAO.getDaoContact().retrieveByUID(wrapper.getUid());
        return new ResponsePayload(Response.GENERAL_SUCC,"Contact list of account "+wrapper.getUid()+" fetched.",list);
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    public Response update(ContactWrapper wrapper)
    {
        Response response = this.authorizeEdit(wrapper);
        if(response!=null)
            return response;
        //DAO层 fid不接受修改
        try{
        this.jdbcDataDAO.getDaoContact().update(wrapper.getContact());
        return new Response(Response.GENERAL_SUCC,"Contact updated.");
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    public Response delete(ContactWrapper wrapper)
    {
        Response response = this.authorizeEdit(wrapper);
        if(response!=null)
            return response;
        try{
        this.jdbcDataDAO.getDaoContact().delete(wrapper.getContact().getContactId());
        return new Response(Response.GENERAL_SUCC,"Contact deleted.");
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
}
