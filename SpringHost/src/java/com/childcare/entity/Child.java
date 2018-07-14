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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "Child")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Child.findAll", query = "SELECT c FROM Child c")
    , @NamedQuery(name = "Child.findByCid", query = "SELECT c FROM Child c WHERE c.cid = :cid")
    , @NamedQuery(name = "Child.findByName", query = "SELECT c FROM Child c WHERE c.name = :name")
    , @NamedQuery(name = "Child.findByImage", query = "SELECT c FROM Child c WHERE c.image = :image")
    , @NamedQuery(name = "Child.findByAge", query = "SELECT c FROM Child c WHERE c.age = :age")
    , @NamedQuery(name = "Child.findByStatus", query = "SELECT c FROM Child c WHERE c.status = :status")})
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CID")
    private Integer cid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "Image")
    private String image;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Age")
    private int age;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @OneToOne(mappedBy = "cid")
    private Device device;
    @JoinColumn(name = "DeviceID", referencedColumnName = "DeviceID")
    @OneToOne
    private Device deviceID;
    @JoinColumn(name = "Creator", referencedColumnName = "UID")
    @ManyToOne(optional = false)
    private Account creator;
    @JoinColumn(name = "FID", referencedColumnName = "fid")
    @ManyToOne(optional = false)
    private Family fid;

    public Child() {
    }

    public Child(Integer cid) {
        this.cid = cid;
    }

    public Child(Integer cid, String name, String image, int age, int status) {
        this.cid = cid;
        this.name = name;
        this.image = image;
        this.age = age;
        this.status = status;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Device deviceID) {
        this.deviceID = deviceID;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Family getFid() {
        return fid;
    }

    public void setFid(Family fid) {
        this.fid = fid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cid != null ? cid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Child)) {
            return false;
        }
        Child other = (Child) object;
        if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.Child[ cid=" + cid + " ]";
    }
    
}
