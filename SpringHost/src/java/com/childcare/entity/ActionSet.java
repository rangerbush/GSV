/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "ActionSet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionSet.findAll", query = "SELECT a FROM ActionSet a")
    , @NamedQuery(name = "ActionSet.findByAid", query = "SELECT a FROM ActionSet a WHERE a.aid = :aid")
    , @NamedQuery(name = "ActionSet.findByDesc", query = "SELECT a FROM ActionSet a WHERE a.desc = :desc")})
public class ActionSet implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actionSet")
    private Collection<ActionTaken> actionTakenCollection;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Discard")
    private boolean discard;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "AID")
    private Integer aid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESC")
    private String desc;
    @JoinTable(name = "ActionTaken", joinColumns = {
        @JoinColumn(name = "AID", referencedColumnName = "AID")}, inverseJoinColumns = {
        @JoinColumn(name = "GID", referencedColumnName = "GID")})
    @ManyToMany
    private Collection<Anchorgroup> anchorgroupCollection;

    public ActionSet() {
    }

    public ActionSet(Integer aid) {
        this.aid = aid;
    }

    public ActionSet(Integer aid, String desc) {
        this.aid = aid;
        this.desc = desc;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlTransient
    public Collection<Anchorgroup> getAnchorgroupCollection() {
        return anchorgroupCollection;
    }

    public void setAnchorgroupCollection(Collection<Anchorgroup> anchorgroupCollection) {
        this.anchorgroupCollection = anchorgroupCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aid != null ? aid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionSet)) {
            return false;
        }
        ActionSet other = (ActionSet) object;
        if ((this.aid == null && other.aid != null) || (this.aid != null && !this.aid.equals(other.aid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.ActionSet[ aid=" + aid + " ]";
    }

    public boolean getDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    @XmlTransient
    public Collection<ActionTaken> getActionTakenCollection() {
        return actionTakenCollection;
    }

    public void setActionTakenCollection(Collection<ActionTaken> actionTakenCollection) {
        this.actionTakenCollection = actionTakenCollection;
    }
    
}
