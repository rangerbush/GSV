package com.childcare.entity;

import com.childcare.entity.Device;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-05T19:28:43")
@StaticMetamodel(DeviceAudit.class)
public class DeviceAudit_ { 

    public static volatile SingularAttribute<DeviceAudit, Date> date;
    public static volatile SingularAttribute<DeviceAudit, BigDecimal> latitude;
    public static volatile SingularAttribute<DeviceAudit, Integer> auditSeq;
    public static volatile SingularAttribute<DeviceAudit, Device> deviceID;
    public static volatile SingularAttribute<DeviceAudit, BigDecimal> longitude;

}