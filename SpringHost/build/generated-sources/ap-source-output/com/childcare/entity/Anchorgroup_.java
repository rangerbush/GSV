package com.childcare.entity;

import com.childcare.entity.Account;
import com.childcare.entity.ActionSet;
import com.childcare.entity.Anchor;
import com.childcare.entity.Family;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-05T19:28:43")
@StaticMetamodel(Anchorgroup.class)
public class Anchorgroup_ { 

    public static volatile SingularAttribute<Anchorgroup, BigDecimal> inLat;
    public static volatile SingularAttribute<Anchorgroup, Family> fid;
    public static volatile SingularAttribute<Anchorgroup, Integer> cluster;
    public static volatile CollectionAttribute<Anchorgroup, Anchor> anchorCollection;
    public static volatile SingularAttribute<Anchorgroup, Long> gid;
    public static volatile CollectionAttribute<Anchorgroup, ActionSet> actionSetCollection;
    public static volatile SingularAttribute<Anchorgroup, BigDecimal> exLat;
    public static volatile SingularAttribute<Anchorgroup, BigDecimal> inRadius;
    public static volatile SingularAttribute<Anchorgroup, Integer> type;
    public static volatile SingularAttribute<Anchorgroup, BigDecimal> exRadius;
    public static volatile SingularAttribute<Anchorgroup, Account> uid;
    public static volatile SingularAttribute<Anchorgroup, String> groupName;
    public static volatile SingularAttribute<Anchorgroup, Integer> maxSeq;
    public static volatile SingularAttribute<Anchorgroup, BigDecimal> exLong;
    public static volatile SingularAttribute<Anchorgroup, BigDecimal> inLong;

}