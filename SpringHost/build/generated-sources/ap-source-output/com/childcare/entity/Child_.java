package com.childcare.entity;

import com.childcare.entity.Account;
import com.childcare.entity.Device;
import com.childcare.entity.Family;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(Child.class)
public class Child_ { 

    public static volatile SingularAttribute<Child, Family> fid;
    public static volatile SingularAttribute<Child, String> image;
    public static volatile SingularAttribute<Child, Account> creator;
    public static volatile SingularAttribute<Child, String> name;
    public static volatile SingularAttribute<Child, Device> device;
    public static volatile SingularAttribute<Child, Device> deviceID;
    public static volatile SingularAttribute<Child, Integer> age;
    public static volatile SingularAttribute<Child, Integer> cid;
    public static volatile SingularAttribute<Child, Integer> status;

}