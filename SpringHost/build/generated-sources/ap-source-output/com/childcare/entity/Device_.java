package com.childcare.entity;

import com.childcare.entity.Child;
import com.childcare.entity.DeviceAudit;
import com.childcare.entity.Family;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-14T00:39:34")
@StaticMetamodel(Device.class)
public class Device_ { 

    public static volatile SingularAttribute<Device, Date> timeStamp;
    public static volatile SingularAttribute<Device, Family> fid;
    public static volatile SingularAttribute<Device, Integer> cluster;
    public static volatile SingularAttribute<Device, BigDecimal> latitude;
    public static volatile SingularAttribute<Device, Integer> pulse;
    public static volatile SingularAttribute<Device, String> deviceID;
    public static volatile SingularAttribute<Device, Child> child;
    public static volatile SingularAttribute<Device, Integer> status;
    public static volatile CollectionAttribute<Device, DeviceAudit> deviceAuditCollection;
    public static volatile SingularAttribute<Device, BigDecimal> longitude;

}