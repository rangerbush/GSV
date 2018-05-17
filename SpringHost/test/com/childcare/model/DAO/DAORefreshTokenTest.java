/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.DAO;

import com.auth0.jwt.interfaces.Claim;
import com.childcare.entity.Account;
import com.childcare.entity.RefreshToken;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.TestFramework;
import com.childcare.model.service.serviceAccount;
import com.childcare.model.support.JsonWebTokenUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
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
public class DAORefreshTokenTest extends TestFramework{
    
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    private Account account;
    @Resource
    private serviceAccount service;

    @Before
    public void setUp() {
       // this.account = jdbcDataDAO.getDaoAccount().getAccountInstance(21);
    }
    
    /**
     * Test of create method, of class DAORefreshToken.
     */
    
    public void test1() {
        System.out.println("create");
        String jti =  JsonWebTokenUtil.jtiGenerator(50);
        System.out.println(jti);
        //String token = JsonWebTokenUtil.refreshToken(21,jti);
        this.jdbcDataDAO.getDaoToken().create(jti);
        System.out.println("JTI:"+jti);
       // System.out.println("RefreshToken:"+token);
        //JsonWebTokenUtil.verify(token);
        RefreshToken rt = this.jdbcDataDAO.getDaoToken().find(jti);
        assertNotNull(rt);
        // TODO review the generated test code and remove the default call to fail.
    }
    
    
    public void test2() {
        Map<String,Claim> map =JsonWebTokenUtil.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVSUQiOjIxLCJUeXBlIjoiUmVmcmVzaCIsImV4cCI6MTUyMzI5OTkwOCwiVFRMIjo1LCJpYXQiOjE1MjMyMTM1MDgsImp0aSI6Im54UzFZaHUyVHRreXc4aWh3RDV0S1BXQlg5bHY2Y0hqS1RkOTY2dE5aOTZHRko5d1lyIn0.iucUqTNtsHwudDUSEgS2t7KJGqumcJV3230N-N960jA");
        long uid = ((Claim)map.get("UID")).asLong();
        boolean flag = ((Claim)map.get("Type")).asString().equalsIgnoreCase("refresh");
        assertTrue(uid==21 & flag);
    }
    
    
    public void test3()
    {
        String str = JsonWebTokenUtil.accessToken(21);
        System.out.println("Test3: "+str);
        assertNull(null);
    }
    
    @Test
    public void test4()
    {
        String jti = JsonWebTokenUtil.jtiGenerator(50);
       // String rt = JsonWebTokenUtil.refreshToken(23,jti);
        this.jdbcDataDAO.getDaoToken().create(jti);
        Gson gson = new Gson();
        Map<String,String> map = new HashMap();
        map.put("Refresh", "asdaga");
        String rep = gson.toJson(service.refreshToken(map));
        System.out.println("Test4: ");
        System.out.println(rep);
        assertNull(null);
    }


}
