/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.websocket;

import java.util.Calendar;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author New User
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class standardMessage {
    private String payload;
    @NonNull
    private Integer command;
    //0 - new query 开始查询携带的DeviceID，终止之前执行的查询。deviceID不可为空
    //1 - heartbeat 心跳包，deviceID可为空
    //2 - stop service 停止UID对应的thread。deviceID可为空
    
}
