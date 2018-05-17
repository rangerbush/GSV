package com.childcare.entity;

import com.childcare.entity.Account;
import com.childcare.entity.AccountFamilyPK;
import com.childcare.entity.Family;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-14T00:39:34")
@StaticMetamodel(AccountFamily.class)
public class AccountFamily_ { 

    public static volatile SingularAttribute<AccountFamily, AccountFamilyPK> accountFamilyPK;
    public static volatile SingularAttribute<AccountFamily, Family> family;
    public static volatile SingularAttribute<AccountFamily, Account> account;
    public static volatile SingularAttribute<AccountFamily, Integer> status;

}