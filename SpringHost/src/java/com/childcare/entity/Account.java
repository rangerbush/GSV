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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author New User
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findByUid", query = "SELECT a FROM Account a WHERE a.uid = :uid")
    , @NamedQuery(name = "Account.findByUserName", query = "SELECT a FROM Account a WHERE a.userName = :userName")
    , @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password")
    , @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")
    , @NamedQuery(name = "Account.findByPhone", query = "SELECT a FROM Account a WHERE a.phone = :phone")
    , @NamedQuery(name = "Account.findByDiscard", query = "SELECT a FROM Account a WHERE a.discard = :discard")})
public class Account implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uid")
    private Collection<Contact> contactCollection;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "PIN")
    private String pin;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "FirstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "LastName")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<AccountFamily> accountFamilyCollection;

    @ManyToMany(mappedBy = "accountCollection")
    private Collection<Family> familyCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UID")
     @Min(value=1)
    private Long uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Password")
    private String password;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "Email")
    private String email;
   // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 15)
    @Column(name = "Phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "discard")
    private boolean discard;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uid")
    private Collection<Anchorgroup> anchorgroupCollection;

    public Account() {
    }

    public Account(Long uid) {
        this.uid = uid;
    }

    public Account(Long uid, String password, boolean discard) {
        this.uid = uid;
        this.password = password;
        this.discard = discard;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getDiscard() {
        return isDiscard();
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
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
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.childcare.entity.Account[ uid=" + uid + " ]";
    }

    @XmlTransient
    public Collection<Family> getFamilyCollection() {
        return familyCollection;
    }

    public void setFamilyCollection(Collection<Family> familyCollection) {
        this.familyCollection = familyCollection;
    }

    @XmlTransient
    public Collection<AccountFamily> getAccountFamilyCollection() {
        return accountFamilyCollection;
    }

    public void setAccountFamilyCollection(Collection<AccountFamily> accountFamilyCollection) {
        this.accountFamilyCollection = accountFamilyCollection;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the discard
     */
    public boolean isDiscard() {
        return discard;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @XmlTransient
    public Collection<Contact> getContactCollection() {
        return contactCollection;
    }

    public void setContactCollection(Collection<Contact> contactCollection) {
        this.contactCollection = contactCollection;
    }
    
}
