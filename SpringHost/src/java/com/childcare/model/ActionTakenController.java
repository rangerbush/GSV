/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.ActionTaken;
import com.childcare.entity.ActionTakenPK;
import com.childcare.entity.structure.Response;
import com.childcare.model.service.serviceActionTaken;
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

@RestController
@RequestMapping(value = "/actiontaken")
/**
 *
 * @author New User
 */
public class ActionTakenController {
    @Resource
    private serviceActionTaken service;
    
     @ResponseBody
    @RequestMapping(value = "/register/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@PathVariable(value = "pw") String passwd, @RequestBody ActionTaken at)
    {
         return this.service.create(at, passwd);
    }
    
    @ResponseBody
    @RequestMapping(value = "/demo/{pw}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object demo(@PathVariable(value = "pw") String passwd)
    {
         return this.service.demo(passwd);
    }
    
    @ResponseBody
    @RequestMapping(value = "/demopk", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object demoPK()
    {
         return new ActionTakenPK(1,132);
    }
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "pw") String passwd, @RequestBody ActionTakenPK pk)
    {
         return this.service.read(pk, passwd);
    }
    
    @ResponseBody
    @RequestMapping(value = "/update/{pw}/{status}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@PathVariable(value = "pw") String passwd,@PathVariable(value = "status") int status, @RequestBody ActionTakenPK pk)
    {
         return this.service.update(pk, passwd, status);
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@PathVariable(value = "pw") String passwd, @RequestBody ActionTakenPK pk)
    {
         return this.service.delete(pk, passwd);
    }
    
    
    
    /*
    {
    "response": {
        "actionTakenPK": {
            "aid": 1,
            "gid": 132
        },
        "status": 0,
        "actionSet": null,
        "anchorgroup": null
         }
    }
    */
    
}
