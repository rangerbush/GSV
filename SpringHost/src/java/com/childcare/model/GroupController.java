/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
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
        
        public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
        
    /**
     * 
     * @param group
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody Anchorgroup group)
    {
        try{
         Family familyInstance = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(group.getFid().getFid());
         String hashed = familyInstance.getFamilyPassword(); //find stored hashed password with given fid
         if (BCrypt.checkpw(group.getFid().getFamilyPassword(), hashed) && //if input password match stored password of given fid
                 Objects.equals(this.jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(group.getGid()).getFid().getFid(), familyInstance.getFid()))  //and if stored fid of input gid matches input fid
         {     
                 jdbcDataDAO.getDaoAnchorGroup().updateGroup(group);
                 return new Response();
         }      
        }
              catch (DataAccessException e)
               {
                 return new Response(e.getMessage());
               }
        return new Response("Invalid FamilyID or FamilyPassword");
    } 
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{gid}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "gid")Long gid) {
        try{
        Anchorgroup group = (Anchorgroup)jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(gid);
                return group;
        }
         catch (DataAccessException e)
         {
           return new Response(e.getMessage());
         }

    }
    
     /**
     * 
     * @param group
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object create(@RequestBody Anchorgroup group)
    {

       try{
         Family familyInstance = this.jdbcDataDAO.getDaoFamily().getFamilyInstance(group.getFid().getFid());
         String hashed = familyInstance.getFamilyPassword(); //find stored hashed password with given fid
         if (BCrypt.checkpw(group.getFid().getFamilyPassword(), hashed) && //if input password match stored password of given fid
                 Objects.equals(this.jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(group.getGid()).getFid().getFid(), familyInstance.getFid()))  //and if stored fid of input gid matches input fid
         {
             long id = jdbcDataDAO.getDaoAnchorGroup().createGroup(group);
            return new Response(""+id);
         }
        }
        catch (DataAccessException e)
        {
           return new Response(e.getMessage());
        }
         return new Response("Invalid FamilyID or FamilyPassword");
       
    } 
    
        
        
    
}
