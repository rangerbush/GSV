package com.childcare.entity;

import com.childcare.entity.ActionTaken;
import com.childcare.entity.Anchorgroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T18:20:53")
@StaticMetamodel(ActionSet.class)
public class ActionSet_ { 

    public static volatile SingularAttribute<ActionSet, Boolean> discard;
    public static volatile CollectionAttribute<ActionSet, ActionTaken> actionTakenCollection;
    public static volatile CollectionAttribute<ActionSet, Anchorgroup> anchorgroupCollection;
    public static volatile SingularAttribute<ActionSet, Integer> aid;
    public static volatile SingularAttribute<ActionSet, String> desc;

}