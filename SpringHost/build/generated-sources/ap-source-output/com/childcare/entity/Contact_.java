package com.childcare.entity;

import com.childcare.entity.Family;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-14T00:39:34")
@StaticMetamodel(Contact.class)
public class Contact_ { 

    public static volatile SingularAttribute<Contact, Family> fid;
    public static volatile SingularAttribute<Contact, String> firstName;
    public static volatile SingularAttribute<Contact, String> lastName;
    public static volatile SingularAttribute<Contact, Integer> contactId;
    public static volatile SingularAttribute<Contact, String> phone;
    public static volatile SingularAttribute<Contact, String> email;

}