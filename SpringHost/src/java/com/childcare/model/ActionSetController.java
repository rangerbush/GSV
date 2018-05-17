/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.ActionSet;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.support.PasswordUtility;
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
@RequestMapping(value = "/actionset")
public class ActionSetController {
    
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
    
    
    /**
     * Check Admin password to create a new actionSet record
     * @param passwd
     * @param desc
     * @return created AID or error msg
     */
    @ResponseBody
    @RequestMapping(value = "/register/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@PathVariable(value = "pw") String passwd, @RequestBody String desc)
    {
        if (PasswordUtility.validateAdminPassword(passwd))
        {
            try{
        return new ResponsePayload(Response.GENERAL_SUCC,"ActionSet Created.",this.jdbcDataDAO.getDaoActionSet().createAction(desc));  //AID of created row
            }
            catch (DataAccessException e)
            {
                return new Response(e);
            }
        }
        else
            return new Response(700,"Invalid Administration Password");
    }
    
    /**
     * fetch an actionSet instance with AID
     * Those rows marked discarded will be ignored
     * @param aid integer AID
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/fetch/{aid}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "aid") int aid)
    {
        try{
            return new ResponsePayload(Response.GENERAL_SUCC,"",this.jdbcDataDAO.getDaoActionSet().getActionInstance(aid));
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete/{pw}/{aid}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@PathVariable(value = "pw") String passwd,@PathVariable(value = "aid") int aid)
    {
        if (PasswordUtility.validateAdminPassword(passwd))
        {
        try
        {
            this.jdbcDataDAO.getDaoActionSet().deleteAction(aid);
            return new Response();
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
        }
        else
            return new Response(700,"Invalid Administration Password");
    }
    
    @ResponseBody
    @RequestMapping(value = "/recover/{pw}/{aid}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object recover(@PathVariable(value = "pw") String passwd,@PathVariable(value = "aid") int aid)
    {
        if (PasswordUtility.validateAdminPassword(passwd))
        {
        try
        {
            this.jdbcDataDAO.getDaoActionSet().recoverAction(aid);
            return new Response();
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
        }
        else
             return new Response(700,"Invalid Administration Password");
            
    }
    
    /**
     * Only Desc will be updated
     * @param passwd
     * @param action
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/update/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@PathVariable(value = "pw") String passwd,@RequestBody ActionSet action)
    {
        if (PasswordUtility.validateAdminPassword(passwd))
        {
        try
        {
            this.jdbcDataDAO.getDaoActionSet().updateAction(action);
            return new Response();
        }
        catch (DataAccessException e)
        {
            return new Response(e);
        }
        }
        else
             return new Response(700,"Invalid Administration Password");
    }
    
    
    
}
