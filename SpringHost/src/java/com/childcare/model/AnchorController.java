/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Anchor;
import com.childcare.entity.Device;
import com.childcare.entity.structure.Response;
import com.childcare.model.service.serviceAnchor;
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
    private serviceAnchor service;
    
 
   
    
    @ResponseBody
    @RequestMapping(value = "/register/{pw}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody List<Anchor> anchor, @PathVariable(value = "pw") String pswd) {
            return this.service.register(anchor, pswd);
     }
    
    @ResponseBody
    @RequestMapping(value = "/fetch/{gid}/{seq}/{pw}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@PathVariable(value = "gid")Long gid,@PathVariable(value = "seq")int seq, @PathVariable(value = "pw")String pswd) { 
           return this.service.fetch(gid, seq, pswd);
    }
    
        
    @ResponseBody
    @RequestMapping(value = "/fetchgid/{gid}/{pw}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetchByGID(@PathVariable(value = "gid")Long gid,@PathVariable(value = "pw")String pswd) { 
           return this.service.fetchByGID(gid,pswd);
    }
    

    @ResponseBody
    @RequestMapping(value = "/delete/{passwd}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object deleteAnchor(@RequestBody List<Anchor> list,@PathVariable(value = "passwd")String passwd)
    {
            return this.service.deleteAnchor(list, passwd);
    }
   
    
    /**
     * FOR TEST ONLY
     * @return 
     */
     @ResponseBody
    @RequestMapping(value = "/fetch", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetchAll() {
       return this.service.fetchAll();

    }
    

    
    
    @ResponseBody
    @RequestMapping(value = "/update/{passwd}", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody List<Anchor> list,@PathVariable(value = "passwd")String passwd)
    {
            return this.service.update(list, passwd);
    }
    
}
