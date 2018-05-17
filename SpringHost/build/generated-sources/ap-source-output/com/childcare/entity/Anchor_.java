package com.childcare.entity;

import com.childcare.entity.AnchorPK;
import com.childcare.entity.Anchorgroup;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-14T00:39:34")
@StaticMetamodel(Anchor.class)
public class Anchor_ { 

    public static volatile SingularAttribute<Anchor, BigDecimal> latitude;
    public static volatile SingularAttribute<Anchor, AnchorPK> anchorPK;
    public static volatile SingularAttribute<Anchor, BigDecimal> radius;
    public static volatile SingularAttribute<Anchor, Anchorgroup> anchorgroup;
    public static volatile SingularAttribute<Anchor, BigDecimal> longitude;

}