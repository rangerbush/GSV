/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Anchor;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.JdbcDataDAOImpl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.stereotype.Service;
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

    public Object register(List<Anchor> anchor, String pswd) {
        try {
            // String hashed = this.jdbcDataDAO.getFamilyPasswordByGID(anchor.getAnchorPK().getGid()); //find stored hashed password with given fid
            //if (BCrypt.checkpw(anchor, hashed)) //if passes the check, do the update
            if (!this.groupCheck(anchor)) {
                return new Response(701,"Empty List or GID integrity failure");
            }
            if (this.jdbcDataDAO.getUtility().Validator(pswd, anchor.get(0).getAnchorPK().getGid())) //if input password matches the record of given gid
            {
                return new ResponsePayload(Response.GENERAL_SUCC,"",jdbcDataDAO.getDaoAnchor().createAnchor(anchor));
            } else {
                return new Response(703,"Incorrect Password");
            }
            //else
            // return new Response("Invalid FamilyID or FamilyPassword");
        } catch (DataAccessException e) {
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
            long gid = list.get(0).getAnchorPK().getGid();
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getAnchorPK().getGid() != gid) {
                    return false;
                }
            }
            return true;
        }
        return false; //if list is null or is empty
    }

    public Object fetch(Long gid, int seq, String pswd) {

        try {
            if (this.jdbcDataDAO.getUtility().Validator(pswd, gid)) {
                Anchor anchor = (Anchor) jdbcDataDAO.getDaoAnchor().getAnchorInstance(gid, seq);
                return new ResponsePayload(anchor);
            } else {
                return new Response(700,"Incorrect Password");
            }
        } catch (DataAccessException e) {
            return new Response(e);
        }
    }

    public Object fetchByGID(Long gid, String password) {
        try {
            if (this.jdbcDataDAO.getUtility().Validator(password, gid)) {
                return new ResponsePayload(this.jdbcDataDAO.getDaoAnchor().findByGID(gid));
            } else {
                return new Response(700,"Incorrect Password");
            }
        } catch (DataAccessException e) {
            return new Response(e);
        }
    }

    public Object deleteAnchor(List<Anchor> list, String passwd) {
        if (!this.groupCheck(list)) {
            return new Response(700,"Empty List or GID integrity failure");
        } else if (!this.jdbcDataDAO.getUtility().Validator(passwd, list.get(0).getAnchorPK().getGid())) {
            return new Response(700,"Invalid FamilyID or FamilyPassword");
        } else {
            this.jdbcDataDAO.getDaoAnchor().deleteAnchor(list);
        }
        return new Response();
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

    public Object update(List<Anchor> list, String passwd) {
        if (this.groupCheck(list)) {
            try {
                if (this.jdbcDataDAO.getUtility().Validator(passwd, list.get(0).getAnchorPK().getGid())) {
                    this.jdbcDataDAO.getDaoAnchor().updateAnchor(list);
                    return new Response();
                } else {
                    return new Response(700,"Invalid FamilyID or FamilyPassword");
                }
            } catch (DataAccessException e) {
                return new Response(e);
            }

        } else {
            return new Response(700,"Empty List or GID integrity failure");
        }
    }

}
