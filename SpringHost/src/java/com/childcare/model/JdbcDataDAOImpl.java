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
import com.childcare.model.DAO.DAOAccount;
import com.childcare.model.DAO.DAOActionSet;
import com.childcare.model.DAO.DAOActionTaken;
import com.childcare.model.DAO.DAOAnchor;
import com.childcare.model.DAO.DAOAnchorGroup;
import com.childcare.model.DAO.DAOChild;
import com.childcare.model.DAO.DAOContact;
import com.childcare.model.DAO.DAODevice;
import com.childcare.model.DAO.DAODeviceToken;
import com.childcare.model.DAO.DAOFamily;
import com.childcare.model.DAO.DAORefreshToken;
import com.childcare.model.DAO.DAOSupervisor;
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
import javax.transaction.TransactionManager;
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
    @Resource(name="daoActionTaken")
    private DAOActionTaken daoActionTaken;
    @Resource(name="daoAccount")
    private DAOAccount daoAccount;
    @Resource(name="daoRefreshToken")
    private DAORefreshToken daoToken;
    @Resource(name="daoChild")
    private DAOChild daoChild;
    @Resource(name="daoContact")
    private DAOContact daoContact;
    @Resource(name="daoSupervisor")
    private DAOSupervisor daoSupervisor;
    @Resource(name="daoDeviceToken")
    private DAODeviceToken daoDeviceToken;
    @Resource(name="transactionManager")
    private org.springframework.jdbc.datasource.DataSourceTransactionManager tm;
    
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
    
    public String generalTest()
    {
       return "TransactionManager is "+(this.tm==null?"null":"not null");
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
     * @return the daoActionTaken
     */
    public DAOActionTaken getDaoActionTaken() {
        return daoActionTaken;
    }

    /**
     * @return the daoAccount
     */
    public DAOAccount getDaoAccount() {
        return daoAccount;
    }

    /**
     * @return the daoToken
     */
    public DAORefreshToken getDaoToken() {
        return daoToken;
    }

    /**
     * @return the daoChild
     */
    public DAOChild getDaoChild() {
        return daoChild;
    }

    /**
     * @return the daoContact
     */
    public DAOContact getDaoContact() {
        return daoContact;
    }

    /**
     * @return the daoDeviceToken
     */
    public DAODeviceToken getDaoDeviceToken() {
        return daoDeviceToken;
    }


    /**
     * @return the daoSupervisor
     */
    public DAOSupervisor getDaoSupervisor() {
        return daoSupervisor;
    }


}  