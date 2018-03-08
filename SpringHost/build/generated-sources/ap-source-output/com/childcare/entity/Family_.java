package com.childcare.entity;

import com.childcare.entity.Account;
import com.childcare.entity.Anchorgroup;
import com.childcare.entity.Device;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-05T19:28:43")
@StaticMetamodel(Family.class)
public class Family_ { 

    public static volatile SingularAttribute<Family, Integer> fid;
    public static volatile CollectionAttribute<Family, Account> accountCollection;
    public static volatile SingularAttribute<Family, String> familyPassword;
    public static volatile SingularAttribute<Family, Integer> maxCluster;
    public static volatile SingularAttribute<Family, String> familyName;
    public static volatile CollectionAttribute<Family, Anchorgroup> anchorgroupCollection;
    public static volatile CollectionAttribute<Family, Device> deviceCollection;

}