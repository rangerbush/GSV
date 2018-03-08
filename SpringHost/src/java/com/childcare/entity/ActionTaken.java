/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "ActionTaken")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionTaken.findAll", query = "SELECT a FROM ActionTaken a")
    , @NamedQuery(name = "ActionTaken.findByAid", query = "SELECT a FROM ActionTaken a WHERE a.actionTakenPK.aid = :aid")
    , @NamedQuery(name = "ActionTaken.findByGid", query = "SELECT a FROM ActionTaken a WHERE a.actionTakenPK.gid = :gid")
    , @NamedQuery(name = "ActionTaken.findByStatus", query = "SELECT a FROM ActionTaken a WHERE a.status = :status")})
public class ActionTaken implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ActionTakenPK actionTakenPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    private int status;
    @JoinColumn(name = "AID", referencedColumnName = "AID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ActionSet actionSet;
    @JoinColumn(name = "GID", referencedColumnName = "GID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Anchorgroup anchorgroup;

    public ActionTaken() {
    }

    public ActionTaken(ActionTakenPK actionTakenPK) {
        this.actionTakenPK = actionTakenPK;
    }

    public ActionTaken(ActionTakenPK actionTakenPK, int status) {
        this.actionTakenPK = actionTakenPK;
        this.status = status;
    }

    public ActionTaken(int aid, int gid) {
        this.actionTakenPK = new ActionTakenPK(aid, gid);
    }

    public ActionTakenPK getActionTakenPK() {
        return actionTakenPK;
    }

    public void setActionTakenPK(ActionTakenPK actionTakenPK) {
        this.actionTakenPK = actionTakenPK;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ActionSet getActionSet() {
        return actionSet;
    }

    public void setActionSet(ActionSet actionSet) {
        this.actionSet = actionSet;
    }

    public Anchorgroup getAnchorgroup() {
        return anchorgroup;
    }

    public void setAnchorgroup(Anchorgroup anchorgroup) {
        this.anchorgroup = anchorgroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionTakenPK != null ? actionTakenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionTaken)) {
            return false;
        }
        ActionTaken other = (ActionTaken) object;
        if ((this.actionTakenPK == null && other.actionTakenPK != null) || (this.actionTakenPK != null && !this.actionTakenPK.equals(other.actionTakenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.ActionTaken[ actionTakenPK=" + actionTakenPK + " ]";
    }
    
}
