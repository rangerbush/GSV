/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class DAOAnchorGroup {
    @Resource 
     private  JdbcTemplate jdbcTemplate;
    
    private Anchorgroup normalizer(Anchorgroup group)
    {
        if (group.getInLat()==null)
            group.setInLat(BigDecimal.ZERO);
        if (group.getInLong()==null)
            group.setInLong(BigDecimal.ZERO);
        if (group.getInRadius()==null)
            group.setInRadius(BigDecimal.ZERO);
        if (group.getExLat()==null)
            group.setExLat(BigDecimal.ZERO);
        if (group.getExLong()==null)
            group.setExLong(BigDecimal.ZERO);
        if (group.getExRadius()==null)
            group.setExRadius(BigDecimal.ZERO);
        return group;
    }
    
     
    protected class AnchorGroupMapper implements RowMapper,Serializable{  
            /**
             * Cluster is a mini group between Group and family, a family have many clusters, and a group belongs to a cluster
             * A cluster is a collection of several groups inside a family that can be applied for one child, especially when there are many children in this family.
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
            group.setExRadius(arg0.getBigDecimal("exRadius"));
            group.setType(arg0.getInt("Type"));
            group.setMaxSeq(arg0.getInt("MaxSeq"));
            group.setCluster(arg0.getInt("Cluster"));
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
                    // "`GID` = "+group.getGid()+",\n" +
                     "`GroupName` = '"+group.getGroupName()+"',\n" +
           //          "`FID` = "+group.getFid().getFid()+",\n" +
                     "`inLat` = "+group.getInLat()+",\n" +
                     "`inLong` = "+group.getInLong()+",\n" +
                     "`inRadius` = "+group.getInRadius()+",\n" +
                     "`exLat` = "+group.getExLat()+",\n" +
                     "`exLong` = "+group.getExLong()+",\n" +
                     "`exRadius` = "+group.getExRadius()+",\n" +
                     "`Type` = "+group.getType()+",\n" +
                     "`Cluster` ="+group.getCluster()+"\n"+
                     "WHERE `GID` = "+group.getGid()+";";
         this.jdbcTemplate.execute(sql);       
      }
    
    public long createGroup(Anchorgroup group)
    {
        group = this.normalizer(group);
        String sql = "INSERT INTO `GSV_DB`.`anchorgroup`\n" +
                    // "(`GID`,\n" +
                     "(`GroupName`,\n" +
                     "`FID`,\n" +
                     "`inLat`,\n" +
                     "`inLong`,\n" +
                     "`inRadius`,\n" +
                     "`exLat`,\n" +
                     "`exLong`,\n" +
                     "`exRadius`,\n" +
                     "`MaxSeq`,\n"+
                     "`Type`,\n" +
                     "`Cluster`) \n"+
                     "VALUES \n" +
                "("+
                 //    "(<{GID: }>,\n" +
                     "'"+group.getGroupName()+"',\n " +
                     group.getFid().getFid()+",\n " +
                     group.getInLat()+",\n " +
                     group.getInLong()+",\n " +
                     group.getInRadius()+",\n " +
                     group.getExLat()+",\n " +
                     group.getExLong()+",\n " +
                     group.getExRadius()+",\n " +
                     group.getMaxSeq()+",\n " +
                     group.getType()+",\n " +
                     group.getCluster()+ ");";
          System.out.println(sql);
         return this.run(sql);

    }
            
    /**
     * Select statement to get a group instance using GID
     * @param GID Group ID
     * @return 
     * @throws DataAccessException 
     */
    public Anchorgroup getGroupInstance(Long GID) throws DataAccessException
    {
        String sql = "select * from `GSV_DB`.`anchorgroup` where `GID` = ?;";  
        Object[] params = {GID};  
        int[] types = {Types.VARCHAR}; 
        return (Anchorgroup)jdbcTemplate.queryForObject(sql, params, types, new AnchorGroupMapper());     
    }
    
    public List<Anchorgroup> searchGroupsByFIDandCluster(int FID,int cluster) throws DataAccessException
    {
        String sql = "SELECT * FROM `GSV_DB`.`anchorgroup` WHERE `FID` = " +FID+" and `Cluster` = "+cluster+";";
        return (List<Anchorgroup>)this.jdbcTemplate.query(sql, new AnchorGroupMapper());
    }
    
    public List<Anchorgroup> searchGroupsByFID(int FID) throws DataAccessException
    {
        String sql = "SELECT * FROM `GSV_DB`.`anchorgroup` WHERE `FID` = " +FID+";";
        return (List<Anchorgroup>)this.jdbcTemplate.query(sql, new AnchorGroupMapper());
    }
    

    
    /**
     * Delete statement
     * @param GID Group ID of the group to be deleted
     * @throws DataAccessException 
     */
    public void deleteGroup(Long GID) throws DataAccessException  //TODO: 检查是否需要删除下属anchor表中记录
    {
        String sql = "DELETE FROM `GSV_DB`.`anchorgroup`\n" +
                     "WHERE `GID` = "+GID+";";
         this.jdbcTemplate.execute(sql);

    }
    
        
    /**
     * run a preparedSQL and obtain returned generated key
     * @param sql
     * @return
     * @throws DataAccessException 
     */
    private long run(String sql) throws DataAccessException
    {
         KeyHolder keyHolder = new GeneratedKeyHolder();             

            int updatecount/*number of effected rows*/ = this.jdbcTemplate.update(new PreparedStatementCreator() {  
            @Override  
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
            return ps;  }},keyHolder);
            long id = keyHolder.getKey().longValue(); 
            return id;
            //http://fancyboy2050.iteye.com/blog/1455559
            //在JDBC 3.0规范中，当新增记录时，允许将数据库自动产生的主键值绑定到Statement或PreparedStatement中
            //当autoGeneratedKeys参数设置为Statement.RETURN_GENERATED_KEYS值时即可绑定数据库产生的主键值，设置为Statement.NO_GENERATED_KEYS时，不绑定主键值。

    }
    
    
    
}
