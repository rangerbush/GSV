/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author New User
 */
public class CourierTest{
    
    public CourierTest() {
    }
    

    /**
     * Test of resetPasswordViaAliYun method, of class Courier.
     */
    @Test
    public void testResetPasswordViaAliYun() {
        System.out.println("resetPasswordViaAliYun");
        String text = "this is a text message from GSV";
        String receiver = "zaixiawuming_90@126.com";
        Courier.resetPasswordViaAliYun(text, receiver);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
