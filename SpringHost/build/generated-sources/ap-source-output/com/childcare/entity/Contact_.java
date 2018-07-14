package com.childcare.entity;

import com.childcare.entity.Account;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(Contact.class)
public class Contact_ { 

    public static volatile SingularAttribute<Contact, String> firstName;
    public static volatile SingularAttribute<Contact, String> lastName;
    public static volatile SingularAttribute<Contact, Account> uid;
    public static volatile SingularAttribute<Contact, Integer> contactId;
    public static volatile SingularAttribute<Contact, String> phone;
    public static volatile SingularAttribute<Contact, String> email;
    public static volatile SingularAttribute<Contact, Integer> status;

}