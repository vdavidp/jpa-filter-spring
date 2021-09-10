package io.github.vdavidp.jpa.filter.db;

import io.github.vdavidp.jpa.filter.el.Visitor;
import javax.persistence.criteria.Predicate;

public interface Binder extends Visitor {

  Predicate getPredicate();
}
