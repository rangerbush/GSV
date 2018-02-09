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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "family")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Family.findAll", query = "SELECT f FROM Family f")
    , @NamedQuery(name = "Family.findByFid", query = "SELECT f FROM Family f WHERE f.fid = :fid")
    , @NamedQuery(name = "Family.findByFamilyName", query = "SELECT f FROM Family f WHERE f.familyName = :familyName")
    , @NamedQuery(name = "Family.findByFamilyPassword", query = "SELECT f FROM Family f WHERE f.familyPassword = :familyPassword")})
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fid")
    private Integer fid;
    @Size(max = 30)
    @Column(name = "familyName")
    private String familyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "FamilyPassword")
    private String familyPassword;
    @JoinTable(name = "Account_Family", joinColumns = {
        @JoinColumn(name = "FID", referencedColumnName = "fid")}, inverseJoinColumns = {
        @JoinColumn(name = "UID", referencedColumnName = "UID")})
    @ManyToMany
    private Collection<Account> accountCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fid")
    private Collection<Device> deviceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fid")
    private Collection<Anchorgroup> anchorgroupCollection;

    public Family() {
    }

    public Family(Integer fid) {
        this.fid = fid;
    }

    public Family(Integer fid, String familyPassword) {
        this.fid = fid;
        this.familyPassword = familyPassword;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyPassword() {
        return familyPassword;
    }

    public void setFamilyPassword(String familyPassword) {
        this.familyPassword = familyPassword;
    }

    @XmlTransient
    public Collection<Account> getAccountCollection() {
        return accountCollection;
    }

    public void setAccountCollection(Collection<Account> accountCollection) {
        this.accountCollection = accountCollection;
    }

    @XmlTransient
    public Collection<Device> getDeviceCollection() {
        return deviceCollection;
    }

    public void setDeviceCollection(Collection<Device> deviceCollection) {
        this.deviceCollection = deviceCollection;
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
        hash += (fid != null ? fid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Family)) {
            return false;
        }
        Family other = (Family) object;
        if ((this.fid == null && other.fid != null) || (this.fid != null && !this.fid.equals(other.fid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.Family[ fid=" + fid + " ]";
    }
    
}
