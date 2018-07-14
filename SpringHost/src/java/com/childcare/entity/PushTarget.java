/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author New User
 */
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class PushTarget {
    @Setter
    @Getter
    private String deviceToken;
    @Setter
    @Getter
    private int fid;
    
}
