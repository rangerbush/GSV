/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.PushTarget;
import com.childcare.model.JdbcDataDAOImpl;
import com.childcare.model.exception.Alert;
import com.childcare.model.exception.RedAlert;
import com.childcare.model.exception.YellowAlert;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
/**
 *
 * @author New User
 */
public class AlertHandler {
    @Resource
    private pushManager pusher;
    @Resource
    private JdbcDataDAOImpl jdbc;
    
    protected void handle(Exception e)
    {
            if (e.getClass() == YellowAlert.class)
                yellow((YellowAlert)e);
            if (e.getClass() == RedAlert.class)
                red((RedAlert)e);
    }
    
    private void yellow(YellowAlert e)
    {
        List<String> list = new ArrayList();
        list.add(e.getDevice().getDeviceID());
        List<PushTarget> pList = this.jdbc.getDaoSupervisor().findUserTokenByDID(list);
        List<String> tokenList = pList.stream().map(p->p.getDeviceToken()).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("Your device ").append(e.getDevice().getDeviceID()).append(", bonded to ").append("Child "+e.getDevice().getCid()!=null?e.getDevice().getCid().getCid():"no child")
                .append(",has just violated rule group ").append(e.getGid());
        pusher.push(tokenList, sb.toString());
    }
    
    private void red(RedAlert e)
    {
        List<String> list = new ArrayList();
        list.add(e.getDevice().getDeviceID());
        List<PushTarget> pList = this.jdbc.getDaoSupervisor().findUserTokenByDID(list);
        List<String> tokenList = pList.stream().map(p->p.getDeviceToken()).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("Your device ").append(e.getDevice().getDeviceID()).append(", bonded to ").append("Child "+e.getDevice().getCid()!=null?e.getDevice().getCid().getCid():"no child")
                .append(",has just violated rule group ").append(e.getGid());
        pusher.push(tokenList, sb.toString());
        //TODO call emergency etc.
    }
    
}
