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
public class ActionTakenPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "AID")
    private int aid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GID")
    private int gid;

    public ActionTakenPK() {
    }

    public ActionTakenPK(int aid, int gid) {
        this.aid = aid;
        this.gid = gid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) aid;
        hash += (int) gid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionTakenPK)) {
            return false;
        }
        ActionTakenPK other = (ActionTakenPK) object;
        if (this.aid != other.aid) {
            return false;
        }
        if (this.gid != other.gid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.ActionTakenPK[ aid=" + aid + ", gid=" + gid + " ]";
    }
    
}
