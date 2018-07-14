/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.ActionTaken;
import com.childcare.entity.ActionTakenPK;
import com.childcare.entity.structure.Response;
import com.childcare.entity.structure.ResponsePayload;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.exception.NullException;
import com.childcare.model.support.PasswordUtility;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service("serviceActionTaken")
/**
 *
 * @author New User
 */
public class serviceActionTaken {

    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;

    private void nullCheck(ActionTaken at) throws NullException {
        if (at.getActionTakenPK() == null) {
            throw new NullException("Input ActionTakenPK is null, transaction cancelled.");
        }
    }

    private void nullCheck(ActionTakenPK at) throws NullException {
        if (at == null) {
            throw new NullException("Input ActionTakenPK is null, transaction cancelled.");
        }
    }

    /**
     * Insert a new row in DB
     *
     * @param at ActionTaken Instance
     * @param password password of Family related to Group involved
     * @return
     */
    public Response create(ActionTaken at, String password) {
        try {
            this.nullCheck(at);
            if (this.jdbcDataDAO.getUtility().Validator(password, (long) at.getActionTakenPK().getGid())) {
                this.jdbcDataDAO.getDaoActionTaken().createActionTaken(at.getActionTakenPK().getAid(), at.getActionTakenPK().getGid(), at.getStatus());
                return new Response();
            } else {
                return new Response(700,"pf");
            }
        } catch (NullException | DataAccessException ex) {
            return new Response(ex);
        }
    }

    /**
     *
     * @param pk ActionTakenPK containing AID and GID
     * @param password password of Family related to Group involved
     * @return
     */
    public Response read(ActionTakenPK pk, String password) {
        try {
            this.nullCheck(pk);
            if (this.jdbcDataDAO.getUtility().Validator(password, (long) pk.getGid())) {
                return new ResponsePayload(Response.GENERAL_SUCC,"",this.jdbcDataDAO.getDaoActionTaken().getActionTakenInstance(pk.getAid(), pk.getGid()));
            } else {
                return new Response(700,"pf");
            }
        } catch (NullException | DataAccessException e) {
            return new Response(e);
        }
    }

    /**
     * Only status field is modifiable
     *
     * @param pk index of the row to modify
     * @param password password of Family related to Group involved
     * @param newStatus new Status
     * @return
     */
    public Response update(ActionTakenPK pk, String password, int newStatus) {
        try {
            this.nullCheck(pk);
            if (this.jdbcDataDAO.getUtility().Validator(password, (long) pk.getGid())) {
                this.jdbcDataDAO.getDaoActionTaken().updateActionTaken(pk, new ActionTaken(pk, newStatus));
                return new Response();
            } else {
                return new Response(700,"pf");
            }
        } catch (NullException | DataAccessException e) {
            return new Response(e);
        }
    }

    public Response delete(ActionTakenPK pk, String password) {
        try {
            this.nullCheck(pk);
            if (this.jdbcDataDAO.getUtility().Validator(password, (long) pk.getGid())) {
                this.jdbcDataDAO.getDaoActionTaken().deleteActionTaken(pk);
                return new Response();
            } else {
                return new Response(700,"pf");
            }
        } catch (NullException | DataAccessException e) {
            return new Response(e);
        }
    }
    
    public Response demo(String adminPassword)
    {
        try
        {
            if (PasswordUtility.validateAdminPassword(adminPassword))
            {
                List<ActionTaken> list =this.jdbcDataDAO.getDaoActionTaken().getAll();
            return new Response(888,list.isEmpty()?"No entry in Table thus no demo could be given.":list.get(0)+"");
            }
            else
                return new Response(700,"pf");
        }
        catch ( DataAccessException e) {
            return new Response(e);
        }
    }

}
