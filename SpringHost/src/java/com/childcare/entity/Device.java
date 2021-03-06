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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "Device")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d")
    , @NamedQuery(name = "Device.findByDeviceID", query = "SELECT d FROM Device d WHERE d.deviceID = :deviceID")
    , @NamedQuery(name = "Device.findByLongitude", query = "SELECT d FROM Device d WHERE d.longitude = :longitude")
    , @NamedQuery(name = "Device.findByLatitude", query = "SELECT d FROM Device d WHERE d.latitude = :latitude")
    , @NamedQuery(name = "Device.findByPulse", query = "SELECT d FROM Device d WHERE d.pulse = :pulse")
    , @NamedQuery(name = "Device.findByStatus", query = "SELECT d FROM Device d WHERE d.status = :status")
    , @NamedQuery(name = "Device.findByTimeStamp", query = "SELECT d FROM Device d WHERE d.timeStamp = :timeStamp")})
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DeviceID")
    private String deviceID;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Longitude")
    private BigDecimal longitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @Column(name = "pulse")
    private Integer pulse;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TimeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
    @JoinColumn(name = "CID", referencedColumnName = "CID")
    @OneToOne
    private Child cid;
    @JoinColumn(name = "FID", referencedColumnName = "fid")
    @ManyToOne(optional = false)
    private Family fid;
    @OneToOne(mappedBy = "deviceID")
    private Child child;

    public Device() {
    }

    public Device(String deviceID) {
        this.deviceID = deviceID;
    }

    public Device(String deviceID, BigDecimal longitude, BigDecimal latitude, int status, Date timeStamp) {
        this.deviceID = deviceID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
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

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Child getCid() {
        return cid;
    }

    public void setCid(Child cid) {
        this.cid = cid;
    }

    public Family getFid() {
        return fid;
    }

    public void setFid(Family fid) {
        this.fid = fid;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceID != null ? deviceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.deviceID == null && other.deviceID != null) || (this.deviceID != null && !this.deviceID.equals(other.deviceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.Device[ deviceID=" + deviceID + " ]";
    }
    
}
