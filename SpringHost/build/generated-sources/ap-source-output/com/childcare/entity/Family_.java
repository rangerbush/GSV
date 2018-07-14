package com.childcare.entity;

import com.childcare.entity.Account;
import com.childcare.entity.AccountFamily;
import com.childcare.entity.Child;
import com.childcare.entity.Device;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(Family.class)
public class Family_ { 

    public static volatile CollectionAttribute<Family, AccountFamily> accountFamilyCollection;
    public static volatile SingularAttribute<Family, Integer> fid;
    public static volatile SingularAttribute<Family, Account> creator;
    public static volatile SingularAttribute<Family, String> familyPassword;
    public static volatile SingularAttribute<Family, String> familyName;
    public static volatile CollectionAttribute<Family, Child> childCollection;
    public static volatile CollectionAttribute<Family, Device> deviceCollection;

}