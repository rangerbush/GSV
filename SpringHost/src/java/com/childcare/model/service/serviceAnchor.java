/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.childcare.entity.Anchor;
import com.childcare.entity.AnchorPK;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import com.childcare.entity.structure.Response;
import static com.childcare.entity.structure.Response.ERROR_INTEGRITY;
import static com.childcare.entity.structure.Response.GENERAL_SUCC;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.entity.wrapper.Wrapper;
import com.childcare.entity.wrapper.WrapperDual;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.support.JsonWebTokenUtil;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;

@Service("serviceAnchor")
/**
 *
 * @author New User
 */
public class serviceAnchor {

    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    @Resource
    private ServiceGroup serviceGroup;

    public Object register(List<Anchor> anchor, long uid, String access) {
        try {
            if (!this.groupCheck(anchor)) 
                return new Response(ERROR_INTEGRITY,"Empty List or GID integrity failure");
            if (!JsonWebTokenUtil.checkAccess(uid, access))
                return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
            Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(anchor.get(0).getAnchorPK().getGid());
            if (!this.familyAuth(uid, f.getFid()))
                return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
            return new ResponsePayload(Response.GENERAL_SUCC,"Anchors created.",jdbcDataDAO.getDaoAnchor().createAnchor(anchor));
        } catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) {
            return new Response(e);
        }
    }

    /**
     * check if all elements in list share the same groupID
     *
     * @param list
     * @return true if share the same gid
     */
    private boolean groupCheck(List<Anchor> list) {
        if (null != list & !list.isEmpty()) {
            int gid = list.get(0).getAnchorPK().getGid();
            return list.stream().allMatch(a -> a.getAnchorPK().getGid()==gid);
        }
        return false; //if list is null or is empty
    }
    
    private boolean familyAuth(long uid,int fid)
    {
        List<Family> list = this.jdbcDataDAO.getDaoFamily().findMyFamilies(uid);
        return list.stream().map(f -> f.getFid()).anyMatch(id -> id==fid);
    }
            

    public Object fetch(Integer gid, int seq,Long uid, String access) {
        try {
            if (!JsonWebTokenUtil.checkAccess(uid, access))
               return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
            Anchor anchor = jdbcDataDAO.getDaoAnchor().getAnchorInstance(gid, seq);
            Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(anchor.getAnchorPK().getGid());
            if (!this.familyAuth(uid, f.getFid()))
               return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
            return new ResponsePayload(GENERAL_SUCC,"Anchor fetched.",anchor);
        } catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) {
            return new Response(e);
        }
    }

    public Object fetchByGID(Integer gid,Long uid, String access) {
        try {
            if (!JsonWebTokenUtil.checkAccess(uid, access))
               return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
            List<Anchor> list = this.jdbcDataDAO.getDaoAnchor().findByGID(gid);
            if (list.isEmpty())
            {
            Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(list.get(0).getAnchorPK().getGid());
            if (!this.familyAuth(uid, f.getFid()))
               return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
            }
            return new ResponsePayload(GENERAL_SUCC,"Anchor fetched.",list);
        } catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) {
            return new Response(e);
        }
    }

    public Object deleteAnchor(List<Anchor> list,long uid, String access) {
        try{
            if (!this.groupCheck(list)) 
                return new Response(ERROR_INTEGRITY,"Empty List or GID integrity failure");
            if (!JsonWebTokenUtil.checkAccess(uid, access))
               return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
            Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(list.get(0).getAnchorPK().getGid());
            if (!this.familyAuth(uid, f.getFid()))
               return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
            this.jdbcDataDAO.getDaoAnchor().deleteAnchor(list);
            return new Response(GENERAL_SUCC,"Anchors deleted.");
        }   catch (DataAccessException|JWTVerificationException|UnsupportedEncodingException e) {
            return new Response(e);
        }
    }

    /**
     * FOR TEST ONLY
     *
     * @return
     */
    public Object fetchAll() {

        try {
            return new ResponsePayload(this.jdbcDataDAO.getDaoAnchor().getAllAnchorInstance());
        } catch (DataAccessException e) {
            return new Response(e);
        }

    }
    
    public Response fetchChildAnchors(int cid,long uid,String access)
    {
        try{
            if (!JsonWebTokenUtil.checkAccess(uid, access))
               return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
            Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByCID(cid);
            if (!this.familyAuth(uid, f.getFid()))
               return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
            List<Anchor> list = this.jdbcDataDAO.getDaoAnchor().findByCID(cid);
            List<Integer> gidList =  list.stream().map(a -> a.getAnchorgroup().getGid()).distinct().collect(Collectors.toList());
            List<Anchorgroup> groupList = this.jdbcDataDAO.getDaoAnchorGroup().getGroups(gidList);
            List<List<Anchor>> result = groupList.stream().map( gid ->list.stream()
                    .filter(a -> a.getAnchorPK().getGid()==gid.getGid()) //当指定一个GID时，找出所有满足的anchor，替换其中的group后导出
                    .map(a -> {
                    a.setAnchorgroup(gid);
                    return a;
                    })
                    .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return new ResponsePayload(GENERAL_SUCC,"Anchors of Child "+cid+" fetched.",result);
            
        } catch (IncorrectResultSizeDataAccessException ex) 
        {
            return new Response(Response.EXCEPTION_DATA_ACCESS,"Requested child does not exist.");
        }
        catch (JWTVerificationException|UnsupportedEncodingException|DataAccessException e) {
            return new Response(e);
        }
    }

    /**
     * Create a Group first, and then create a list of anchors using returned GID
     * @param wrapper
     * @return 
     */
    @Transactional
    public Object createGroupAndAnchor(WrapperDual<Anchorgroup,List<Anchor>> wrapper) throws DataAccessException
    {
        Response response = this.serviceGroup.create(wrapper.getUid(),wrapper.getAccess(),wrapper.getPayload1());
        if (response.getStatus_code()!=GENERAL_SUCC)
            return response;
        ResponsePayload rp = (ResponsePayload)response;
        int gid =((int)rp.getPayload()); 
        List<Anchor> list = wrapper.getPayload2().stream().map(a -> {
                a.setAnchorPK(new AnchorPK(gid,1));
                return a;
        }).collect(Collectors.toList());
        jdbcDataDAO.getDaoAnchor().createAnchor(list);
        return new ResponsePayload(Response.GENERAL_SUCC,"Group "+gid+" and related "+list.size()+" anchors created.",gid);
        }
    
    @Transactional
    public Object recreateAnchor(Wrapper<List<Anchor>> wrapper)
    {
        if (!this.groupCheck(wrapper.getPayload())) 
                return new Response(ERROR_INTEGRITY,"Empty List or GID integrity failure");
        try {
            if (!JsonWebTokenUtil.checkAccess(wrapper.getUid(), wrapper.getAccess()))
                return new Response(Response.ERROR_TOKEN_ACCESS_UID,"This is not an access token or you do not own this token.");
        } catch (JWTVerificationException|UnsupportedEncodingException ex) {
            return new Response(ex);
        }
        try{
        Anchorgroup group = this.jdbcDataDAO.getDaoAnchorGroup().getGroupInstance(wrapper.getPayload().get(0).getAnchorPK().getGid());
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            //说明anchorgroup不存在
            return new Response(Response.EXCEPTION_DATA_ACCESS,"Requested group does not exist.");
        }
        Family f = this.jdbcDataDAO.getDaoFamily().findFamilyByGID(wrapper.getPayload().get(0).getAnchorPK().getGid());
        if (!this.familyAuth(wrapper.getUid(), f.getFid()))
            return new Response(Response.ERROR_FAMILY_PERMISSION,"You are not member of this family thus you do not have permission to edit it.");
        int seq = this.jdbcDataDAO.getDaoAnchor().recreateAnchor(wrapper.getPayload());
        return new Response(GENERAL_SUCC,seq+" anchors recreated.");
    }

}


