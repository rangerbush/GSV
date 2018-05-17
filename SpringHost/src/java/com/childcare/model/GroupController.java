/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.AnchorGroupWrapper;
import com.childcare.model.service.serviceFamily;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author New User
 */
@RestController
@RequestMapping(value = "/group")
public class GroupController {
        @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
        @Resource
    private serviceFamily serviceFamily;
        
        public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
        
    /**
     * 更新一个group记录。fid域不会被修改，需在gid中fid域包含家庭密码
     * @param wrapper
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody AnchorGroupWrapper wrapper)
    {
        try{
            //输入UID AccessToken验证
            //输入目标fid,password
            //不接受FID变动
            //UID必须为原有家庭(DB抓取FID域)成员
            //密码需匹配提交的FID对应密码
        //wrapper空值
        if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         Family familyInstance;
         familyInstance = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(wrapper.getGroup().getGid().intValue());//检索gid对应family信息
         String hashed = familyInstance.getFamilyPassword(); //find stored hashed password with given fid
        if (!BCrypt.checkpw(wrapper.getGroup().getFid().getFamilyPassword(), hashed))  //if input password match stored password of given fid
            return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyPassword");//输入family密码不正确
        jdbcDataDAO.getDaoAnchorGroup().updateGroup(wrapper.getGroup());
            return new Response(Response.GENERAL_SUCC,"Group updated.");
        }
              catch (DataAccessException|NullPointerException|JWTVerificationException|UnsupportedEncodingException e)
               {
                 return new Response(e);
               }
    } 
    
    /**
     * 提供fPassword，任意fid，指定gid
     * @param wrapper
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/fetch", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@RequestBody AnchorGroupWrapper wrapper) {
        try{
        if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
            return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
        Family familyInstance = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(wrapper.getGroup().getGid().intValue());//检索gid对应family信息
        String hashed = familyInstance.getFamilyPassword(); //find stored hashed password with given fid
        if (!BCrypt.checkpw(wrapper.getGroup().getFid().getFamilyPassword(), hashed))  //if input password match stored password of given fid
            return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyPassword");//输入family密码不正确  
        Anchorgroup group = (Anchorgroup)jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(wrapper.getGroup().getGid());
                return group;
        }
         catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e)
         {
           return new Response(e);
         }

    }
    
        @ResponseBody
    @RequestMapping(value = "/register/help", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object createHelp()
    {
        AnchorGroupWrapper w = new AnchorGroupWrapper();
        w.setGroup(new Anchorgroup(Long.getLong("123"),"groupname"));
        return w;
    }
    
     /**
     * 
     * @param wrapper
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object create(@RequestBody AnchorGroupWrapper wrapper)
    {
    /*
    private int uid;
    private String access;
    private Anchorgroup group;
        */
       /*
        需要提交家庭密码，FID
        家庭密码需符合数据库
        */
       try{
         if (wrapper.getGroup()==null|wrapper.getGroup().getFid()==null|wrapper.getGroup().getFid().getFamilyPassword()==null)
             return new Response(Response.ERROR_NULL_FIELD,"family password or related field is null.");
        //wrapper中空值问题
        //this.serviceFamily.familyOperationAuthorization(wrapper.getUid(), wrapper.getAccess(), wrapper.getGroup().getFid().getFid());
         if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))//token验证
             return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
         Family familyInstance = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(wrapper.getGroup().getFid().getFid());
         String hashed = familyInstance.getFamilyPassword(); //find stored hashed password with given fid  家庭密码
         if (BCrypt.checkpw(wrapper.getGroup().getFid().getFamilyPassword(), hashed)) //if input password match stored password of given fid输入家庭密码符合输入FID对应密码
            //    &&  Objects.equals(this.jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(wrapper.getGroup().getGid()).getFid().getFid(), familyInstance.getFid()))  //and if stored fid of input gid matches input fid
                 //使用给定GID从数据库抓取的记录中fid数值 是否和 申请的中提供的fid相等
         {
             long id = jdbcDataDAO.getDaoAnchorGroup().createGroup(wrapper.getGroup());
            return new ResponsePayload(Response.GENERAL_SUCC,"Group created.",id);
         }
        }
        catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e)
        {
           return new Response(e);
        }
         return new Response(Response.ERROR_FAMILY_PASSWORD,"Invalid FamilyID or FamilyPassword");
       
    } 
    
        
        
    
}
