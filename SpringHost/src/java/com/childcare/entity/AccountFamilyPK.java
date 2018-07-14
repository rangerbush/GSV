/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author New User
 */
@Embeddable
public class AccountFamilyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "FID")
    private int fid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UID")
    private long uid;

    public AccountFamilyPK() {
    }

    public AccountFamilyPK(int fid, long uid) {
        this.fid = fid;
        this.uid = uid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fid;
        hash += (int) uid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountFamilyPK)) {
            return false;
        }
        AccountFamilyPK other = (AccountFamilyPK) object;
        if (this.fid != other.fid) {
            return false;
        }
        if (this.uid != other.uid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.AccountFamilyPK[ fid=" + fid + ", uid=" + uid + " ]";
    }
    
}
