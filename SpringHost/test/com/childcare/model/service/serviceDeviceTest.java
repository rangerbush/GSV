/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Anchor;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import com.childcare.entity.structure.Response;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.TestFramework;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author New User
 */
public class serviceDeviceTest extends TestFramework{
    
    @Resource
    private serviceDevice sd;
    @Resource
    private JdbcDataDAOImpl jdbc;
    public serviceDeviceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
/*
    @Test
    public void testConstraintCheck()
    {
        Device device = this.jdbc.getDaoDevice().getDeviceInstance("demo");
        assertEquals(device.getLatitude(), new BigDecimal(-37.8175493));
        assertEquals(device.getLongitude(),new BigDecimal(144.9693288));
        this.sd.constraintCheck(device);
    }


    /**
     * Test of touch method, of class serviceDevice.
     */

    public void testTouch() {
        System.out.println("touch");
        Device device = null;
        serviceDevice instance = new serviceDevice();
        Response expResult = null;
        Response result = instance.touch(device);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class serviceDevice.
     */
    public void testUpdate() {
        System.out.println("update");
        Device device = null;
        serviceDevice instance = new serviceDevice();
        Response expResult = null;
        Response result = instance.update(device);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
