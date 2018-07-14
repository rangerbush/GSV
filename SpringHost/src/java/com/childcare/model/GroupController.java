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
import com.childcare.model.service.ServiceGroup;
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
    @Resource
    private ServiceGroup serviceGroup;
        
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
        return this.serviceGroup.update(wrapper.getUid(),wrapper.getAccess() ,wrapper.getGroup());
    } 
    
    /**
     * 
     * @param wrapper
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/fetch", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@RequestBody AnchorGroupWrapper wrapper) {
        return this.serviceGroup.getInstance(wrapper);
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@RequestBody AnchorGroupWrapper wrapper) {
        return this.serviceGroup.delete(wrapper);
    }
    
        @ResponseBody
    @RequestMapping(value = "/register/help", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object createHelp()
    {
        AnchorGroupWrapper w = new AnchorGroupWrapper();
        w.setGroup(new Anchorgroup(Integer.getInteger("123")));
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
        return this.serviceGroup.create(wrapper);
    } 
    
        
        
    
}
