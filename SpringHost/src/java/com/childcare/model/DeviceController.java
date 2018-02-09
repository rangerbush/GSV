/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Account;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
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
@RequestMapping(value = "/device")
public class DeviceController {
    
     @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public void setJdbcDataDAO(JdbcDataDAOImpl jdbcDataDAO) {  
        this.jdbcDataDAO = jdbcDataDAO;  
    }  
    
    /**
     * Register a new device
     * @param device
     * @return Fail:xxx if failed, SUCC is successful
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody Device device) {
        try{
        jdbcDataDAO.createDevice(device);
        }
        catch (DataAccessException|FamilyNullException e)
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
    
    
        @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody Device device)
    {
       try{
        jdbcDataDAO.updateDevice(device);
        }
        catch (DataAccessException|FamilyNullException e)
        {
             return "{\"response\":\""+e.getMessage()+"\"}";
        }
        return "{\"response\":\"SUCC\"}";
    }
    
    
    
        @ResponseBody
    @RequestMapping(value = "/mail/{id}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object mail(@PathVariable(value = "id")String id) { 
        List<DeviceAudit> list;
        list = jdbcDataDAO.getAudit(id);
        Iterator it = list.iterator();
        String text="";
        DeviceAudit instance;
        while (it.hasNext())
        {
            instance = (DeviceAudit)it.next();
            text = text+instance.getDeviceID().getDeviceID()+","+instance.getDate()+","+instance.getLongitude().setScale(5,BigDecimal.ROUND_HALF_EVEN)+","+instance.getLatitude().setScale(5,BigDecimal.ROUND_HALF_EVEN)+"\n";
        }
         try {
             Courier.sendMail(text);
         } catch (RuntimeException ex) {
            return "{\"response\":\"RuntimeException: "+ex.getMessage()+"\"}";
         } 
           return "{\"response\":\"SUCC\"}";


    }
}
