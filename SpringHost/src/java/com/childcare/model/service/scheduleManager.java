/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Device;
import com.childcare.model.JdbcDataDAOImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
/**
 *
 * @author New User
 */
public class scheduleManager {
    
    @Resource
    private JdbcDataDAOImpl dao;
    @Resource
    private pushManager pusher;
    
    @Scheduled(fixedDelay = 600000) //10 mins
    public void missingDetector()
    {
        Date now = new Date(); 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        
        try{
            System.out.println("-------> Scheduler: Start at "+dateFormat.format(now)+" <-----------");
            this.dao.getDaoDevice().registerMissing(10," minute");
            List<Device> list = this.dao.getDaoDevice().getMissing();
            this.dao.getDaoDevice().missing2Deactivated(5,10," minute");
            System.out.println("-------> Scheduler: Finished |"+list.size()+" missing devices found.<-----------");
            list.forEach((d) -> {
                this.pusher.push(d);
            }); //PUSH warnings
        }
        catch(DataAccessException | IllegalStateException e)
        {
            Logger log = Logger.getLogger("Scheduler");
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    
}
