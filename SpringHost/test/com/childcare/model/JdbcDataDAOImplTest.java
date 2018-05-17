/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import com.childcare.entity.Account;
import com.childcare.entity.PasswdChanger;
import com.childcare.model.DAO.DAOActionSet;
import com.childcare.model.DAO.DAOAnchor;
import com.childcare.model.DAO.DAOAnchorGroup;
import com.childcare.model.DAO.DAODevice;
import com.childcare.model.DAO.DAOFamily;
import com.childcare.model.support.PasswordUtility;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;




/**
 *
 * @author New User
 */
public class JdbcDataDAOImplTest extends TestFramework {
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    public JdbcDataDAOImplTest() {
    }
    

    /**
     * Test of getDaoDevice method, of class JdbcDataDAOImpl.
     */
    @Test
    public void testGetDaoDevice() {
        System.out.println("getDaoDevice");
        JdbcDataDAOImpl instance = this.jdbcDataDAO;
        DAODevice result = instance.getDaoDevice();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getDaoFamily method, of class JdbcDataDAOImpl.
     */
    @Test

    public void testGetDaoFamily() {
        System.out.println("getDaoFamily");
        JdbcDataDAOImpl instance = this.jdbcDataDAO;
        DAOFamily result = instance.getDaoFamily();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getUtility method, of class JdbcDataDAOImpl.
     */
    @Test

    public void testGetUtility() {
        System.out.println("getUtility");
        JdbcDataDAOImpl instance = new JdbcDataDAOImpl();
        PasswordUtility result = this.jdbcDataDAO.getUtility();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getDaoAnchorGroup method, of class JdbcDataDAOImpl.
     */
    @Test

    public void testGetDaoAnchorGroup() {
        System.out.println("getDaoAnchorGroup");
        DAOAnchorGroup result = this.jdbcDataDAO.getDaoAnchorGroup();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getDaoAnchor method, of class JdbcDataDAOImpl.
     */
    @Test

    public void testGetDaoAnchor() {
        System.out.println("getDaoAnchor");
        DAOAnchor result = this.jdbcDataDAO.getDaoAnchor();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }



    /**
     * Test of getDaoActionSet method, of class JdbcDataDAOImpl.
     */
    @Test
 
    public void testGetDaoActionSet() {
        System.out.println("getDaoActionSet");
        DAOActionSet result = this.jdbcDataDAO.getDaoActionSet();
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
    }


    
}
