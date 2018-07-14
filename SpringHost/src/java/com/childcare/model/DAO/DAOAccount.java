/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.childcare.entity.Account;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.PasswdChanger;
import com.childcare.model.service.serviceChild;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.Serializable;
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
public class DAOAccount {
     @Resource 
    private JdbcTemplate jdbcTemplate;
     @Resource
     private serviceChild service;
     public final String defaultFileName = "default.png";
     
      /*---------------------------ACCOUNT------------------------------*/


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
            user.setFirstName(arg0.getString("FirstName"));  
            user.setLastName(arg0.getString("LastName"));
            user.setEmail(arg0.getString("Email"));  
            user.setPin(arg0.getString("PIN"));
            user.setPassword(arg0.getString("Password"));  
            user.setDiscard(arg0.getInt("discard")!=0);  
            user.setPhone(arg0.getString("Phone"));
            user.setAvatar(service.BASE_URL+"avatar/"+arg0.getString("avatar"));
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
     * @param account account instance used as data source
     * @return 
     */
    public long createAccount(Account account) throws DataAccessException {  
        // TODO Auto-generated method stub  
        //example ('Enterprise','123','mail','12345','0')
        String sql = "insert into account (FirstName,LastName,Password,Email,Phone,discard,PIN,avatar)"  
                    + "values ('"+account.getFirstName().replaceAll("\'", "\\\\'")+"','"+account.getLastName().replaceAll("\'", "\\\\'")+"','"+account.getPassword().replaceAll("\'", "\\\\'")+"','"+account.getEmail().replaceAll("\'", "\\\\'")+"','"+account.getPhone().replaceAll("\'", "\\\\'")+"',0,'"+account.getPin()+
                "','"+this.defaultFileName+"');";
        try
        {
            return this.run(sql);
        }
        catch (DataAccessException e)
        {
            throw e;
        }
       // System.out.println("---> Create Account: "+account.getUserName()+"   -----------");
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
    
    
    /**
     * get Account Instance with UID
     * @param uid
     * @return 
     */
    public Account getAccountInstance(long uid)
    {
        String sql = "select * from account where UID = ?;";  
        Object[] params = {uid};  
        int[] types = {Types.INTEGER}; 
        return (Account)jdbcTemplate.queryForObject(sql, params, types, new AccountMapper());
    }
    
        /**
     * get Account Instance with UID
     * @param email
     * @return 
     */
    public Account getAccountInstanceByEmail(String email)
    {
        String sql = "select * from account where Email = ?;";  
        Object[] params = {email};  
        int[] types = {Types.VARCHAR}; 
        return (Account)jdbcTemplate.queryForObject(sql, params, types, new AccountMapper());
    }
    
        /**
     * get Account Instance with UID
     * @param uid
     * @return 
     */
    public String getAccountPassword(long uid)
    {
        String sql = "select `Password` from account where UID = ?;";  
        Object[] params = {uid};  
        int[] types = {Types.INTEGER}; 
       // return (String)jdbcTemplate.queryForObject(sql, params, types, new AccountMapper());
       return this.jdbcTemplate.queryForObject(sql, params, types, String.class);
    }
    
    
    /**
     * Update email ,phone information through an account instance
     * @param account
     * @throws DataAccessException 
     */
    public void update(Account account) throws DataAccessException
    {
        String sql = "UPDATE `GSV_DB`.`account`\n" +
                     " SET\n" +
                     " `FirstName` = '"+account.getFirstName().replaceAll("\'", "\\\\'")+"',\n" +
                     " `Password` = '"+account.getPassword()+"',\n" +
                     " `Email` = '"+account.getEmail().replaceAll("\'", "\\\\'")+"',\n" +
                     " `Phone` = '"+account.getPhone().replaceAll("\'", "\\\\'")+"',\n" +
                     " `LastName` = '"+account.getLastName().replaceAll("\'", "\\\\'")+"'\n" +
                     " WHERE `UID` = "+account.getUid()+";"; 
        try{
            this.jdbcTemplate.execute(sql);
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
    public void updatePartial(Account account) throws DataAccessException
    {
        String sql = "UPDATE `GSV_DB`.`account`\n" +
                     " SET\n" +
                     " `FirstName` = '"+account.getFirstName().replaceAll("\'", "\\\\'")+"',\n" +
                     " `Email` = '"+account.getEmail().replaceAll("\'", "\\\\'")+"',\n" +
                     " `Phone` = '"+account.getPhone().replaceAll("\'", "\\\\'")+"',\n" +
                     " `LastName` = '"+account.getLastName()+"'\n" +
                     " WHERE `UID` = "+account.getUid()+";"; 
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
    public void updatePassword(PasswdChanger psd)
    {
            String sql = "update account set Password = '"+psd.getNewPasswd()+"' where UID = "+psd.getUid()+";"; 
            this.jdbcTemplate.execute(sql);

    }
    
    public void updatePassword(String password, String mail)
    {
            String sql = "update account set Password = '"+password+"' where Email = '"+mail+"';"; 
            this.jdbcTemplate.execute(sql);

    }
    
    public void updateAvatar(long uid, String avatar)
    {
        String sql = "update account set avatar = '"+avatar+"' where UID ="+uid+";";
        this.jdbcTemplate.execute(sql);
    }
    
    public List<Account> searchByFID(int fid)
    {
        String sql = "select * from account where UID in (select UID from Account_Family where FID ="+fid+");";
        return this.jdbcTemplate.query(sql, new AccountMapper());
    }
}
