package com.childcare.entity;

import com.childcare.entity.AccountFamily;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Family;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-14T00:39:34")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile CollectionAttribute<Account, AccountFamily> accountFamilyCollection;
    public static volatile SingularAttribute<Account, String> firstName;
    public static volatile SingularAttribute<Account, String> lastName;
    public static volatile SingularAttribute<Account, Long> uid;
    public static volatile SingularAttribute<Account, Boolean> discard;
    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, String> pin;
    public static volatile SingularAttribute<Account, String> phone;
    public static volatile CollectionAttribute<Account, Anchorgroup> anchorgroupCollection;
    public static volatile CollectionAttribute<Account, Family> familyCollection;
    public static volatile SingularAttribute<Account, String> email;

}