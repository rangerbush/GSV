/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.childcare.entity.Account;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.AccountFamilyPK;
import com.childcare.entity.Child;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import static com.childcare.entity.structure.Response.GENERAL_SUCC;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.support.AccountFamilyTrim;
import com.childcare.model.AccountController;
import static com.childcare.model.DAO.DAOChild.URL;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    
    public Object addRelationship(int fid,long uid,int status, String access, int ownership)
    {
        try{
            if (JsonWebTokenUtil.checkAccess(uid, access))
            {
        jdbcDataDAO.getDaoFamily().addRelationship(fid, uid, status,ownership);   
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
    public Object editRelationship(int fid,long uid,int status, String token,long host)
    {
        try{
            if (!JsonWebTokenUtil.checkAccess(host, token))
                    return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
             List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(host);
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
     * @param relation
     * @return 
     */
    @Transactional
    public Object createFamily(long uid,Family family, String token,int relation)
    {
        try{
            if (JsonWebTokenUtil.checkAccess(uid, token))
            {
            int fid = this.jdbcDataDAO.getDaoFamily().createFamily(family,uid); //创建family并获取返回的fid
            this.jdbcDataDAO.getDaoFamily().addRelationship(fid, uid, relation,1); //添加fid和uid的关联并设置为owner
            return new ResponsePayload(Response.GENERAL_SUCC,"Family created.",fid);
            } else
                return new Response(Response.ERROR_TOKEN_ACCESS_UID ,"This is not an Access token or this token does not belong to given user.");
        } catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException|IllegalArgumentException e)
        {
            return new Response(e);
        }
    }
    
    public Object getMyFamily(long uid,String token)
    {
        try
        {
            //int fid = this.jdbcDataDAO.getDaoFamily().findMyFamily(uid);
               if (JsonWebTokenUtil.checkAccess(uid, token))
            {
            List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
            List<Integer> fidList = list.stream()
                    .map(f -> f.getFid())
                    .collect(Collectors.toList());
            List<Child> childList = this.jdbcDataDAO.getDaoChild().findByMultiFID(fidList);
            List<AccountFamily> afList = this.jdbcDataDAO.getDaoFamily().findByMultiFID(fidList);
            List<HashMap<String,Object>> result = list.stream().map((Family f) -> {
                /*对于每一个fid*/
                HashMap<String,Object> map = new HashMap();
                map.put("fid", f.getFid().longValue());
                //取childList中和指定fid一致的记录的数量
                map.put("familyName",f.getFamilyName());
                map.put("child", childList.stream().filter(c -> Objects.equals(c.getFid().getFid(), f.getFid())).count());
                map.put("account", afList.stream().filter(af -> af.getAccountFamilyPK().getFid()==f.getFid()).count());
                map.put("creator",f.getCreator().getUid() );
                return map;
            }).collect(Collectors.toList());
            return new ResponsePayload(Response.GENERAL_SUCC,"Family record found.",result);
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
    public int familyOperationAuthorization(long uid,String access,int fid) throws UnsupportedEncodingException,DataAccessException,JWTVerificationException
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
    
    public Object getFamilyMembers(int fid,String access,long uid)
    {

            try {
                if (!JsonWebTokenUtil.checkAccess(uid, access))
                    return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an Access token or this token does not belong to given user.");
                //2 验证uid下属family中包含给定fid
                List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
                if(!(list.stream().anyMatch((e) -> (e.getFid()==fid))))
                    return new Response(Response.ERROR_FAMILY_PERMISSION,"You do not have permission to modify this record.");
                List<AccountFamily> memberList = this.jdbcDataDAO.getDaoFamily().getMembers(fid);
                List<AccountFamilyTrim> pkList = new ArrayList();
                memberList.forEach(e -> pkList.add(new AccountFamilyTrim(e.getAccountFamilyPK(),e.getStatus())));
                return new ResponsePayload(Response.GENERAL_SUCC,"Family member list fetched.",pkList);
            } catch (UnsupportedEncodingException | DataAccessException | JWTVerificationException ex) {
                return new Response(ex);
            }
    }
    
        public Object getFamilyMembersDetailed(int fid,String access,long uid)
    {

            try {
                if (!JsonWebTokenUtil.checkAccess(uid, access))
                    return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an Access token or this token does not belong to given user.");
                //2 验证uid下属family中包含给定fid
                List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
                if(!(list.stream().anyMatch((e) -> (e.getFid()==fid))))
                    return new Response(Response.ERROR_FAMILY_PERMISSION,"You do not have permission to modify this record.");
                List<Account> memberList = this.jdbcDataDAO.getDaoAccount().searchByFID(fid);
                List<Child> childList = this.jdbcDataDAO.getDaoChild().findByFID(fid,URL);
                List<Device> deviceList = this.jdbcDataDAO.getDaoDevice().findByFID(fid);
                List<AccountFamily> afList = this.jdbcDataDAO.getDaoFamily().getMembers(fid);
                List<Child> returnList = childList.stream().filter(c->c.getDeviceID()!=null).map(c->
            {
               Device device = deviceList.stream().filter(d->d.getDeviceID().equals(c.getDeviceID().getDeviceID())).findAny().get();
               c.setDeviceID(device);
               return c;
            }).collect(Collectors.toList());  //child中注入device信息
                Map<String,Object> map = new HashMap();
                map.put("children",returnList);
                map.put("members", memberList);
                map.put("relationship", afList);
                return new ResponsePayload(GENERAL_SUCC,"Child & Member list of UID "+uid+" fetched.",map);
            } catch (UnsupportedEncodingException | DataAccessException | JWTVerificationException ex) {
                return new Response(ex);
            }
    }
    

}
