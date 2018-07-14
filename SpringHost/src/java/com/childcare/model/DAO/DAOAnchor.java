/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Anchor;
import com.childcare.entity.AnchorPK;
import com.childcare.entity.Anchorgroup;
import com.childcare.model.JdbcDataDAOImpl;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

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
             anchor.setAnchorgroup(new Anchorgroup(arg0.getInt("GID")));
             anchor.setAnchorPK(new AnchorPK(arg0.getInt("GID"),arg0.getInt("SeqID")));
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
    @Transactional
    public Integer createAnchor(List<Anchor> list) throws DataAccessException
    {
       // String query = "select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+anchor.getAnchorPK().getGid()+";";
 
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
    
    /**
     * When a group exist, delete all anchors it has and insert input anchors
     * @param list list of anchors to be updated
     * @return
     * @throws DataAccessException 
     */
    @Transactional
    public Integer recreateAnchor(List<Anchor> list) throws DataAccessException
    {
       // String query = "select `anchorgroup`.`MaxSeq` FROM `GSV_DB`.`anchorgroup` WHERE `anchorgroup`.`GID` ="+anchor.getAnchorPK().getGid()+";";
 
         //  Integer maxSeq = this.jdbcTemplate.queryForObject(query, Integer.class);
            String []prepare = {"delete from `anchor` where `GID` = "+list.get(0).getAnchorPK().getGid()+";",//删除已有anchors
                "update `GSV_DB`.`anchorgroup` set `anchorgroup`.`MaxSeq` = 0 where `anchorgroup`.`GID` ="+list.get(0).getAnchorPK().getGid()+";"}; //重置Maxseq
            this.jdbcTemplate.batchUpdate(prepare);
            Integer seq = 0;
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
    
    
    public Anchor getAnchorInstance(int GID,int Seq) throws DataAccessException
    {
        String sql = "SELECT `anchor`.`GID`,\n" +
                     "    `anchor`.`SeqID`,\n" +
                     "    `anchor`.`Longitude`,\n" +
                     "    `anchor`.`Latitude`,\n" +
                     "    `anchor`.`Radius`\n" +
                     "FROM `GSV_DB`.`anchor` WHERE `anchor`.`GID` = "+GID+" and `anchor`.`SeqID` = "+Seq+" ;";
            return (Anchor)this.jdbcTemplate.queryForObject(sql, new AnchorMapper());
    }
    
    public List<Anchor> findByGID(int GID) throws DataAccessException
    {
        String sql = "SELECT `anchor`.`GID`,\n" +
                     "    `anchor`.`SeqID`,\n" +
                     "    `anchor`.`Longitude`,\n" +
                     "    `anchor`.`Latitude`,\n" +
                     "    `anchor`.`Radius`\n" +
                     "FROM `GSV_DB`.`anchor` WHERE `anchor`.`GID` = "+GID+"  ;";
        List<Anchor> list = this.jdbcTemplate.query(sql,new AnchorMapper());
        return list;
    }
    
    public List<Anchor> findByCID(int cid)
    {
        String sql = "select * from `GSV_DB`.`anchor` where `GID` in (select `GID` from `GSV_DB`.`anchorgroup` where `CID` = "+cid+")";
        return this.jdbcTemplate.query(sql, new AnchorMapper());
    }
        
    public List<Anchor> findByMultiGID(List<Integer> gList) throws DataAccessException
    {
        StringBuilder content = new StringBuilder();        
        Iterator<Integer> it = gList.iterator();
        if (it.hasNext())
        {
            content.append(it.next());
        }
        while(it.hasNext())
        {
            content.append(",");
            content.append(it.next());           
        } 
        String sql = "SELECT `anchor`.`GID`,\n" +
                     "    `anchor`.`SeqID`,\n" +
                     "    `anchor`.`Longitude`,\n" +
                     "    `anchor`.`Latitude`,\n" +
                     "    `anchor`.`Radius`\n" +
                     "FROM `GSV_DB`.`anchor` WHERE `anchor`.`GID`IN ( "+content+" ) ;";
        return  this.jdbcTemplate.query(sql,new AnchorMapper());
    }
    
        public Map<Integer,List<Anchor>> findByMultiGroup(List<Anchorgroup> gList) throws DataAccessException
    {
        StringBuilder content = new StringBuilder();        
        Iterator<Anchorgroup> it = gList.iterator();
        HashSet<Integer> set = new HashSet();
        List<Anchor> list;
        Anchorgroup ag;
        Integer type;
        String sql;
        if (it.hasNext())
        {
            ag = it.next();
            content.append(ag.getGid());
            set.add(ag.getType());
        }
        while(it.hasNext())
        {
            ag = it.next();
            content.append(",");
            content.append(ag.getGid());
            set.add(ag.getType());
        } 
        Map<Integer,List<Anchor>> map = new HashMap();
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext())
        {
            type = iter.next();
            sql = "SELECT `anchor`.`GID`,\n" +
                     "    `anchor`.`SeqID`,\n" +
                     "    `anchor`.`Longitude`,\n" +
                     "    `anchor`.`Latitude`,\n" +
                     "    `anchor`.`Radius`\n" +
                     "FROM `GSV_DB`.`anchor` LEFT JOIN `GSV_DB`.`anchorgroup` ON `anchor`.`GID` = `anchorgroup`.`GID`"
                    + " WHERE `anchor`.`GID`IN ( "+content+" ) and `anchorgroup`.`Type` = "+type+";";
            list =  this.jdbcTemplate.query(sql,new AnchorMapper()); 
            map.put(type, list);
        }
        return map;
    }
    
    public List<Anchor> getAllAnchorInstance() throws DataAccessException
    {
        String sql = "select * from  `GSV_DB`.`anchor`";

                List<Anchor> list = this.jdbcTemplate.query(sql,new AnchorMapper());
                return list;

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
        this.jdbcTemplate.execute(sql);
    }

   
    
    
}
