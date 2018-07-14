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
@Table(name = "Account_Family")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountFamily.findAll", query = "SELECT a FROM AccountFamily a")
    , @NamedQuery(name = "AccountFamily.findByFid", query = "SELECT a FROM AccountFamily a WHERE a.accountFamilyPK.fid = :fid")
    , @NamedQuery(name = "AccountFamily.findByUid", query = "SELECT a FROM AccountFamily a WHERE a.accountFamilyPK.uid = :uid")
    , @NamedQuery(name = "AccountFamily.findByStatus", query = "SELECT a FROM AccountFamily a WHERE a.status = :status")})
public class AccountFamily implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isOwner")
    private int isOwner;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccountFamilyPK accountFamilyPK;
    @JoinColumn(name = "FID", referencedColumnName = "fid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Family family;
    @JoinColumn(name = "UID", referencedColumnName = "UID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Account account;

    public AccountFamily() {
    }

    public AccountFamily(AccountFamilyPK accountFamilyPK) {
        this.accountFamilyPK = accountFamilyPK;
    }

    public AccountFamily(int fid, int uid) {
        this.accountFamilyPK = new AccountFamilyPK(fid, uid);
    }

    public AccountFamilyPK getAccountFamilyPK() {
        return accountFamilyPK;
    }

    public void setAccountFamilyPK(AccountFamilyPK accountFamilyPK) {
        this.accountFamilyPK = accountFamilyPK;
    }


    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountFamilyPK != null ? accountFamilyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountFamily)) {
            return false;
        }
        AccountFamily other = (AccountFamily) object;
        if ((this.accountFamilyPK == null && other.accountFamilyPK != null) || (this.accountFamilyPK != null && !this.accountFamilyPK.equals(other.accountFamilyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.AccountFamily[ accountFamilyPK=" + accountFamilyPK + " ]";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }
    
}
