/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Contact;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.wrapper.ContactWrapper;
import com.childcare.model.service.serviceContact;
import com.childcare.model.service.serviceDevice;
import javax.annotation.Resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
@RequestMapping(value = "/contact")
public class ContactController {
     @Resource
     private serviceContact service;
     
    @ResponseBody
    @RequestMapping(value = "/help", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object help() {
        ContactWrapper wrapper = new ContactWrapper();
        wrapper.setAccess("access");
        wrapper.setUid(23);
        Contact c = new Contact();
        c.setContactId(20);
        wrapper.setContact(c);
        return wrapper;
     }
     
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody ContactWrapper wrapper) {
            return this.service.create(wrapper);
     }
    
    @ResponseBody
    @RequestMapping(value = "/retrieve", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object retrieve(@RequestBody ContactWrapper wrapper) {
            return this.service.retrieve(wrapper);
     }
    
    @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody ContactWrapper wrapper) {
            return this.service.update(wrapper);
     }
    
    @ResponseBody
    @RequestMapping(value = "/delete", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@RequestBody ContactWrapper wrapper) {
            return this.service.delete(wrapper);
     }
}
