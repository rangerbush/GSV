/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "RefreshToken")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RefreshToken.findAll", query = "SELECT r FROM RefreshToken r")
    , @NamedQuery(name = "RefreshToken.findByJti", query = "SELECT r FROM RefreshToken r WHERE r.jti = :jti")
    , @NamedQuery(name = "RefreshToken.findByTimestamp", query = "SELECT r FROM RefreshToken r WHERE r.timestamp = :timestamp")})
public class RefreshToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "JTI")
    private String jti;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public RefreshToken() {
    }

    public RefreshToken(String jti) {
        this.jti = jti;
    }

    public RefreshToken(String jti, Date timestamp) {
        this.jti = jti;
        this.timestamp = timestamp;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jti != null ? jti.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RefreshToken)) {
            return false;
        }
        RefreshToken other = (RefreshToken) object;
        if ((this.jti == null && other.jti != null) || (this.jti != null && !this.jti.equals(other.jti))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.RefreshToken[ jti=" + jti + " ]";
    }
    
}
