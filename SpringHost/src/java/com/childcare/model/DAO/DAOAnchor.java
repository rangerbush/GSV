/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Anchor;
import com.childcare.entity.AnchorPK;
import com.childcare.model.JdbcDataDAOImpl;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author New User
 */
public class DAOAnchor {
    @Resource 
    private JdbcTemplate jdbcTemplate;
    
      
    protected class AnchorMapper implements RowMapper, Serializable{
         @Override
         public Object mapRow(ResultSet arg0, int arg1) throws SQLException
         {
             Anchor anchor = new Anchor();
             anchor.setLongitude(arg0.getBigDecimal("Longitude"));
             anchor.setLatitude(arg0.getBigDecimal("Latitude"));
             anchor.setRadius(arg0.getBigDecimal("Radius"));
             anchor.setAnchorPK(new AnchorPK(arg0.getLong("GID"),arg0.getInt("SeqID")));
             //anchor.setAnchorgroup(new Anchorgroup(arg0.getLong("GID")));
             return anchor;
         }
                 
        
    }
    

    /**
     * Create a new anchor record with given gid, SeqID will be signed from MaxSeq from Group Table.
     * @param list list of anchors
     * @return MaxSeq number used
     * @throws DataAccessException 
     */
    public Integer createAnchor(List<Anchor> list) throws DataAccessException
    {
       // String query = "select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+anchor.getAnchorPK().getGid()+";";
        try{
         //  Integer maxSeq = this.jdbcTemplate.queryForObject(query, Integer.class);
            String prepare = "select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+list.get(0).getAnchorPK().getGid()+";";
            Integer seq = this.jdbcTemplate.queryForObject(prepare, Integer.class);
            String []sql = new String [list.size()+1]; 
            for (int i = 0;i < list.size();i++)
            {
                  sql[i]  = "INSERT INTO `GSV_DB`.`anchor`\n" +
                     "(`GID`,\n" +
                     "`SeqID`,\n" +
                     "`Longitude`,\n" +
                     "`Latitude`,\n" +
                     "`Radius`)\n" +
                     "VALUES\n" +"("+
                     list.get(i).getAnchorPK().getGid()+",\n" +
           //          "(select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+list.get(i).getAnchorPK().getGid()+")+1"+",\n" +
                     ++seq+",\n"+
                     list.get(i).getLongitude()+",\n" +
                     list.get(i).getLatitude()+",\n" +
                     list.get(i).getRadius()+");";
            }
            sql[list.size()] = "UPDATE `GSV_DB`.`anchorgroup`\n" +
                            "SET\n" +
                            "`MaxSeq` = "+seq+"\n" +
                            "WHERE `GID` = "+list.get(0).getAnchorPK().getGid()+";";
            
            //System.out.println(sql[0]);
           // System.out.println(sql[1]);
            jdbcTemplate.batchUpdate(sql);
            return seq;
        }
        catch (DataAccessException e)
        {
            throw e;
        }
        
    }
    
    public Anchor getAnchorInstance(Long GID,int Seq) throws DataAccessException
    {
        String sql = "SELECT `anchor`.`GID`,\n" +
                     "    `anchor`.`SeqID`,\n" +
                     "    `anchor`.`Longitude`,\n" +
                     "    `anchor`.`Latitude`,\n" +
                     "    `anchor`.`Radius`\n" +
                     "FROM `GSV_DB`.`anchor` WHERE `anchor`.`GID` = "+GID+" and `anchor`.`SeqID` = "+Seq+" ;";
        try{
            return (Anchor)this.jdbcTemplate.queryForObject(sql, new AnchorMapper());
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }
    
    public List<Anchor> getAllAnchorInstance() throws DataAccessException
    {
        String sql = "select * from  `GSV_DB`.`anchor`";
        try 
        {
                List<Anchor> list = this.jdbcTemplate.query(sql,new AnchorMapper());
                return list;
        }
               catch (DataAccessException e)
        {
            throw e;
        }
    }
    
    public void deleteAnchor(List<Anchor> list)throws DataAccessException
    {
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < list.size();i++)
        {
            if (i!=0)
                sb.append(",");
            sb.append(list.get(i).getAnchorPK().getSeqID());
        }
        String sql = "DELETE FROM `GSV_DB`.`anchor`\n" +
                     "WHERE (`anchor`.`GID` = "+list.get(0).getAnchorPK().getGid()+") and (`anchor`.`SeqID` in ("+sb.toString()+"));";
        //System.out.println(sql);
        try{
        this.jdbcTemplate.execute(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
    }

    public void updateAnchor(List<Anchor> list)throws DataAccessException
    {
        
        try
        {
            String prepare = "select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+list.get(0).getAnchorPK().getGid()+";";
            Integer seq = this.jdbcTemplate.queryForObject(prepare, Integer.class);
            //anchors in list are in the same group and MaxSeq is obtained
            String []sql = new String [list.size()];
            for (int i = 0; i<list.size(); i++)
            {
                
                sql[i] = "UPDATE `GSV_DB`.`anchor`\n" +
                         "SET\n" +
                         "`Longitude` = <{Longitude: }>,\n" +
                         "`Latitude` = <{Latitude: }>,\n" +
                         "`Radius` = <{Radius: }>\n" +
                         "WHERE `GID` = <{expr}> AND `SeqID` = <{expr}>;";
            }
        }
        catch (DataAccessException e)
        {
            throw e;
        }
        }

    
    
}
