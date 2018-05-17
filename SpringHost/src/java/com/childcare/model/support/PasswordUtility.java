/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import com.childcare.model.JdbcDataDAOImpl;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author New User
 */
public class PasswordUtility {
    @Resource  
   private JdbcTemplate jdbcTemplate;
   private static final String adminPassword = "123456";

    public String selfCheck()
    {
        return "Utility Self Check: jdbc isNull --> "+(this.jdbcTemplate == null)+"";
    }
    
    public static boolean validateAdminPassword(String psw)
    {
        return (adminPassword == null ? psw == null : adminPassword.equals(psw));
    }
    
    /**
     * get password by GID
     * @param GID
     * @return
     * @throws DataAccessException 
     */
        public String getFamilyPasswordByGID(Long GID) throws DataAccessException
    {
        
        try{
            String sql = "SELECT `family`.`FamilyPassword` FROM `GSV_DB`.`family` WHERE `family`.`FID` = (SELECT  `anchorgroup`.`FID` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID`= "+GID+");";
            return this.jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }
        
        public String getFamilyPasswordByFID(int fid) throws DataAccessException
        {
            try{
            String sql = "SELECT `family`.`FamilyPassword` FROM `GSV_DB`.`family` WHERE `family`.`FID` = "+fid+";";
            return this.jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
            
        }
        
     /**
     * validate with password and gid
     * @param password input password
     * @param gid GroupID
     * @return if it matches
     */
    public boolean Validator(String password, long gid)
    {
        String stored = this.getFamilyPasswordByGID(gid);
        return BCrypt.checkpw(password, stored);
    }    
    
    /**
     * 
     * @param password
     * @param fid familyID
     * @return  if it matches
     */
    public boolean Validator(String password, int fid)
    {
        String stored = this.getFamilyPasswordByFID(fid);
        return BCrypt.checkpw(password, stored);
    }    

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
