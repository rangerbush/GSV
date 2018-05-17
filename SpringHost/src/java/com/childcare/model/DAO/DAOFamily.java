/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Account;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.AccountFamilyPK;
import com.childcare.entity.Family;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author New User
 */
public class DAOFamily {
    @Resource 
    private JdbcTemplate jdbcTemplate;
    

    /**
     * Hash input password
     * @param fa
     * @return 
     */
    private Family familyNormolizer(Family fa)
    {
        System.out.println("Raw Password:"+fa.getFamilyPassword());
        fa.setFamilyPassword(BCrypt.hashpw(fa.getFamilyPassword(), BCrypt.gensalt()));
        System.out.println("Bcrypted Password:"+fa.getFamilyPassword());
        if (fa.getMaxCluster()<1)
            fa.setMaxCluster(1);
        return fa;
        /*
        if (BCrypt.checkpw(candidate, hashed))
	System.out.println("It matches");
        else
	System.out.println("It does not match");
        */
    }
    
      protected class FamilyMapper implements RowMapper,Serializable{  
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
            Family family = new Family();  
            family.setFid(arg0.getInt("fid"));  
            family.setFamilyName(arg0.getString("familyName"));  
            family.setFamilyPassword(arg0.getString("FamilyPassword"));  
            family.setMaxCluster(arg0.getInt("MaxCluster"));
            return family;  
        }  
          
    }  
      
    protected class FandAMapper implements RowMapper,Serializable{  
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
            AccountFamily instance = new AccountFamily(); 
            AccountFamilyPK pk = new AccountFamilyPK();
            pk.setFid(arg0.getInt("FID"));
            pk.setUid(arg0.getInt("UID"));
            instance.setAccountFamilyPK(pk);  
            instance.setStatus(arg0.getInt("status"));
            instance.setFamily(new Family(arg0.getInt("FID")));
            instance.setAccount(new Account(arg0.getLong("UID")));
            return instance;  
        }  
          
    } 
    
   /**
    * find owner's uid of a family
    * @param fid
    * @return 
    */
    public Integer findOwner(int fid)
    {
        String sql = "select `UID` from `GSV_DB`.`Account_Family` where `FID` = "+fid+" and `status` = 0;";  
        return jdbcTemplate.queryForInt(sql);
    }
    
    
    public List<AccountFamily> getMembers(int fid)
    {
        String sql = "select * from `GSV_DB`.`Account_Family` where `FID` = "+fid+";";
         List<AccountFamily> list = this.jdbcTemplate.query(sql,new FandAMapper());
         return list;
    }
    
    public List<Family> findMyFamilies(long uid)
    {
        String sql = "select * from `GSV_DB`.`family` where `FID` IN (select `FID` from `GSV_DB`.`Account_Family` where `UID` = "+uid+");";
         List<Family> list = this.jdbcTemplate.query(sql,new FamilyMapper());
         return list;
    }
    
    public List<Family> findOwnedFamilies(long uid)
    {
        String sql = "select * from `GSV_DB`.`family` where `FID` IN (select `FID` from `GSV_DB`.`Account_Family` where `UID` = "+uid+" and `status` = 0) ;";
         List<Family> list = this.jdbcTemplate.query(sql,new FamilyMapper());
         return list;
    }
    
    public Family findFamilyByGID(int gid)
    {
        String sql = "select * from `GSV_DB`.`family` where `FID` = (select `FID` from `GSV_DB`.`anchorgroup` where `GID` = "+gid+") ;";
         Family family = (Family)this.jdbcTemplate.queryForObject(sql, new FamilyMapper());
         return family;
    }
    
    public Family findFamilyByCID(int cid)
    {
        String sql = "SELECT * FROM `GSV_DB`.`family` WHERE `family`.`fid` = (select `FID` from `Child` where `CID` = "+cid+")  ;";
        return (Family)this.jdbcTemplate.queryForObject(sql, new FamilyMapper());
    }
    
    public void addRelationship(int fid,long uid,int status)
    {
        String sql = "INSERT INTO `GSV_DB`.`Account_Family`\n" +
                    "(`FID`,`UID`,`status`)\n" +
                    "VALUES\n" +
                    "("+fid+","+uid+","+status+");";
        this.jdbcTemplate.execute(sql);
    }
    
    public void updateRelationship(int fid,long uid,int status)
    {
        String sql = "UPDATE `GSV_DB`.`Account_Family` SET `status` = '"+status+"' WHERE `FID` = "+fid+" AND `UID` ="+uid+";";
        this.jdbcTemplate.execute(sql);
    }
    
    @Deprecated
    public int findMyFamily(long uid)
    {
        String sql = "select * from `GSV_DB`.`Account_Family` where `UID` = "+uid+";";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

      /**
       * 
       * @param fa
       * @return
       * @throws DataAccessException 
       */
     public int createFamily(Family fa) throws DataAccessException
     {
         Family family = this.familyNormolizer(fa);
         String sql = "INSERT INTO `GSV_DB`.`family` (`familyName`, `FamilyPassword`,`MaxCluster`)" +
                        "VALUES" +
                        "('"+family.getFamilyName()+"','"+family.getFamilyPassword()+"',"+family.getMaxCluster()+");";
                        //"(?,?);\n";              
        KeyHolder keyHolder = new GeneratedKeyHolder();             
        try
        {
            int updatecount/*number of effected rows*/ = this.jdbcTemplate.update(new PreparedStatementCreator() {  
            @Override  
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
            return ps;  }},keyHolder);
            int id = keyHolder.getKey().intValue(); 
            return id;
            //http://fancyboy2050.iteye.com/blog/1455559
            //在JDBC 3.0规范中，当新增记录时，允许将数据库自动产生的主键值绑定到Statement或PreparedStatement中
            //当autoGeneratedKeys参数设置为Statement.RETURN_GENERATED_KEYS值时即可绑定数据库产生的主键值，设置为Statement.NO_GENERATED_KEYS时，不绑定主键值。
        }
        catch (DataAccessException e)   
        {
            throw e;
        }
         
     }
     
    public Family getFamilyInstance(int fid)
    {
        String sql = "select * from `GSV_DB`.`family` where `fid` = ?;";  
        Object[] params = {fid};  
        int[] types = {Types.INTEGER}; 
        return (Family)jdbcTemplate.queryForObject(sql, params, types, new FamilyMapper());  
    }
     
    /**
     * Allow to update family name with given FID and correct password
     * @param family
     * @throws Exception 
     */
     public void updateFamily(Family family) throws Exception
      {         
        if (!BCrypt.checkpw(family.getFamilyPassword(),this.getFamilyInstance(family.getFid()).getFamilyPassword()))   
                throw new Exception("Input password does not match the record.");  //if failed password check, throw an exception
        Family normF = this.familyNormolizer(family); //passwd is now hashed
        String sql = "UPDATE `GSV_DB`.`family`\n" +
                     "SET\n" +
                     "`familyName` = '"+normF.getFamilyName()+"',\n" +
                     "`MaxCluster` = "+normF.getMaxCluster()+"\n"+
                     "WHERE `fid` = "+normF.getFid()+";\n";
              // System.out.println(sql);
              /*
              UPDATE `GSV_DB`.`family`
                SET
                `familyName` = case `FamilyPassword` when 'passwd' then 'testName' end,
                `FamilyPassword` = case `FamilyPassword` when 'passwd' then 'newPasswd' end
                WHERE `fid` = 11111113;
              */
        try
        {
           this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
          
      }
     
     /**
      * Call this method to change password only
      * @param family validation data, including fid and the password contained for validation
      * @param passwd new password
      * @throws Exception 
      */
     public void changePassword(Family family,String passwd) throws Exception
     {
        if (!BCrypt.checkpw(family.getFamilyPassword(),this.getFamilyInstance(family.getFid()).getFamilyPassword()))   
                throw new Exception("Input password does not match the record.");  //if failed password check, throw an exception
        String sql = "UPDATE `GSV_DB`.`family`\n" +
                     "SET\n" +
                     "`FamilyPassword` = '"+BCrypt.hashpw(passwd,BCrypt.gensalt())+"'\n" +
                     "WHERE `family`.`fid` = "+family.getFid()+";\n";
        System.out.println(sql);
        try{
        this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }  
     }
     
    /**
     * CHECK password before calling this method!
     * @param fid
     * @throws DataAccessException 
     */ 
    public void deleteFamily(int fid) throws DataAccessException
    {
        String sql = "DELETE FROM `GSV_DB`.`family`\n" +
                     "WHERE `family`.`fid` = "+fid+";";
        this.jdbcTemplate.execute(sql);
    }
    
}
