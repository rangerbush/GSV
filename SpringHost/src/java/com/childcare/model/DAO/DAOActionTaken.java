/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.ActionTaken;
import com.childcare.entity.ActionTakenPK;
import com.childcare.entity.Anchor;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
     
    public ActionTaken getActionTakenInstance(int AID,int GID)
    {
        String sql = "SELECT `ActionTaken`.`AID`,\n" +
                "    `ActionTaken`.`GID`,\n" +
                "    `ActionTaken`.`Status`\n" +
                "FROM `GSV_DB`.`ActionTaken` WHERE `ActionTaken`.`AID` = "+AID+" AND `ActionTaken`.`GID` ="+GID+" AND (SELECT `Discard` from `GSV_DB`.`ActionSet` where `ActionSet`.`AID` = "+AID+")=0;";
        return (ActionTaken)this.jdbcTemplate.queryForObject(sql,new ActionMapper());
    }
    
    public void createActionTaken(int AID, int GID, int Status)
    {
        String sql = "INSERT INTO `GSV_DB`.`ActionTaken`\n" +
                     "(`AID`,\n" +
                     "`GID`,\n" +
                     "`Status`)\n" +
                     "VALUES\n" +
                     "("+AID+",\n" +
                     ""+GID+",\n" +
                     Status+");";
        this.jdbcTemplate.execute(sql);
    }
    
    public void updateActionTaken(ActionTakenPK pk,ActionTaken newAction)
    {
        String sql ="UPDATE `GSV_DB`.`ActionTaken`\n" +
                    "SET\n" +
                    "`AID` ="+newAction.getActionTakenPK().getAid()+",\n" +
                    "`GID` = "+newAction.getActionTakenPK().getGid()+",\n" +
                    "`Status` = "+newAction.getStatus()+"\n" +
                    "WHERE `AID` = "+pk.getAid()+" AND `GID` = "+pk.getGid()+";";
        this.jdbcTemplate.execute(sql);
    }
    
    public void deleteActionTaken(ActionTakenPK pk)
    {
        String sql = "DELETE FROM `GSV_DB`.`ActionTaken`\n" +
                     "WHERE `AID` = "+pk.getAid()+" AND `GID` = "+pk.getGid()+";";
                this.jdbcTemplate.execute(sql);
    }
    
    public List<ActionTaken> getAll()
    {
        String sql = "SELECT `ActionTaken`.`AID`,\n" +
                "    `ActionTaken`.`GID`,\n" +
                "    `ActionTaken`.`Status`\n" +
                "FROM `GSV_DB`.`ActionTaken`;";
        return this.jdbcTemplate.query(sql,new ActionMapper());
    }
}
