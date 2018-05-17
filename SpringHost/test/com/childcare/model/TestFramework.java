/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:web/WEB-INF/applicationContext.xml"}) //加载配置文件   
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)  
@Transactional  
/**
 *
 * @author New User
 */
public abstract class TestFramework extends AbstractTransactionalJUnit4SpringContextTests/**/{
    
}
