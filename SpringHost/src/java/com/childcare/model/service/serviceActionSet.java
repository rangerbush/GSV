/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.service;

import com.childcare.model.JdbcDataDAOImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("serviceActionSet")
/**
 *
 * @author New User
 */
public class serviceActionSet {
        @Resource
    private JdbcDataDAOImpl jdbcDataDAO;
        
    public boolean isDiscarded()
    {
       return true;
    }
    
}
