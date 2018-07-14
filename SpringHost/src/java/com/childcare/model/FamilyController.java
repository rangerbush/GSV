/*
 * In family DAO, password from client will be hashed again to be operated in Database, no matter if that password is raw or has been hashed once by client.
 * 
 * 
 */
package com.childcare.model;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.service.serviceFamily;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author New User
 */
@RestController
@RequestMapping(value = "/family")
public class FamilyController {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    @Resource
    private serviceFamily service;
        
        public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
        
    @ResponseBody
    @RequestMapping(value = "/changepw/{passwd}", method = POST,produces = { APPLICATION_JSON_VALUE }) 
    public Object changePassword(@RequestBody Family family,@PathVariable(value = "passwd")String passwd)
    {
        try {
            this.jdbcDataDAO.getDaoFamily().changePassword(family, passwd);
            return new Response();
        } catch (Exception ex) {
            return new Response(ex);
        }
    }

    /**
     * 
     * @param family
     * @return SUCC if successful, or exception info if failed
     */        
    @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody Family family)
    {
       try{
        jdbcDataDAO.getDaoFamily().updateFamily(family);
        return new Response();
        }
        catch (Exception e)
        {
             //return "{\"response\":\""+e+"\"}";
            return new Response(e);
        }
        //return "{\"response\":\"SUCC\"}";
        
    } 
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{fid}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "fid")int fid) {
        
        try{
        Family family = (Family)jdbcDataDAO.getDaoFamily().getFamilyInstance(fid);
        family.setFamilyPassword(null);
                return family;
        }
         catch (DataAccessException e)
         {
           return new Response(e);
         }

    }

    
    @ResponseBody
    @RequestMapping(value = "/password", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object tryPassword(@RequestBody Family family)
    {
       // Gson gson = new Gson();
        try{
        Family f = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(family.getFid());
        boolean flag = BCrypt.checkpw(family.getFamilyPassword(), f.getFamilyPassword());
        return new ResponsePayload(Response.GENERAL_SUCC,"Similarity of '"+family.getFamilyPassword()+"' comparing to record in DB:",flag);
        }
        catch (Exception e)
        {
            return new Response(e);
        }
    } 
    
    @ResponseBody
    @RequestMapping(value = "/delete/{fid}/{passwd}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@PathVariable(value = "fid")int fid,@PathVariable(value = "passwd")String passwd)
    {
        try{
        if (this.jdbcDataDAO.getUtility().Validator(passwd, fid))
        {
            this.jdbcDataDAO.getDaoFamily().deleteFamily(fid);
            return new Response();
        }
        else
            return new Response(700,"Invalid FamilyID or FamilyPassword");
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/get_member", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object getMember(@RequestParam(value = "fid")int fid,@RequestParam(value = "token")String token,@RequestParam(value = "uid")long uid)
    {
        return this.service.getFamilyMembers(fid, token, uid);
    }
    
        @ResponseBody
    @RequestMapping(value = "/get_member_detail", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object getMemberDetailed(@RequestParam(value = "fid")int fid,@RequestParam(value = "token")String token,@RequestParam(value = "uid")long uid)
    {
        return this.service.getFamilyMembersDetailed(fid, token, uid);
    }


}
