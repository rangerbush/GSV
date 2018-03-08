/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.model.FamilyNullException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author New User
 */
public class DAODevice {
    @Resource  
   private JdbcTemplate jdbcTemplate;
    

    
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
    public Device getDeviceInstance(String DID)
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
    
    public void deleteDevice(String did) throws DataAccessException
    {
        String sql = "DELETE FROM `GSV_DB`.`Device`\n" +
                    "WHERE `DeviceID` = '"+did+"';";
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
     * check if fid in given DID matches the fid provided in input
     * @param did
     * @param fid
     * @return true if matches
     * @throws DataAccessException 
     */
    public boolean checkDIDandFID(String did,int fid) throws DataAccessException
    {
        String sql = "select `Device`.`FID` from FROM `GSV_DB`.`Device` where `Device`.`DeviceID` = '"+did+"'; ";
        int familyID = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return familyID == fid;
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
                    //"`DeviceID` = '" +device.getDeviceID()+"',"+
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
      
      /**
       * Enquiry audit information of particular Device ID
       * @param DID
       * @return 
       */
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

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
      
      
    
}
