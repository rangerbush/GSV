/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.AccountFamilyPK;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.AccountController;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("serviceFamily")
/**
 *
 * @author New User
 */
public class serviceFamily {
    
        @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public Object addRelationship(int fid,int uid,int status, String access)
    {
        try{
            if (JsonWebTokenUtil.checkAccess(uid, access))
            {
        jdbcDataDAO.getDaoFamily().addRelationship(fid, uid, status);   
        return new Response(Response.GENERAL_SUCC,"relationship created.");
            }
            else
                return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e)
        {
            return new Response(e);
        }   
    }
    
    /**
     * Must be family owner to edit.
     * @param fid
     * @param uid
     * @param status
     * @param token
     * @param host
     * @return 
     */
    public Object editRelationship(int fid,int uid,int status, String token,int host)
    {
        try{
            if (!JsonWebTokenUtil.checkAccess(host, token))
                    return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
             List<Family> list = this.jdbcDataDAO.getDaoFamily().findOwnedFamilies(host);
             if(!(list.stream().anyMatch((e) -> (e.getFid()==fid))))
                return new Response(Response.ERROR_FAMILY_PERMISSION,"You do not have permission to modify this record.");
             //注意 这里没检验被添加人是否同意
             this.jdbcDataDAO.getDaoFamily().updateRelationship(fid, uid, status);
             return new Response(Response.GENERAL_SUCC,"Relationship updated.");
        }   catch (UnsupportedEncodingException | DataAccessException | JWTVerificationException ex) {
               return new Response(ex);
            }
        
    }
    
    /**
     * create a family,and log the relationship between fid and uid
     * @param uid
     * @param family
     * @param token
     * @return 
     */
    @Transactional
    public Object createFamily(int uid,Family family, String token)
    {
        try{
            if (JsonWebTokenUtil.checkAccess(uid, token))
            {
            int fid = this.jdbcDataDAO.getDaoFamily().createFamily(family); //创建family并获取返回的fid
            this.jdbcDataDAO.getDaoFamily().addRelationship(fid, uid, AccountController.STATUS_OWNER); //添加fid和uid的关联并设置为owner
            return new ResponsePayload(Response.GENERAL_SUCC,"Family created.",fid);
            } else
                return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
        } catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|IllegalArgumentException e)
        {
            return new Response(e);
        }
    }
    
    public Object getMyFamily(int uid,String token)
    {
        try
        {
            //int fid = this.jdbcDataDAO.getDaoFamily().findMyFamily(uid);
               if (JsonWebTokenUtil.checkAccess(uid, token))
            {
            List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
            list.forEach((family) -> {
                family.setFamilyPassword("******");
                   });   
            return new ResponsePayload(Response.GENERAL_SUCC,"Family record found.",list);
            } else
                return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e)
        {
            return new Response(e);
        }
    }
    
    /**
     * 
     * @param uid
     * @param access
     * @param fid
     * @return ERROR_TOKEN_ACCESS_UID,ERROR_FAMILY_PERMISSION,GENERAL_SUCC
     * @throws UnsupportedEncodingException 
     */
    public int familyOperationAuthorization(int uid,String access,int fid) throws UnsupportedEncodingException,DataAccessException,JWTVerificationException
    {
                //1 验证uid和token符合，且token有效
                if (!JsonWebTokenUtil.checkAccess(uid, access))
                    return Response.ERROR_TOKEN_ACCESS_UID;
                //2 验证uid下属family中包含给定fid
                List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
                if(list.stream().anyMatch((e) -> (e.getFid()==fid)))
                    return Response.GENERAL_SUCC;
                return Response.ERROR_FAMILY_PERMISSION;
        
    }
    

}
