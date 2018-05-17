/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.childcare.entity.RefreshToken;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.annotation.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author New User
 */
public class DAORefreshToken {
    
    @Resource 
    private JdbcTemplate jdbcTemplate;
    
      protected class TokenMapper implements RowMapper,Serializable{  
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
            RefreshToken token = new RefreshToken();  
            token.setJti(arg0.getString("JTI"));  
            token.setTimestamp(arg0.getDate("TIMESTAMP"));
            return token;  
        }  
    }  
      
    public void replace(String oJti,String nJti)
    {
        String []sql = new String[2];
        sql[0] =  "INSERT INTO `GSV_DB`.`RefreshToken`\n" +
                     "(`JTI`) \n" +
                     "VALUES \n" +
                     "('"+nJti+"');";
        sql[1] =  "DELETE FROM `GSV_DB`.`RefreshToken`\n" +
                     " WHERE `JTI` = '"+oJti+"';";
        this.jdbcTemplate.batchUpdate(sql);
    }
      
    public void create(String jti)
    {
        String sql = "INSERT INTO `GSV_DB`.`RefreshToken`\n" +
                     "(`JTI`) \n" +
                     "VALUES \n" +
                     "('"+jti+"');";
        this.jdbcTemplate.execute(sql);
    }
    
    public void delete(String token)
    {
        String sql = "DELETE FROM `GSV_DB`.`RefreshToken`\n" +
                     " WHERE `JTI` = '"+token+"';";
        this.jdbcTemplate.execute(sql);
    }
    
    public void touch(String token)
    {
        String sql = "UPDATE `GSV_DB`.`RefreshToken`\n" +
                     " SET \n" +
                     " `TIMESTAMP` = now() \n" +
                     " WHERE `JTI` = '"+token+"';";
        this.jdbcTemplate.execute(sql);
    }
    
    /**
     * 
     * @param token
     * @return RefreshToken, or null if specified token not found.
     */
    public RefreshToken find(String token)
    {
        try
        {
         String sql = "select * from `GSV_DB`.`RefreshToken` where `JTI` = ?;";  
        Object[] params = {token};  
        int[] types = {Types.VARCHAR}; 
        Object obj = jdbcTemplate.queryForObject(sql, params, types, new TokenMapper());
        if (obj==null)
            return null;
        else return (RefreshToken)obj;
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
}
