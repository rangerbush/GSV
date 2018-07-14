/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Account;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.wrapper.NewDeviceWrapper;
import com.childcare.entity.wrapper.Wrapper;
import com.childcare.model.service.serviceDevice;
import com.childcare.model.support.CoordinateConversion;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
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
     private serviceDevice service;
    /**
     * Register a new device
     * @param device
     * @return Fail:xxx if failed, SUCC is successful
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody Device device) {
            return this.service.register(device);
     }
    
    @ResponseBody
    @RequestMapping(value = "/register_add", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object registerAndAdd(@RequestBody NewDeviceWrapper wrapper) {
            return this.service.registerAndAdd(wrapper);
     }
    
    @ResponseBody
    @RequestMapping(value = "/register_add/demo", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object registerAndAdd() {
            NewDeviceWrapper wrapper = new NewDeviceWrapper();
            wrapper.setAccess("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVSUQiOjIzLCJUeXBlIjoiQWNjZXNzIiwiZXhwIjoxNTIzNjE2Njk3LCJUVEwiOjI0LCJpYXQiOjE1MjM1MzAyOTd9.wHwjU0SLq_j_Mg_meJx0yT7ncau59bRTL1WU3uL7lfE");
            wrapper.setDevice(new Device("deviceId"));
            
            return wrapper;
    }
    
    //call from phones
    /**
     * update pulse,long&lati, timestamp; family password required.
     * @param wrapper
     * @return 
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object edit(@RequestBody NewDeviceWrapper wrapper) {
            return this.service.edit(wrapper);
     }
    
    //call from watches
    @ResponseBody
    @RequestMapping(value = "/touch", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object touch(@RequestBody Device device) {
            return this.service.touch(device);
     }
    
    @ResponseBody
    @RequestMapping(value = "/device_of_family", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object deviceOfFamily(@RequestBody Wrapper<Integer> wrapper) {
           return this.service.findByFID(wrapper);
     }

    

    
    @ResponseBody
    @RequestMapping(value = "/test_run", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object testRunnable(@RequestParam("token") String token,@RequestParam("raw") String raw) {
          return this.service.testRunnable(token,raw);
     }
    
        @ResponseBody
    @RequestMapping(value = "/test_lonlat2utm", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object testUtm(@RequestParam("lon") double lon,@RequestParam("lat") double lat) {
            CoordinateConversion cc = new CoordinateConversion();
            String utm = cc.latLon2UTM(lat,lon);
            Map<String,String> map = new HashMap();
            map.put("UTM", utm);
            double[] grid = cc.utm2LatLon(utm);
            map.put("Lat", grid[0]+"");
            map.put("Long", grid[1]+"");
            return map;
     }
    
    @ResponseBody
    @RequestMapping(value = "/test_constraint", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object testConstraint() {
        //Device device = this.jdbcDataDAO.getDaoDevice().getDeviceInstance("demo");
          return this.service.testConstraint("demo");
     }

    
    
    @ResponseBody
    @RequestMapping(value = "/update", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object update(@RequestBody Device device)
    {
            return this.service.update(device);
    }
        

    
    @ResponseBody
    @RequestMapping(value = "/help", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object help()
    {
            Device device = new Device();
            return device;
    }
    
    
    
    @ResponseBody
    @RequestMapping(value = "/delete/{id}/{family}/{passwd}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object delete(@PathVariable(value = "id")String id,@PathVariable(value = "family")int fid,@PathVariable(value = "passwd")String passwd)
    {
            return this.service.delete(id, fid, passwd);
    }
    
    
    
        @ResponseBody
    @RequestMapping(value = "/mail/{id}", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object mail(@PathVariable(value = "id")String id) { 
            return this.service.mail(id);
    }
    

    

    /**
     * @param service the service to set
     */
    public void setService(serviceDevice service) {
        this.service = service;
    }


}
