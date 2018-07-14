/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Account;
import com.childcare.entity.DeviceToken;
import com.childcare.entity.Family;
import com.childcare.entity.PushTarget;
import com.childcare.entity.Supervisor;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author New User
 */
public class DAOSupervisor {
     @Resource 
    private JdbcTemplate jdbcTemplate;
    
    protected class Mapper implements RowMapper,Serializable{  
            /**
             * 
             * @param arg0 the ResultSet to map (pre-initialized for the current row)
             * @param arg1 the number of the current row
             * @return
             * @throws SQLException 
             */
        @Override  
        public Object mapRow(ResultSet arg0, int arg1) throws SQLException {  
            // TODO Auto-generated method stub  
            Supervisor supervisor = new Supervisor();
            supervisor.setDeviceID(arg0.getString("DeviceID"));
            return supervisor;  
        }  
          
    } 
    
    public void create(String did)
    {
        String sql = "INSERT IGNORE INTO `GSV_DB`.`supervisor` " +
                    "(`DeviceID`) " +
                    "VALUES('"+did+"');";
        this.jdbcTemplate.execute(sql);
    }
    
    
    
    public List<PushTarget> findUserTokenByDID(List<String> didList)
    {
        StringBuilder sb = new StringBuilder();
        if (didList.isEmpty())
            return new ArrayList();
        didList.stream().forEach(f -> sb.append("'").append(f).append("',"));
        sb.deleteCharAt(sb.lastIndexOf(","));
        String sql = "select device_token.device_id,Account_Family.FID  from device_token join Account_Family on device_token.uid = Account_Family.UID "
                + "where Account_Family.FID in (select Device.FID from Device where DeviceID in "
                + "("+sb.toString()+"));";
        return this.jdbcTemplate.query(sql, new pushTargetMapper());
    }
    
        protected class pushTargetMapper implements RowMapper,Serializable{  
            /**
             * 
             * @param arg0 the ResultSet to map (pre-initialized for the current row)
             * @param arg1 the number of the current row
             * @return
             * @throws SQLException 
             */
        @Override  
        public PushTarget mapRow(ResultSet arg0, int arg1) throws SQLException {  
            // TODO Auto-generated method stub  
            PushTarget pt = new PushTarget();
            pt.setDeviceToken(arg0.getString("device_id"));
            pt.setFid(arg0.getInt("FID"));
            return pt;  
        }  
          
    } 
    
}
