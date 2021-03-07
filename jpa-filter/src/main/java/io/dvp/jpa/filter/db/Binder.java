package io.dvp.jpa.filter.db;

import io.dvp.jpa.filter.el.Visitor;

import javax.persistence.criteria.Predicate;

public interface Binder extends Visitor {

    Predicate getPredicate();
}
