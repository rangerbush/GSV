/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Anchor;
import com.childcare.entity.Device;
import com.childcare.entity.structure.Response;
import java.util.Iterator;
import java.util.List;
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
@RequestMapping(value = "/anchor")
public class AnchorController {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
   
    
    @ResponseBody
    @RequestMapping(value = "/register/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody List<Anchor> anchor, @PathVariable(value = "pw") String pswd) {
        try{
        // String hashed = this.jdbcDataDAO.getFamilyPasswordByGID(anchor.getAnchorPK().getGid()); //find stored hashed password with given fid
          //if (BCrypt.checkpw(anchor, hashed)) //if passes the check, do the update
               if (!this.groupCheck(anchor))
                   return new Response("Empty List or GID integrity failure");
               if (this.jdbcDataDAO.getUtility().Validator(pswd, anchor.get(0).getAnchorPK().getGid())) //if input password matches the record of given gid
               return new Response(""+jdbcDataDAO.getDaoAnchor().createAnchor(anchor));
               else
                   return new Response("Incorrect Password");          
           //else
              // return new Response("Invalid FamilyID or FamilyPassword");
        }
        catch (DataAccessException e)
        {
          return new Response(e);
        }
     }
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{gid}/{seq}/{pw}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "gid")Long gid,@PathVariable(value = "seq")int seq, @PathVariable(value = "pw")String pswd) {
        
        try{
            if (this.jdbcDataDAO.getUtility().Validator(pswd, gid))
            {
        Anchor anchor = (Anchor)jdbcDataDAO.getDaoAnchor().getAnchorInstance(gid, seq);
                return new Response(anchor);
            }
            else
                return new Response("Incorrect Password");       
        }
         catch (DataAccessException e)
         {
          return new Response(e);
         }

    }
    

    @ResponseBody
    @RequestMapping(value = "/delete/{passwd}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object deleteAnchor(@RequestBody List<Anchor> list,@PathVariable(value = "passwd")String passwd)
    {
        if (!this.groupCheck(list))
            return new Response("Empty List or GID integrity failure");
        else
        if (!this.jdbcDataDAO.getUtility().Validator(passwd, list.get(0).getAnchorPK().getGid()))
            return new Response("Invalid FamilyID or FamilyPassword");
        else
            this.jdbcDataDAO.getDaoAnchor().deleteAnchor(list);
            return new Response();
    }
   
    
    /**
     * FOR TEST ONLY
     * @return 
     */
     @ResponseBody
    @RequestMapping(value = "/fetch", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetchAll() {
        
        try{
        return new Response(this.jdbcDataDAO.getDaoAnchor().getAllAnchorInstance());
        }
         catch (DataAccessException e)
         {
          return new Response(e);
         }

    }
    
    /**
     * check if all elements in list share the same groupID
     * @param list
     * @return true if share the same gid
     */
    private boolean groupCheck(List<Anchor> list)
    {
        if (null!=list & !list.isEmpty())
        {
            long gid = list.get(0).getAnchorPK().getGid();
            for (int i=1; i<list.size();i++)
            {
                if (list.get(i).getAnchorPK().getGid()!=gid)
                    return false;
            }
            return true;
        }
        return false; //if list is null or is empty
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/update/{passwd}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody List<Anchor> list,@PathVariable(value = "passwd")String passwd)
    {
       if (this.groupCheck(list))
       {
             try{
                 if (this.jdbcDataDAO.getUtility().Validator(passwd, list.get(0).getAnchorPK().getGid()))
                      {
                       this.jdbcDataDAO.getDaoAnchor().updateAnchor(list);
                         return new Response();
                         }
                     else
                          return new Response("Invalid FamilyID or FamilyPassword");
                 }
             catch (DataAccessException e)
             {
                     return new Response(e);
              }
        
       }
        else
            return new Response("Empty List or GID integrity failure");
    }
    
}
