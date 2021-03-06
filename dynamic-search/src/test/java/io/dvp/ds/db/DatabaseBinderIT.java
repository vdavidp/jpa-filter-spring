package io.dvp.ds.db;

import io.dvp.ds.el.ExpressionTree;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.*;
import java.util.function.BiFunction;

import static io.dvp.ds.el.ExpressionTree.defaultSymbols;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DatabaseBinderIT {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void bindOperatorsWithDifferentWeight() {
        String exp = "{title} = 'Article 2' or {title} = 'Article 3'";
        ExpressionTree tree = ExpressionTree.build(exp, defaultSymbols());

        CriteriaQuery<Article> cq = entityManager.getCriteriaBuilder().createQuery(Article.class);
        Root<Article> root = cq.from(Article.class);

        Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Predicate>> mappers = new HashMap<>();
        mappers.put("=", Mappers.equalTo());
        mappers.put("or", Mappers.or());

        DatabaseBinder<Article> binder = new DatabaseBinder<>(
                root, entityManager.getCriteriaBuilder(), mappers);
        tree.getRoot().visit(binder);

        cq.select(root).where(binder.getPredicate());
        List<Article> articles = entityManager.createQuery(cq).getResultList();

        List<String> titles = articles.stream().map(Article::getTitle).collect(toList());
        assertEquals(2, articles.size());
        assertTrue(titles.containsAll(asList("Article 2", "Article 3")));
    }
}
