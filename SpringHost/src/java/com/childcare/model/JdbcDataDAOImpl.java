/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Account;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.entity.PasswdChanger;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author New User
 */
@Repository("jdbcDataDAO")  
public class JdbcDataDAOImpl{  
    @Resource  
    private JdbcTemplate jdbcTemplate;   
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  
    }  
  
    /*---------------------------ACCOUNT------------------------------*/
    /**
     *  Check incoming instance, changing all null field into a blank string (""),
     * UID will not be processed.
     * @param account Incoming account instance
     * @return modified account instance
     */
    private Account accountValidator(Account account)
    {
        if (account.getEmail()==null)
            account.setEmail("");
        if (account.getPassword()==null)
            account.setPassword("");
        if (account.getUserName()==null)
            account.setUserName("");
        if (account.getPhone()==null)
            account.setPhone("");
        return account;
    }
    /*String sql="delete from user where name = ?";  */
    
    /**
     * 
     */
        protected class AccountMapper implements RowMapper,Serializable{  
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
            Account user = new Account();  
            user.setUid(arg0.getLong("UID"));  
            user.setUserName(arg0.getString("UserName"));  
            user.setEmail(arg0.getString("Email"));  
            user.setPassword(arg0.getString("Password"));  
            user.setDiscard(arg0.getInt("discard")!=0);  
            user.setPhone(arg0.getString("Phone"));
            return user;  
        }  
          
    }  
    
    
    /**
     * "Delete" an account, discard field of account deleted will be set as 1.
     * @param uid UID of account to be deleted
     */
    public void deleteAccountByUID(long uid)  throws DataAccessException {  
        // TODO Auto-generated method stub  
        String sql="update account set discard = 1 where UID = ?";  
        Object[] params = {uid};  
        int[] types = {Types.INTEGER}; 
        try
        {
        jdbcTemplate.update(sql,params,types);  
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }  
    
    /**
     * Create a new account with UserName, Password, Email and Phone info in an existing Account instance, 
     * UID will be ignored as Database will assign an auto generated UID for you, and 'discard' field will be set as 0(not discarded).
     * @param iAccount account instance used as data source
     */
    public void createAccount(Account iAccount) throws DataAccessException {  
        // TODO Auto-generated method stub  
        //example ('Enterprise','123','mail','12345','0')
        Account account = this.accountValidator(iAccount);
        String sql = "insert into account (UserName,Password,Email,Phone,discard)"  
                    + "values (?,?,?,?,0);";  
        Object[] params = {account.getUserName(),account.getPassword()+"",  
                account.getEmail(),account.getPhone()};  
        int[] types = {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};
        try
        {
        jdbcTemplate.update(sql, params, types);  
        }
        catch (DataAccessException e)
        {
            throw e;
        }
       // System.out.println("---> Create Account: "+account.getUserName()+"   -----------");
        }  
    
    /**
     * Find UID of a given UserName
     * @param userName String, username you want to look up for.
     * @return 
     */
    public long findUID(String userName) throws DataAccessException
    {
        String sql = "select * from account where UserName = '"+userName+"';";  
        Object[] params = {};  
        int[] types = {};  

        try{
            //try to convert query result to Account, and return UID as a  result
        return ((Account)jdbcTemplate.queryForObject(sql, params, types, new AccountMapper())).getUid();
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
    }
    /**
     * get Account Instance with UID
     * @param uid
     * @return 
     */
    protected Account getAccountInstance(long uid)
    {
        String sql = "select * from account where UID = ?;";  
        Object[] params = {uid};  
        int[] types = {Types.INTEGER}; 
          try{
        return (Account)jdbcTemplate.queryForObject(sql, params, types, new AccountMapper());
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
    }
    
    /**
     * Update email ,phone information through an account instance
     * @param account
     * @throws DataAccessException 
     */
    protected void mergeAccount(Account account) throws DataAccessException
    {
        String sql = "update account set Email = '"+account.getEmail()+"', Phone = '"+account.getPhone()+"' where UID = "+account.getUid()+";"; 
        try{
            this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }
    
    /**
     * Update user password with PasswdChanger instance, containing original Password, new Password and UID information.
     * @param psd
     * @return true if done, false if original password does not match record.
     * @throws DataAccessException if anything with sql goes wrong.
     */
    public boolean updatePassword(PasswdChanger psd) throws DataAccessException
    {
        if (psd.getOrgPasswd().equals(this.getAccountInstance(psd.getUID()).getPassword())) //if old password given by user matches record in DB
        {
            String sql = "update account set Password = '"+psd.getNewPasswd()+"' where UID = "+psd.getUID()+";"; 
             try{
            this.jdbcTemplate.execute(sql);
            return true;
        }
        catch (DataAccessException e)
        {
            throw e;
        }
        } return false;
    }
    /*
    ==================Family===============
    */
    private Family familyNormolizer(Family fa)
    {
        fa.setFamilyPassword(BCrypt.hashpw(fa.getFamilyPassword(), BCrypt.gensalt()));
        System.out.println("Bcrypted Password:"+fa.getFamilyPassword());
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
            return family;  
        }  
          
    }  

 /**
  * Create a Family Record. Password is hashed 
  * @param fa
  * @throws DataAccessException 
  */
     public void createFamily(Family fa) throws DataAccessException
     {
         Family family = this.familyNormolizer(fa);
         String sql = "INSERT INTO `GSV_DB`.`family` (`fid`, `familyName`, `FamilyPassword`)" +
                        "VALUES" +
                        "(?,?,?);";
          Object[] params = {family.getFid(),family.getFamilyName(),family.getFamilyPassword()};  
        int[] types = {Types.INTEGER,Types.VARCHAR,Types.VARCHAR};
        try
        {
        jdbcTemplate.update(sql, params, types);  
        }
        catch (DataAccessException e)
        {
            throw e;
        }
         
     }
     
     protected Family getFamilyInstance(int fid)
    {
        String sql = "select * from `GSV_DB`.`family` where `fid` = ?;";  
        Object[] params = {fid};  
        int[] types = {Types.INTEGER}; 
          try{
        return (Family)jdbcTemplate.queryForObject(sql, params, types, new FamilyMapper());
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
    }
    
    
    /*
    ================================================Device=====================
   
    */
    
    private Device deviceNormolizer(Device inDevice) throws FamilyNullException
    {
        if (inDevice.getDeviceID()==null)
            inDevice.setDeviceID("");
        if (inDevice.getFid()==null||inDevice.getFid().getFid()==null)
        {
            throw new FamilyNullException("Family is null");
        }
        if (inDevice.getPulse()==null)
            inDevice.setPulse(0);
        return inDevice;
    }
    
      protected class DeviceMapper implements RowMapper,Serializable{  
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
            System.out.println("----Device Mapper Start--------");
            Device device = new Device();  
            device.setDeviceID(arg0.getString("DeviceID"));  
            Family f = new Family();
            f.setFid(arg0.getInt("FID"));
            device.setFid(f);  
            device.setLatitude(arg0.getBigDecimal("Latitude"));  
            device.setLongitude(arg0.getBigDecimal("Longitude"));  
            device.setPulse(arg0.getInt("pulse")>=0?arg0.getInt("pulse"):0); 
            return device;  
        }  
          
    }  
    
    /**
     * Create a new device with given PK
     * @param iDevice input device instance
     * @throws DataAccessException when having issue accessing DB
     * @throws FamilyNullException when FID is null
     */    
    public void createDevice(Device iDevice) throws DataAccessException,FamilyNullException {  
        // TODO Auto-generated method stub  
        //example ('Enterprise','123','mail','12345','0')
         Device device = iDevice;
       try{
       device = this.deviceNormolizer(iDevice);}
       catch (FamilyNullException e){
           throw e;
       }
        String sql =  "INSERT INTO `GSV_DB`.`Device` (`DeviceID`,`FID`,`Longitude`," +
                        "`Latitude`," +
                        "`pulse`)" +
                        "VALUES" +
                        "('"+device.getDeviceID()+"'," +
                        device.getFid().getFid()+"," +
                        device.getLongitude()+"," +
                        device.getLatitude()+"," +
                        device.getPulse()+");";
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
     * get Device Instance with Device ID
     * @param DID
     * @return 
     */
    protected Device getDeviceInstance(String DID)
    {
        String sql = "select * from `GSV_DB`.`Device` where `DeviceID` = ?;";  
        Object[] params = {DID};  
        int[] types = {Types.VARCHAR}; 
          try{
        return (Device)jdbcTemplate.queryForObject(sql, params, types, new DeviceMapper());
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
    }
    
      public void updateDevice(Device iDevice) throws DataAccessException,FamilyNullException
      {         
          Device device = iDevice;
       try{
       device = this.deviceNormolizer(iDevice);}
       catch (FamilyNullException e){
           throw e;
       }
       
               String sql = "UPDATE `GSV_DB`.`Device`" +
                    "SET" +
                    "`DeviceID` = '" +device.getDeviceID()+"',"+
                    "`FID` = " +device.getFid().getFid()+","+
                    "`Longitude` = " +device.getLongitude()+","+
                    "`Latitude` = " +device.getLatitude()+","+
                    "`pulse` = " +device.getPulse()+
                    " WHERE `DeviceID` = '"+device.getDeviceID()+"';";
               
                 try
        {
         this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
         sql= "INSERT INTO `GSV_DB`.`DeviceAudit`" +
                "(`DeviceID`," +
                "`Longitude`," +
                "`Latitude`) " +
                "VALUES ( " +
                "'"+device.getDeviceID()+"', " +
                device.getLongitude()+","+
               device.getLatitude()+ ");";
               try
        {
         this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
          
      }
      
      public List<DeviceAudit> getAudit(String DID)
      {
        String sql = "select * from `GSV_DB`.`DeviceAudit` where `DeviceID` = ?;";  
        Object[] params = {DID};  
        int[] types = {Types.VARCHAR}; 
          try{
        return (List<DeviceAudit>)jdbcTemplate.query(sql, params, types, new DeviceAuditMapper());
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
      }
      
      /* ---------------------- Device Audit ------------------------------*/
      protected class DeviceAuditMapper implements RowMapper,Serializable{  
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
            DeviceAudit da = new DeviceAudit();  
            Device device = new Device();
            device.setDeviceID(arg0.getString("DeviceID"));
            da.setDeviceID(device);  
            da.setLatitude(arg0.getBigDecimal("Latitude"));  
            da.setLongitude(arg0.getBigDecimal("Longitude"));  
            da.setAuditSeq(arg0.getInt("AuditSeq"));
            da.setDate(arg0.getTimestamp("Date"));
            return da;  
        }  
          
    }  
      
      
      
      /*=====================Anchor Group===============*/
      
      
    protected class AnchorGroupMapper implements RowMapper,Serializable{  
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
            Anchorgroup group = new Anchorgroup();  
            group.setGid(arg0.getLong("GID"));  
            group.setGroupName(arg0.getString("GroupName"));
            Family f = new Family();
            f.setFid(arg0.getInt("FID"));
            group.setFid(f);  
            group.setInLat(arg0.getBigDecimal("inLat"));  
            group.setInLong(arg0.getBigDecimal("inLong")); 
            group.setInRadius(arg0.getBigDecimal("inRadius"));
            group.setExLat(arg0.getBigDecimal("exLat"));  
            group.setExLong(arg0.getBigDecimal("exLong")); 
            group.setInRadius(arg0.getBigDecimal("exRadius"));
            group.setType(arg0.getInt("Type"));
            return group;  
        }  
          
    } 

    /**
     * update/insert new instance
     * @param group Group instance to be updated
     * @throws DataAccessException
     */
    public void updateGroup(Anchorgroup group) throws DataAccessException
      {         
 
        String sql = "UPDATE `GSV_DB`.`anchorgroup`\n" +
                     "SET\n" +
                     "`GID` = "+group.getGid()+",\n" +
                     "`GroupName` = '"+group.getGroupName()+"',\n" +
                     "`FID` = "+group.getFid()+",\n" +
                     "`inLat` = "+group.getInLat()+",\n" +
                     "`inLong` = "+group.getInLong()+",\n" +
                     "`inRadius` = "+group.getInRadius()+",\n" +
                     "`exLat` = "+group.getExLat()+",\n" +
                     "`exLong` = "+group.getExLong()+",\n" +
                     "`exRadius` = "+group.getExRadius()+",\n" +
                     "`Type` = "+group.getType()+"\n" +
                     "WHERE `GID` = "+group.getGid()+";";
               
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
     * Select statement to get a group instance using GID
     * @param GID Group ID
     * @return 
     * @throws DataAccessException 
     */
    protected Anchorgroup getGroupInstance(Long GID) throws DataAccessException
    {
        String sql = "select * from `GSV_DB`.`anchorgroup` where `GID` = ?;";  
        Object[] params = {GID};  
        int[] types = {Types.VARCHAR}; 
          try{
        return (Anchorgroup)jdbcTemplate.queryForObject(sql, params, types, new AnchorGroupMapper());
        }
        catch (DataAccessException e)
         {
              throw e;     
          }
    }
    
    /**
     * Delete statement
     * @param GID Group ID of the group to be deleted
     * @throws DataAccessException 
     */
    protected void deleteGroup(Long GID) throws DataAccessException
    {
        String sql = "DELETE FROM `GSV_DB`.`anchorgroup`\n" +
                     "WHERE `GID` = "+GID+";";
        try
        {
         this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }
    
}  