/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Child;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.model.exception.NullException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
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

    
      private Device deviceNormolizer(Device inDevice) throws NullException
    {
        if (inDevice.getDeviceID()==null)
            throw new NullException("DeviceID is null");
        if (inDevice.getFid()==null||inDevice.getFid().getFid()==null)
        {
            throw new NullException("Family is null");
        }
        if (inDevice.getPulse()==null)
            inDevice.setPulse(0);
        if (inDevice.getLatitude()==null)
            inDevice.setLatitude(BigDecimal.ZERO);
        if (inDevice.getLongitude()==null)
            inDevice.setLongitude(BigDecimal.ZERO);
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
            Device device = new Device();  
            device.setDeviceID(arg0.getString("DeviceID"));  
            Family f = new Family();
            f.setFid(arg0.getInt("FID"));
            device.setFid(f);  
            device.setLatitude(arg0.getBigDecimal("Latitude"));  
            device.setLongitude(arg0.getBigDecimal("Longitude"));  
            device.setPulse(arg0.getInt("pulse")>=0?arg0.getInt("pulse"):0); 
            device.setStatus(arg0.getInt("Status"));
            device.setTimeStamp(arg0.getDate("TimeStamp"));
            device.setChild(new Child(arg0.getInt("CID")));
            return device;  
        }  
          
    }  
      
      public static String cookForSQL(String text)
      {
          return text.replaceAll("\'", "\\\\'");  
      }
    
    /**
     * Create a new device with given PK
     * @param iDevice input device instance
     * @throws DataAccessException when having issue accessing DB
     * @throws com.childcare.model.exception.NullException
     */    
    public void createDevice(Device iDevice) throws DataAccessException,NullException {  
        // TODO Auto-generated method stub  
        //example ('Enterprise','123','mail','12345','0')
         Device device = iDevice;
       try{
       device = this.deviceNormolizer(iDevice);}
       catch (NullException e){
           throw e;
       }
       System.out.println(cookForSQL(device.getDeviceID()));
        String sql =  "INSERT INTO `GSV_DB`.`Device` (`DeviceID`,`FID`,`Longitude`," +
                        "`Latitude`," +
                        "`pulse`)" +
                        "VALUES" +
                        "('"+cookForSQL(device.getDeviceID())+"'," +
                        device.getFid().getFid()+"," +
                        device.getLongitude()+"," +
                        device.getLatitude()+"," +
                        device.getPulse()+");";
         this.jdbcTemplate.execute(sql);
        }  
    
    public List<Device> findByUID(long uid)
    {
        String sql = "select * from Device where FID in (select FID from Account_Family where UID = "+uid+");";
         return this.jdbcTemplate.query(sql, new DeviceMapper());
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
        return (Device)jdbcTemplate.queryForObject(sql, params, types, new DeviceMapper());
    }
    
    public void deleteDevice(String did) throws DataAccessException
    {
        String sql = "DELETE FROM `GSV_DB`.`Device`\n" +
                    "WHERE `DeviceID` = '"+cookForSQL(did)+"';";
            this.jdbcTemplate.execute(sql);
    }
    
    public List<Device> findByFID(int fid)
    {
        String sql = "select * from Device where FID ="+fid+";";
        return this.jdbcTemplate.query(sql,new DeviceMapper());
    }
    
    public List<Device> findMulti(List<String> dList)
    {
        if (dList.isEmpty())
            throw new DataAccessException("Input list is empty."){};
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = dList.iterator();
        while(it.hasNext())
        {
            sb.append(",").append("'").append(cookForSQL(it.next())).append("'");
        }
        sb.deleteCharAt(0);
        String sql = "select * from Device where DeviceID IN ("+sb.toString()+")";
        return this.jdbcTemplate.query(sql, new DeviceMapper());
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
        String sql = "select `Device`.`FID` from FROM `GSV_DB`.`Device` where `Device`.`DeviceID` = '"+cookForSQL(did)+"'; ";
        int familyID = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return familyID == fid;
    }
    
    /**
     * Simply refresh timestamp of device to avoid being missing
     * @param device
     * @throws DataAccessException 
     */
    public void touch(Device device) throws DataAccessException
    {
        //pulse, long, lat, timestamp
           String sql = "UPDATE `GSV_DB`.`Device`" +
                    "SET" +
                    "`TimeStamp` = now()"+","+
                   "`pulse` = "+device.getPulse()+","+
                   "`Longitude` = " +device.getLongitude()+","+
                    "`Latitude` = " +device.getLatitude()+
                    " WHERE `DeviceID` = '"+cookForSQL(device.getDeviceID())+"';";
         this.jdbcTemplate.execute(sql);
    }
    
      public void updateDevice(Device device) throws DataAccessException,NullException
      {         
       try{
       device = this.deviceNormolizer(device);}
       catch (NullException e){
           throw e;
       }
       
               String sql = "UPDATE `GSV_DB`.`Device`" +
                    "SET" +
                    //"`DeviceID` = '" +device.getDeviceID()+"',"+
                    "`FID` = " +device.getFid().getFid()+","+
                    "`Longitude` = " +device.getLongitude()+","+
                    "`Latitude` = " +device.getLatitude()+","+
                    "`pulse` = " +device.getPulse()+","+
                    "`Status` = "+device.getStatus()+","+
                    "`TimeStamp` = now()"+
                    " WHERE `DeviceID` = '"+cookForSQL(device.getDeviceID())+"';";
         this.jdbcTemplate.execute(sql);
    /*     sql= "INSERT INTO `GSV_DB`.`DeviceAudit`" +
                "(`DeviceID`," +
                "`Longitude`," +
                "`Latitude`) " +
                "VALUES ( " +
                "'"+device.getDeviceID()+"', " +
                device.getLongitude()+","+
               device.getLatitude()+ ");";
         this.jdbcTemplate.execute(sql);      
               */
      }
      
      public List<Device> getMissing() throws DataAccessException
      {
        String sql = "SELECT * "+
                "FROM `GSV_DB`.`Device` WHERE `Status` = 2;";
          List<Device> list = this.jdbcTemplate.query(sql,new DeviceMapper());  /*- list of missing devices -*/
          return list;     
      }
      
      /**
       * 
       * @param toI Inform client for how many times
       * @param allowance_value like 5
       * @param allowance_unit line _minute  (_ stands for space bar)
       * @throws DataAccessException 
       */
      public void missing2Deactivated(int toI,int allowance_value, String allowance_unit) throws DataAccessException
      {
          /*-- Set all missing to deactivated after  --*/
          String sql = "UPDATE `GSV_DB`.`Device`\n" +
                       "SET\n" +
                       "`Status` = 0\n" +
                       "WHERE `Status` = 2 and `TimeStamp`<date_sub(now(),interval "+allowance_value*toI+allowance_unit+");";
          this.jdbcTemplate.execute(sql);
      }
      
      public void registerMissing(int allowance_value, String allowance_unit) throws DataAccessException
      {
          /*-- Set all devices in Status 1(Tracking) who have gone slient for more than a given length of time to Status 2 --*/
          String sql = "UPDATE `GSV_DB`.`Device`\n" +
                       "SET\n" +
                       "`Status` = 2\n" +
                       "WHERE `Status` = 1 and `TimeStamp`<date_sub(now(),interval "+allowance_value+allowance_unit+");";
          this.jdbcTemplate.execute(sql);
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
