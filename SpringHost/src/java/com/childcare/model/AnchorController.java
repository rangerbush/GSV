/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Anchor;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Child;
import com.childcare.entity.structure.Response;
import com.childcare.entity.wrapper.Wrapper;
import com.childcare.entity.wrapper.WrapperDual;
import com.childcare.model.service.serviceAnchor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
@RequestMapping(value = "/anchor")
public class AnchorController {
    @Resource
    private serviceAnchor service;

    @ResponseBody
    @RequestMapping(value = "/create", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object register(@RequestBody Wrapper<List<Anchor>> wrapper) {
           return this.service.register((List<Anchor>)wrapper.getPayload(),wrapper.getUid(),wrapper.getAccess());
     }

    @ResponseBody
    @RequestMapping(value = "/test", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object registerTest(@RequestParam(value = "gid")Long gid) {
            return gid;
     }

    @ResponseBody
    @RequestMapping(value = "/create/help", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object registerHelp() {
            List<Anchor> list = new ArrayList();
            list.add(new Anchor(135,1));
            list.add(new Anchor(135,2));
            list.add(new Anchor(135,3));
            Wrapper wrapper = new  Wrapper(23,"token",list);
            return wrapper;
     }


    @ResponseBody
    @RequestMapping(value = "/fetch", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object fetch(@RequestParam(value = "gid")Integer gid,@RequestParam(value = "seq")int seq,@RequestParam("uid")Long uid,@RequestParam("access")String access) { 
           return this.service.fetch(gid, seq, uid,access);
    }
    
 
    @ResponseBody
    @RequestMapping(value = "/fetch_gid", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object fetchByGID(@RequestBody Wrapper<Integer> wrapper) { 
           return this.service.fetchByGID(wrapper.getPayload(),wrapper.getUid(),wrapper.getAccess());
    }
    

    @ResponseBody
    @RequestMapping(value = "/delete", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object deleteAnchor(@RequestBody Wrapper<List<Anchor>> wrapper)
    {
            return this.service.deleteAnchor(wrapper.getPayload(), wrapper.getUid(),wrapper.getAccess());
    }
    
    /**
     * FOR TEST ONLY
     * @return 
     */
     @ResponseBody
    @RequestMapping(value = "/fetch_all", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object fetchAll() {
       return this.service.fetchAll();

    }
    
    @ResponseBody
    @RequestMapping(value = "/create_ganda", method = POST,produces = { APPLICATION_JSON_VALUE })
    public Object createGroupAndAnchor(@RequestBody WrapperDual<Anchorgroup,List<Anchor>> wrapper) {
        try{
           return this.service.createGroupAndAnchor(wrapper);
        } catch (DataAccessException e)
        {
            return new Response(e);
        }
     }
    
    @ResponseBody
    @RequestMapping(value = "/create_ganda/help", method = GET,produces = { APPLICATION_JSON_VALUE })
    public Object createGroupAndAnchorHelp() {
            WrapperDual<Anchorgroup,List<Anchor>> wrapper = new WrapperDual();
            Anchorgroup g = new Anchorgroup();
            g.setCid(new Child(5));
            wrapper.setPayload1(g);
            return wrapper;
     }
    
    @ResponseBody
    @RequestMapping(value = "/recreate", method = POST,produces = {APPLICATION_JSON_VALUE})
    public Object recreate(@RequestBody Wrapper<List<Anchor>> wrapper) {
        try{
           return this.service.recreateAnchor(wrapper);
        } catch (DataAccessException e)
        {
            return new Response(e);
        }
     }
  
    @ResponseBody
    @RequestMapping(value = "/fetch_child_anchor", method = POST,produces = {APPLICATION_JSON_VALUE})
    public Object fetchChildAnchor(@RequestBody Wrapper<Integer> wrapper) {
            return this.service.fetchChildAnchors(wrapper.getPayload(), wrapper.getUid(), wrapper.getAccess());
     }
}
