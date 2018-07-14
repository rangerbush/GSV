/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Account;
import com.childcare.entity.Child;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
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
public class DAOChild {
      @Resource 
    private JdbcTemplate jdbcTemplate;
    public static final int DEFAULT = 0;
    public static final int URL = 1;
      
         protected class ChildMapper implements RowMapper,Serializable{  
            /**
             * 
             * @param arg0 the ResultSet to map (pre-initialized for the current row)
             * @param arg1 the number of the current row
             * @return
             * @throws SQLException 
             */
        @Override  
        public Child mapRow(ResultSet arg0, int arg1) throws SQLException {  
            // TODO Auto-generated method stub  
            Child child = new Child();
            child.setAge(arg0.getInt("Age"));  
            child.setCid(arg0.getInt("CID"));
            child.setName(arg0.getString("Name"));
            child.setImage(arg0.getString("Image"));
            child.setDeviceID(new Device(arg0.getString("DeviceID")));
            child.setFid(new Family(arg0.getInt("FID")));
            child.setStatus(arg0.getInt("status"));
            child.setCreator(new Account(arg0.getLong("Creator")));
            return child;  
        }  
    }
         
          protected class WithURLMapper implements RowMapper,Serializable{  
            /**
             * 
             * @param arg0 the ResultSet to map (pre-initialized for the current row)
             * @param arg1 the number of the current row
             * @return
             * @throws SQLException 
             */
        @Override  
        public Child mapRow(ResultSet arg0, int arg1) throws SQLException {  
            // TODO Auto-generated method stub  
            Child child = new Child();
            child.setAge(arg0.getInt("Age"));  
            child.setCid(arg0.getInt("CID"));
            child.setName(arg0.getString("Name"));
            child.setImage("safetyvillage.top:8080/SpringHost/upload/"+arg0.getString("Image"));
            child.setDeviceID(new Device(arg0.getString("DeviceID")));
            child.setFid(new Family(arg0.getInt("FID")));
            child.setStatus(arg0.getInt("status"));
            child.setCreator(new Account(arg0.getLong("Creator")));
            return child;  
        }  
    }
         
    public void editImage(int cid,String image)
    {
        String sql = "UPDATE `GSV_DB`.`Child`\n" +
                     " SET\n" +
                     " `Image` = '"+image+"' \n" +
                     " WHERE `CID` = "+cid+";";
        this.jdbcTemplate.execute(sql);
    }
         
    /**
     * create a new child record
     * @param child
     * @param creator
     * @return CID of created record
     */
    public int create(Child child,long creator)
    {
        String did = "null";
        if (child.getDeviceID()!=null)
            did = "'"+child.getDeviceID().getDeviceID()+"'";
        String sql = "INSERT INTO `GSV_DB`.`Child`\n" +
                     "("+
                     "`FID`,\n" +
                     "`DeviceID`,\n" +
                     "`Name`,\n" +
                     "`Image`,\n" +
                     "`Creator`,\n"+
                     "`status`,\n"+
                     "`Age`) \n" +
                     "VALUES \n" +
                     "("+
                     child.getFid().getFid()+",\n" +
                     did+",\n" +
                     "'"+child.getName().replaceAll("\'", "\\\\'")+"',\n" +
                     "'"+child.getImage()+"',\n" +
                     creator+","+
                     child.getStatus()+","+
                     child.getAge()+");";
          KeyHolder keyHolder = new GeneratedKeyHolder();             
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
    
    public void update(Child child)
    {
                String did = "null";
        if (child.getDeviceID()!=null)
            did = "'"+child.getDeviceID().getDeviceID()+"'";
        String sql = "UPDATE `GSV_DB`.`Child`\n" +
                     " SET\n" +
                     " `FID` = "+child.getFid().getFid()+",\n" +
                     " `DeviceID` = "+did.replaceAll("\'", "\\\\'")+",\n" +
                     " `Name` = '"+child.getName().replaceAll("\'", "\\\\'")+"',\n" +
                     " `Age` = "+child.getAge()+",\n" +
                     "`status` = "+child.getStatus()+
                     " WHERE `CID` = "+child.getCid()+";";
        this.jdbcTemplate.execute(sql);
    }
    
    public Child findByCID(int cid)
    {
        return this.findByCID(cid, DEFAULT);
    }
    
    public Child findByCID(int cid, int mode)
    {
        String sql = "select * FROM `GSV_DB`.`Child` WHERE `CID` = "+cid+";";
        switch(mode)
        {
            case URL:
            return (Child)this.jdbcTemplate.queryForObject(sql,new WithURLMapper());
            default:
            return (Child)this.jdbcTemplate.queryForObject(sql,new ChildMapper());    
        }
    }

    
    public Child findByDevice(String deviceID)
    {  
        return this.findByDevice(deviceID, DEFAULT);
    }
    
    public Child findByDevice(String deviceID,int mode)
    {  
        String sql = "select * FROM `GSV_DB`.`Child` WHERE `DeviceID` = '"+deviceID.replaceAll("\'", "\\\\'")+"';";
        List<Child> list;
        switch(mode)
        {
            case URL:
        list = this.jdbcTemplate.query(sql,new WithURLMapper());
        break;
            default:
        list = this.jdbcTemplate.query(sql,new ChildMapper());
        }
        if (list.isEmpty())
            return null;
        else return list.get(0);
    }
    
    public List<Child> findByFID(int fid,int mode)
    {
        String sql = "SELECT * FROM `GSV_DB`.`Child` WHERE `Child`.`FID` = "+fid+"  ;";
        List<Child> list;
        switch(mode)
        {
            case URL:
        list = this.jdbcTemplate.query(sql,new WithURLMapper());
        break;
            default:
        list = this.jdbcTemplate.query(sql,new ChildMapper());
        }
        return list;
    }
    
    public List<Child> findByFID(int fid)
    {
        return this.findByFID(fid, DEFAULT);
    }
    
    /**
     * 根据一系列fid查找所有下属的Child
     * @param fidList
     * @return 
     */
    public List<Child> findByMultiFID(List<Integer> fidList)
    {
        if (fidList.isEmpty())
            throw new DataAccessException("Input list of families is empty."){};
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = fidList.iterator();
        while (it.hasNext())
        {
            sb.append(",").append(it.next());
        }
        sb.deleteCharAt(0);
        String sql = "select * from `GSV_DB`.`Child` where Child.FID in ("+sb.toString()+");";
        return this.jdbcTemplate.query(sql,new ChildMapper());
    }
    
    /**
     * 0-normal 1-URL
     * @param uid
     * @param mode
     * @return 
     */
    public List<Child> findByUID(long uid,int mode)
    {
        String sql = "Select * from `GSV_DB`.`Child` where `Child`.`FID` in (select `FID` from `Account_Family` where `UID` = "+uid+")";
        List<Child> list;
        switch(mode)
        {
            case URL:
        list = this.jdbcTemplate.query(sql,new WithURLMapper());
        break;
            default:
        list = this.jdbcTemplate.query(sql,new ChildMapper());
        }
        return list;
    }
    
    public List<Child> findByUID(long uid)
    {
        return this.findByUID(uid, DEFAULT);
    }
    
    public void delete(int cid)
    {
        String sql = "DELETE FROM `GSV_DB`.`Child` WHERE `CID` = "+cid+";";
        this.jdbcTemplate.execute(sql);
    }
}
