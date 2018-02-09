/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "anchor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anchor.findAll", query = "SELECT a FROM Anchor a")
    , @NamedQuery(name = "Anchor.findByGid", query = "SELECT a FROM Anchor a WHERE a.anchorPK.gid = :gid")
    , @NamedQuery(name = "Anchor.findBySeqID", query = "SELECT a FROM Anchor a WHERE a.anchorPK.seqID = :seqID")
    , @NamedQuery(name = "Anchor.findByLongitude", query = "SELECT a FROM Anchor a WHERE a.longitude = :longitude")
    , @NamedQuery(name = "Anchor.findByLatitude", query = "SELECT a FROM Anchor a WHERE a.latitude = :latitude")
    , @NamedQuery(name = "Anchor.findByRadius", query = "SELECT a FROM Anchor a WHERE a.radius = :radius")})
public class Anchor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnchorPK anchorPK;
    @Max(value=180)  @Min(value=-180)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Longitude")
    private BigDecimal longitude;
    @Max(value=90)  @Min(value=-90)
    @Basic(optional = false)
    @NotNull
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @Max(value=(long) 999999.9)  @Min(value=0)
    @Basic(optional = false)
    @NotNull
    @Column(name = "Radius")
    private BigDecimal radius;
    @JoinColumn(name = "GID", referencedColumnName = "GID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Anchorgroup anchorgroup;

    public Anchor() {
    }

    public Anchor(AnchorPK anchorPK) {
        this.anchorPK = anchorPK;
    }

    public Anchor(AnchorPK anchorPK, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        this.anchorPK = anchorPK;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public Anchor(int gid, int seqID) {
        this.anchorPK = new AnchorPK(gid, seqID);
    }

    public AnchorPK getAnchorPK() {
        return anchorPK;
    }

    public void setAnchorPK(AnchorPK anchorPK) {
        this.anchorPK = anchorPK;
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

    public BigDecimal getRadius() {
        return radius;
    }

    public void setRadius(BigDecimal radius) {
        this.radius = radius;
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
        hash += (anchorPK != null ? anchorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anchor)) {
            return false;
        }
        Anchor other = (Anchor) object;
        if ((this.anchorPK == null && other.anchorPK != null) || (this.anchorPK != null && !this.anchorPK.equals(other.anchorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.Anchor[ anchorPK=" + anchorPK + " ]";
    }
    
}
