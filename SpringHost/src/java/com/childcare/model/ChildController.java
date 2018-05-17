/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Child;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import com.childcare.entity.wrapper.ChildDeleteWrapper;
import com.childcare.entity.wrapper.ChildFIDWrapper;
import com.childcare.entity.wrapper.ChildWrapper;
import com.childcare.model.service.serviceChild;
import com.childcare.model.support.HashUtil;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/child")
/**
 *
 * @author New User
 */
public class ChildController {
    @Resource
    private serviceChild service;
    
    @ResponseBody
    @RequestMapping(value = "/create_child", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object createChild(@RequestBody ChildWrapper wrapper)
    {
        return this.service.createChild(wrapper.getUid(), wrapper.getToken(), wrapper.getChild());
    }
    
@RequestMapping(value = "/render", method = POST, produces = {IMAGE_GIF_VALUE,IMAGE_JPEG_VALUE,IMAGE_PNG_VALUE})
@ResponseBody
public Response execute(@RequestParam int cid,@RequestParam String token,@RequestParam int uid,HttpServletRequest httpServletRequest,
       HttpServletResponse httpServletResponse) {
    return this.service.getImage(cid, token, uid, httpServletRequest, httpServletResponse);
}
    
    
    @ResponseBody
    @RequestMapping(value = "/image",headers=("content-type=multipart/*"), method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object editImage(@RequestParam("file") MultipartFile inputFile,@RequestParam("token") String token,@RequestParam("uid") Integer uid,@RequestParam("cid") Integer cid)
    {
        try{
            return this.service.editImage(cid, token, uid, inputFile);
        }
        catch (RuntimeException e)
        {
            return new Response(e);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/edit_child", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object editChild(@RequestBody ChildWrapper wrapper)
    {
        return this.service.editChild(wrapper.getUid(), wrapper.getToken(), wrapper.getChild());
    }
    
    @ResponseBody
    @RequestMapping(value = "/get_child", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object getChild(@RequestBody ChildFIDWrapper wrapper)
    {
        return this.service.readChild(wrapper.getUid(), wrapper.getToken(), wrapper.getFid());
    }
    
    @ResponseBody
    @RequestMapping(value = "/get_all_children", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object getAllChildren(@RequestBody ChildWrapper wrapper)
    {
        return this.service.readByUID(wrapper.getUid(), wrapper.getToken());
    }
    
    
    
    @ResponseBody
    @RequestMapping(value = "/delete_child", method = POST, produces = { APPLICATION_JSON_VALUE})
    public Object deleteChild(@RequestBody ChildDeleteWrapper wrapper)
    {
        return this.service.deleteChild(wrapper.getUid(), wrapper.getToken(), wrapper.getCid(), wrapper.getFamily_password());
    }
    
    @ResponseBody
    @RequestMapping(value = "/help_delete", method = GET, produces = { APPLICATION_JSON_VALUE})
    public Object deleteChild()
    {
        return new ChildDeleteWrapper();
    }
    
    @ResponseBody
    @RequestMapping(value = "/help_create", method = GET, produces = { APPLICATION_JSON_VALUE})
    public Object helpChild()
    {
       ChildWrapper w = new ChildWrapper(null,23,"token");
       Child child = new Child(3);
       child.setFid(new Family(11111115));
       child.setDeviceID(new Device("did"));
       child.setName("name");
       child.setImage("ImageURL");
       child.setAge(6);
       w.setChild(child);
       return w;
    }
    
    
    
}
