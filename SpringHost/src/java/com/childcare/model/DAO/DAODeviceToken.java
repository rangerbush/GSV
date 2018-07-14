/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Account;
import com.childcare.entity.DeviceToken;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

//https://blog.csdn.net/liyong199012/article/details/21516817
//Mysql实现数据的不重复写入（insert if not exists）以及新问题：ID自增不连续的解答
/**
 *
 * @author New User
 */
public class DAODeviceToken {
    
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
            DeviceToken token = new DeviceToken();
            token.setDeviceId(arg0.getString("device_id"));
            token.setUid(new Account(arg0.getLong("uid")));
            return token;  
        }  
          
    } 
    
    public void create(String deviceID,long uid)
    {
        
       String sql = "INSERT IGNORE INTO `GSV_DB`.`device_token` " +
                    "(`uid`,`device_id`) " +
                    "VALUES("+uid+",'"+deviceID+"');";
        this.jdbcTemplate.execute(sql);
    }
    
    public List<DeviceToken> searchByUID(long uid)
    {
        String sql = "SELECT `device_token`.`uid`,`device_token`.`device_id` FROM `GSV_DB`.`device_token` where `uid` = "+uid+";";
        List<DeviceToken> list = this.jdbcTemplate.query(sql, new Mapper());
        return list;
    }
    
    public List<DeviceToken> searchByDID(String did)
    {
        String sql = "SELECT `device_token`.`uid`,`device_token`.`device_id` FROM `GSV_DB`.`device_token` where `device_id` = '"+did+"';";
        List<DeviceToken> list = this.jdbcTemplate.query(sql, new Mapper());
        return list;
    }
    
    
    public boolean ifExsit(String did,long uid)
    {
        String sql = "  SELECT count(device_token`.`device_id)` FROM `GSV_DB`.`device_token` where `device_id` = '"+did+"' and `uid` = "+uid+";";
        Integer rows = this.jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println(rows);
        return rows>0;
    }
    
    public void delete(String did,long uid)
    {
        String sql = "DELETE FROM `GSV_DB`.`device_token`\n" + " WHERE `device_id` = '"+did+"' and `uid` = "+uid+";";
        this.jdbcTemplate.execute(sql);
    }
}
