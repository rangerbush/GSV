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
public class AnchorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "GID")
    private long gid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SeqID")
    private int seqID;

    public AnchorPK() {
    }

    public AnchorPK(long gid, int seqID) {
        this.gid = gid;
        this.seqID = seqID;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public int getSeqID() {
        return seqID;
    }

    public void setSeqID(int seqID) {
        this.seqID = seqID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (long) gid;
        hash += (int) seqID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnchorPK)) {
            return false;
        }
        AnchorPK other = (AnchorPK) object;
        if (this.gid != other.gid) {
            return false;
        }
        if (this.seqID != other.seqID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.AnchorPK[ gid=" + gid + ", seqID=" + seqID + " ]";
    }
    
}
