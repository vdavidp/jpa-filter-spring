package io.dvp.ds.db;

import io.dvp.ds.el.Visitor;

import javax.persistence.criteria.Predicate;

public interface Binder extends Visitor {

    Predicate getPredicate();
}
