package io.github.vdavidp.jpa.filter.db;

import io.github.vdavidp.jpa.filter.el.Defaults;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vdavidp.jpa.filter.el.ExpressionTree;
import java.util.List;
import java.util.function.Function;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import static io.github.vdavidp.jpa.filter.db.Mappers.defaultCriteriaMappers;

public class AssertContext<T> {
  private final EntityManager entityManager;
  private final CriteriaQuery<T> criteriaQuery;
  private final Root<T> root;
  private final CriteriaBinder<T> binder;

  public AssertContext(EntityManager entityManager, Class<T> clazz) {
    this.entityManager = entityManager;
    criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
    root = criteriaQuery.from(clazz);
    binder = new CriteriaBinder<>(
        root, criteriaQuery, entityManager.getCriteriaBuilder(), defaultCriteriaMappers());
  }

  public List<T> executeQuery(String exp) {
    ExpressionTree tree = new ExpressionTree(exp, Defaults.operands(), Defaults.operators());
    tree.visit(binder);

    criteriaQuery.select(root).where(binder.getPredicate());
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @RequiredArgsConstructor
  class Verifier<R> {
    private final Function<T, R> resultProjection;

    void assertResultContains(String exp, List<R> results) {
      List<T> result = executeQuery(exp);
      List<R> projectedResult = result.stream().map(resultProjection).collect(toList());
      assertEquals(projectedResult.size(), results.size());
      assertTrue(projectedResult.containsAll(results));
    }
  }
}
