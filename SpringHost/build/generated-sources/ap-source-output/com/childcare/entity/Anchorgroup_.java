package com.childcare.entity;

import com.childcare.entity.Child;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(Anchorgroup.class)
public class Anchorgroup_ { 

    public static volatile SingularAttribute<Anchorgroup, String> groupName;
    public static volatile SingularAttribute<Anchorgroup, Integer> gid;
    public static volatile SingularAttribute<Anchorgroup, Integer> maxSeq;
    public static volatile SingularAttribute<Anchorgroup, Integer> type;
    public static volatile SingularAttribute<Anchorgroup, Child> cid;

}