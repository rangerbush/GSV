/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
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
        jdbcDataDAO.updateGroup(group);
        }
        catch (DataAccessException e)
        {
             return "{\"response\":\""+e.getMessage()+"\"}";
        }
        return "{\"response\":\"SUCC\"}";
    } 
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{id}/{family}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "id")String id,@PathVariable(value = "family")int fid) {
        
        try{
        Device device = (Device)jdbcDataDAO.getDeviceInstance(id);
        if (!device.getFid().getFid().equals(fid))
            return "{\"response\":\"Invalid FamilyID\"}";
                return device;
        }

         catch (DataAccessException e)
         {
           return "{\"response\":\""+e.getMessage()+"\"}";
         }

    }
    
        
        
    
}
