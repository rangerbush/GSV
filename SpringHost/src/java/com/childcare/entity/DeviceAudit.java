/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "DeviceAudit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceAudit.findAll", query = "SELECT d FROM DeviceAudit d")
    , @NamedQuery(name = "DeviceAudit.findByAuditSeq", query = "SELECT d FROM DeviceAudit d WHERE d.auditSeq = :auditSeq")
    , @NamedQuery(name = "DeviceAudit.findByLongitude", query = "SELECT d FROM DeviceAudit d WHERE d.longitude = :longitude")
    , @NamedQuery(name = "DeviceAudit.findByLatitude", query = "SELECT d FROM DeviceAudit d WHERE d.latitude = :latitude")})
public class DeviceAudit implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AuditSeq")
    private Integer auditSeq;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Longitude")
    private BigDecimal longitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @JoinColumn(name = "DeviceID", referencedColumnName = "DeviceID")
    @ManyToOne(optional = false)
    private Device deviceID;

    public DeviceAudit() {
    }

    public DeviceAudit(Integer auditSeq) {
        this.auditSeq = auditSeq;
    }

    public DeviceAudit(Integer auditSeq, BigDecimal longitude, BigDecimal latitude) {
        this.auditSeq = auditSeq;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getAuditSeq() {
        return auditSeq;
    }

    public void setAuditSeq(Integer auditSeq) {
        this.auditSeq = auditSeq;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Device getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Device deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auditSeq != null ? auditSeq.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeviceAudit)) {
            return false;
        }
        DeviceAudit other = (DeviceAudit) object;
        if ((this.auditSeq == null && other.auditSeq != null) || (this.auditSeq != null && !this.auditSeq.equals(other.auditSeq))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.DeviceAudit[ auditSeq=" + auditSeq + " ]";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
