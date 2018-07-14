package com.childcare.entity;

import com.childcare.entity.AccountFamily;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Child;
import com.childcare.entity.Contact;
import com.childcare.entity.DeviceToken;
import com.childcare.entity.Family;
import com.childcare.entity.Supervisor;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, String> lastName;
    public static volatile SingularAttribute<Account, Boolean> discard;
    public static volatile CollectionAttribute<Account, DeviceToken> deviceTokenCollection;
    public static volatile CollectionAttribute<Account, Contact> contactCollection;
    public static volatile SingularAttribute<Account, String> avatar;
    public static volatile CollectionAttribute<Account, Anchorgroup> anchorgroupCollection;
    public static volatile CollectionAttribute<Account, AccountFamily> accountFamilyCollection;
    public static volatile SingularAttribute<Account, String> firstName;
    public static volatile SingularAttribute<Account, Long> uid;
    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, String> pin;
    public static volatile SingularAttribute<Account, String> phone;
    public static volatile CollectionAttribute<Account, Child> childCollection;
    public static volatile CollectionAttribute<Account, Supervisor> supervisorCollection;
    public static volatile CollectionAttribute<Account, Family> familyCollection;
    public static volatile SingularAttribute<Account, String> email;

}