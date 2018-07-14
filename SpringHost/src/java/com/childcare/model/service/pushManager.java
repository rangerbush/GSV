/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.entity.Device;
import com.childcare.model.support.ApnsEntity;
import com.google.gson.Gson;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

@Service("pushManager")
/**
 *
 * @author New User
 */
public class pushManager   {
    private ExecutorService pool;
    public pushManager()
    {
        pool = Executors.newCachedThreadPool();
    }
    


    public void push(List<String> targetList, String msg) {
         targetList.stream().forEach(str -> pool.execute(new ApnsEntity(str,msg)));
    }

    
}
