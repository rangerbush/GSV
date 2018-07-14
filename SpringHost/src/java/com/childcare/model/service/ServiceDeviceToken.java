/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.DeviceToken;
import com.childcare.entity.structure.Response;
import com.childcare.model.JdbcDataDAOImpl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author New User
 */
@Service("serviceDeviceToken")
public class ServiceDeviceToken {
    
    @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
    
    @Transactional
    public void register(long uid,String did)
    {
        //UID必须已经存在
        //尝试创建did记录
        this.jdbcDataDAO.getDaoSupervisor().create(did);
        //开始创建
        this.jdbcDataDAO.getDaoDeviceToken().create(did, uid);
    }
    
    public List<String> getDevicesID(long uid)
    {
        List<DeviceToken> list = this.jdbcDataDAO.getDaoDeviceToken().searchByUID(uid);
        List<String> dList = new ArrayList();
        list.stream().forEach((d) -> dList.add(d.getDeviceId()));
        return dList;
    }
    
    
    
    
    
}
