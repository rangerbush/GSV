/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.ActionSet;
import com.childcare.entity.ActionTaken;
import com.childcare.entity.ActionTakenPK;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author New User
 */
public class DAOActionTaken {
        @Resource 
    private JdbcTemplate jdbcTemplate;

    
     protected class ActionMapper implements RowMapper, Serializable{
         @Override
         public Object mapRow(ResultSet arg0, int arg1) throws SQLException
         {
             ActionTaken action = new ActionTaken();
             action.setActionTakenPK(new ActionTakenPK(arg0.getInt("AID"),arg0.getInt("GID")));
             action.setStatus(arg0.getInt("Status"));
             return action;
         }   
    }
    
}
