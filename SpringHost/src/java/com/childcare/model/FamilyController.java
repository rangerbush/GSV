/*
 * In family DAO, password from client will be hashed again to be operated in Database, no matter if that password is raw or has been hashed once by client.
 * 
 * 
 */
package com.childcare.model;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
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
@RequestMapping(value = "/family")
public class FamilyController {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
        
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
            return new Response(ex.getMessage());
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
    
    /**
     * Insert a new family record
     * @param family
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object create(@RequestBody Family family)
    {
       // Gson gson = new Gson();
       try{
        return new Response(jdbcDataDAO.getDaoFamily().createFamily(family)+"");
        }
        catch (DataAccessException e)
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
            return new Response("Invalid FamilyID or FamilyPassword");
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
        
        
        
}
