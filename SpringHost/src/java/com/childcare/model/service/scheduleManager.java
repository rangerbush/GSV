/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Device;
import com.childcare.entity.PushTarget;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.websocket.MyWebSocketHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    @Resource 
    private MyWebSocketHandler socketMaster;
    
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
            List<String> didList = list.stream().map(d -> d.getDeviceID()).collect(Collectors.toList());
            List<PushTarget> pList = this.dao.getDaoSupervisor().findUserTokenByDID(didList);
            System.out.println("-------> Scheduler: Finished |"+list.size()+" missing devices found.<-----------");
            push(pList,list);
        }
        catch(DataAccessException | IllegalStateException e)
        {
            Logger log = Logger.getLogger("Scheduler");
            log.log(Level.SEVERE, e.getMessage());
        }
    }
    @Scheduled(fixedRate=10000)
    public void socketPushTask()
    {
        this.socketMaster.push();
    }
    
    private void push(List<PushTarget> pList,List<Device> deviceList)
    {
        List<Integer> fidList = pList.stream().map(p->p.getFid()).collect(Collectors.toList());
        fidList.stream().forEach(fid-> {
            List<String> targetList = pList.stream()
                    .filter(pt->pt.getFid()==fid)
                    .map(pt->pt.getDeviceToken())
                    .collect(Collectors.toList());//device tokens to be informed
            StringBuilder sb = new StringBuilder();
            sb.append("WARNING: We just lost contact with your ");
            deviceList.stream()
                    .filter(d1->Objects.equals(d1.getFid().getFid(), fid))
                    .forEach(d->sb.append("device ").append(d.getDeviceID()).append("(bonded to"+d.getCid()!=null?"child "+d.getCid().getCid():"no child"+")"));
            sb.append(", please take following procedures.");
            this.pusher.push(targetList, sb.toString());

        });
    }

    
}
