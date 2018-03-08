/*
 * This class uses BCrypt from
 * http://www.mindrot.org/projects/jBCrypt/
 * for secure hashing functions.
 * and open the template in the editor.

 * License copied as below:
jBCrypt is subject to the following license:

/*
 * Copyright (c) 2006 Damien Miller <djm@mindrot.org>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * See
 * http://www.mindrot.org/files/jBCrypt/LICENSE
 */
package com.childcare.model;

import com.childcare.entity.Account;
import com.childcare.entity.Anchor;
import com.childcare.entity.AnchorPK;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import com.childcare.entity.PasswdChanger;
import com.childcare.model.DAO.DAOActionSet;
import com.childcare.model.DAO.DAOAnchor;
import com.childcare.model.DAO.DAOAnchorGroup;
import com.childcare.model.DAO.DAODevice;
import com.childcare.model.DAO.DAOFamily;
import com.childcare.model.support.PasswordUtility;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author New User
 */
@Repository("jdbcDataDAO")  
public class JdbcDataDAOImpl{  
    @Resource  
    private JdbcTemplate jdbcTemplate;   
    @Resource(name="daoDevice") 
    private DAODevice daoDevice;
    @Resource(name = "utility")
    private PasswordUtility utility;
    @Resource(name="daoFamily") 
    private DAOFamily daoFamily;
    @Resource(name="daoGroup") 
    private DAOAnchorGroup daoAnchorGroup;
    @Resource(name="daoAnchor") 
    private DAOAnchor daoAnchor;
    @Resource(name="daoActionSet") 
    private DAOActionSet daoActionSet;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  

    }  
 
    /*
    private void initialize()
    {
        //this.setUtility(new PasswordUtility(this.getJdbcTemplate(),this));
       // this.setDaoDevice(new DAODevice(this.getJdbcTemplate()));
       // this.daoFamily = new DAOFamily(this.getJdbcTemplate());
      //  this.daoAnchorGroup = new DAOAnchorGroup(this.getJdbcTemplate());
       // this.daoAnchor = new DAOAnchor(this, this.getJdbcTemplate());
      //  this.daoActionSet = new DAOActionSet(this,this.getJdbcTemplate());
        
    }
    */

  
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
            getJdbcTemplate().update(sql,params,types);  
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
            getJdbcTemplate().update(sql, params, types);  
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
        return ((Account)getJdbcTemplate().queryForObject(sql, params, types, new AccountMapper())).getUid();
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
        return (Account)getJdbcTemplate().queryForObject(sql, params, types, new AccountMapper());
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
            this.getJdbcTemplate().execute(sql);
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
                 this.getJdbcTemplate().execute(sql);
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
   
    
    
    
    /*
    ================================================Device=====================
   
    */
    
  
      
      /*=====================Anchor Group===============*/
 
     /*============================Anchor ==================*/
  
    /**
     * @return the daoDevice
     */
    public DAODevice getDaoDevice() {
        return daoDevice;
    }

    /**
     * @return the daoFamily
     */
    public DAOFamily getDaoFamily() {
        return daoFamily;
    }

    /**
     * @return the utility
     */
    public PasswordUtility getUtility() {
        return utility;
    }

    /**
     * @return the daoAnchorGroup
     */
    public DAOAnchorGroup getDaoAnchorGroup() {
        return daoAnchorGroup;
    }

    /**
     * @return the daoAnchor
     */
    public DAOAnchor getDaoAnchor() {
        return daoAnchor;
    }

    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * @return the daoActionSet
     */
    public DAOActionSet getDaoActionSet() {
        return daoActionSet;
    }

    /**
     * @param daoDevice the daoDevice to set
     */
    public void setDaoDevice(DAODevice daoDevice) {
        this.daoDevice = daoDevice;
    }

    /**
     * @param utility the utility to set
     */
    public void setUtility(PasswordUtility utility) {
        this.utility = utility;
    }
    
}  