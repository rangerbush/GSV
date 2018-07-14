/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "device_token")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeviceToken.findAll", query = "SELECT d FROM DeviceToken d")
    , @NamedQuery(name = "DeviceToken.findByDeviceId", query = "SELECT d FROM DeviceToken d WHERE d.deviceId = :deviceId")})
public class DeviceToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "device_id")
    private String deviceId;
    @JoinColumn(name = "uid", referencedColumnName = "UID")
    @ManyToOne(optional = false)
    private Account uid;

    public DeviceToken() {
    }

    public DeviceToken(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Account getUid() {
        return uid;
    }

    public void setUid(Account uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deviceId != null ? deviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeviceToken)) {
            return false;
        }
        DeviceToken other = (DeviceToken) object;
        if ((this.deviceId == null && other.deviceId != null) || (this.deviceId != null && !this.deviceId.equals(other.deviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.DeviceToken[ deviceId=" + deviceId + " ]";
    }
    
}
