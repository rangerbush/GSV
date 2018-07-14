/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import com.childcare.entity.structure.ExceptionExplainerBuilder;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.AnchorGroupWrapper;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author New User
 */
@Service("serviceGroup")
public class ServiceGroup {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public Response create(AnchorGroupWrapper wrapper)
    {
       /*
        需要提交家庭密码，FID
        家庭密码需符合数据库
        */
       try{
         if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
             return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         try{
         if (!this.familyAuth(wrapper.getUid(), wrapper.getGroup().getCid().getCid()))
             return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
         }
          catch (DataAccessException e)
        {
            return new Response(e,new ExceptionExplainerBuilder().setIncorrectResultSizeDataAccessException("Child you requested does not exist.").build());
        }
        int id = jdbcDataDAO.getDaoAnchorGroup().createGroup(wrapper.getGroup());
        return new ResponsePayload(Response.GENERAL_SUCC,"Group created.",id);
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|NullPointerException e)
        {
           return new Response(e);
        }  
    }
    
    public Response create(long uid,String access,Anchorgroup group)
    {
       /*
        需要提交家庭密码，FID
        家庭密码需符合数据库
        */
       try{
         if (!JsonWebTokenUtil.checkAccess(uid, access))//token验证
             return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         try{
         if (!this.familyAuth(uid, group.getCid().getCid()))
             return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
         }
          catch (DataAccessException e)
        {
            return new Response(e,new ExceptionExplainerBuilder().setIncorrectResultSizeDataAccessException("Child you requested does not exist.").build());
        }
        int id = jdbcDataDAO.getDaoAnchorGroup().createGroup(group);
        return new ResponsePayload(Response.GENERAL_SUCC,"Group created.",id);
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|NullPointerException e)
        {
           return new Response(e);
        }  
    }
    
    public Response update(long uid,String access,Anchorgroup group)
    {
        try{
        if (!JsonWebTokenUtil.checkAccess(uid,access))//token验证
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
        Anchorgroup g = jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(group.getGid());
        try{
        if (!this.familyAuth(uid, g.getCid().getCid()))
            return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
        } catch (DataAccessException e)
        {
            return new Response(e,new ExceptionExplainerBuilder().setIncorrectResultSizeDataAccessException("Child you requested does not exist.").build());
        }
        if (!Objects.equals(g.getGid(), group.getGid()) & !this.familyAuth(uid, group.getCid().getCid()) )
            return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
        jdbcDataDAO.getDaoAnchorGroup().updateGroup(group);
        return new Response(Response.GENERAL_SUCC,"Group updated.");
        } catch (DataAccessException|NullPointerException|JWTVerificationException|UnsupportedEncodingException e) {
            return new Response(e);
        }
    }
    
    public Response getInstance(AnchorGroupWrapper wrapper)
    {
       try{
         if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         Anchorgroup group = jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(wrapper.getGroup().getGid());
         if (!this.familyAuth(wrapper.getUid(), group.getCid().getCid()))
           return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
        return new ResponsePayload(Response.GENERAL_SUCC,"Group fetched.",group);
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|NullPointerException e)
        {
           return new Response(e);
        }
    }   
    
    public Response delete(AnchorGroupWrapper wrapper)
    {
       try{
         if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
             return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         Anchorgroup group = jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(wrapper.getGroup().getGid());
         if (!this.familyAuth(wrapper.getUid(), group.getCid().getCid()))
             return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
        jdbcDataDAO.getDaoAnchorGroup().deleteGroup(wrapper.getGroup().getGid());
        return new Response(Response.GENERAL_SUCC,"Group deleted.");
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|NullPointerException e)
        {
           return new Response(e);
        }
    }
    
    /**
     * 验证某个用户有权操作某个cid，即双方处于同一个family中
     * @param uid
     * @param cid
     * @return 
     */
    private boolean familyAuth(long uid,int cid)
    {
        Family family = this.jdbcDataDAO.getDaoFamily().findFamilyByCID(cid);
        //cid不存在？
        List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
        return list.stream().map(f -> f.getFid()).anyMatch(id -> Objects.equals(id, family.getFid()));
    }
    
}
