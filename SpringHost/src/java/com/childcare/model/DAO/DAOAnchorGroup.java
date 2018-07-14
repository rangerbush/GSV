/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Child;
import com.childcare.entity.Family;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class DAOAnchorGroup {
    @Resource 
     private  JdbcTemplate jdbcTemplate;
    
    private Anchorgroup normalizer(Anchorgroup group)
    {
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
            group.setGid(arg0.getInt("GID"));  
            group.setGroupName(arg0.getString("GroupName"));
            Child c = new Child();
            c.setCid(arg0.getInt("CID"));
            group.setCid(c);
            group.setType(arg0.getInt("Type"));
            group.setMaxSeq(arg0.getInt("MaxSeq"));
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
          group = this.normalizer(group);
        String sql = "UPDATE `GSV_DB`.`anchorgroup`\n" +
                     "SET\n" +
                    // "`GID` = "+group.getGid()+",\n" +
                     "`GroupName` = '"+group.getGroupName().replaceAll("\'", "\\\\'")+"',\n" +
           //          "`FID` = "+group.getFid().getFid()+",\n" +
                     "`Type` = "+group.getType()+" \n" +
                     "WHERE `GID` = "+group.getGid()+";";
         this.jdbcTemplate.execute(sql);       
      }
    
    public int createGroup(Anchorgroup group)
    {
        group = this.normalizer(group);
        String sql = "INSERT INTO `GSV_DB`.`anchorgroup`\n" +
                    // "(`GID`,\n" +
                     "(`GroupName`,\n" +
                     "`CID`,"+
                     "`MaxSeq`,\n"+
                     "`Type`) \n"+
                     "VALUES \n" +
                "("+
                 //    "(<{GID: }>,\n" +
                     "'"+group.getGroupName().replaceAll("\'", "\\\\'")+"',\n " +
                     group.getCid().getCid()+","+
                     0+",\n " +
                     group.getType()+ ");";
          System.out.println(sql);
         return this.run(sql);

    }
            
    /**
     * Select statement to get a group instance using GID
     * @param GID Group ID
     * @return 
     * @throws DataAccessException 
     */
    public Anchorgroup getGroupInstance(int GID) throws DataAccessException
    {
        String sql = "select * from `GSV_DB`.`anchorgroup` where `GID` = ?;";  
        Object[] params = {GID};  
        int[] types = {Types.VARCHAR}; 
        return (Anchorgroup)jdbcTemplate.queryForObject(sql, params, types, new AnchorGroupMapper());     
    }
    
    public List<Anchorgroup> getGroups(List<Integer> gidList)
    {
        if (gidList.isEmpty())
            throw new DataAccessException("Input GroupID list is empty."){};
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> it = gidList.iterator();
        while(it.hasNext())
        {
            sb.append(",").append(it.next());
        }
        sb.deleteCharAt(0);
        String sql = "select * from `GSV_DB`.`anchorgroup` where `GID` IN ("+sb.toString()+");";
        return this.jdbcTemplate.query(sql, new AnchorGroupMapper());
    }
    
    /**
     * 搜寻有相同FID及cluster的group列表
     * @param cid
     * @return
     * @throws DataAccessException 
     */
    public List<Anchorgroup> searchGroupsByCID(int cid)
    {
        String sql = "SELECT * FROM `GSV_DB`.`anchorgroup` WHERE  `CID` = "+cid+";";
        return (List<Anchorgroup>)this.jdbcTemplate.query(sql, new AnchorGroupMapper());
    }
    
    
    /**
     * Delete statement
     * @param GID Group ID of the group to be deleted
     * @throws DataAccessException 
     */
    public void deleteGroup(int GID) throws DataAccessException  //TODO: 检查是否需要删除下属anchor表中记录
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
    private int run(String sql) throws DataAccessException
    {
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
    
    
    
}
