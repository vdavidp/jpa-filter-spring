package io.dvp.jpa.filter.db;

import static io.dvp.jpa.filter.db.Mappers.defaultMappers;
import static io.dvp.jpa.filter.el.ExpressionTree.defaultSymbols;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.dvp.jpa.filter.db.entity.Article;
import io.dvp.jpa.filter.el.ExpressionTree;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DatabaseBinderIT {

  @PersistenceContext
  EntityManager entityManager;

  CriteriaQuery<Article> criteriaQuery;
  Root<Article> root;
  DatabaseBinder<Article> binder;

  @BeforeEach
  void init() {
    criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Article.class);
    root = criteriaQuery.from(Article.class);
    binder = new DatabaseBinder<>(
        root, criteriaQuery, entityManager.getCriteriaBuilder(), defaultMappers());
  }

  @Test
  void bindEqualsOperatorWithOneExpression() {
    String exp = "{title} = 'Article 1'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article 1"));
  }

  static void assertContainsTitles(List<Article> articles, List<String> titles) {
    List<String> articleTitles = articles.stream().map(Article::getTitle).collect(toList());
    assertEquals(titles.size(), articles.size());
    assertTrue(articleTitles.containsAll(titles));
  }

  List<Article> executeQuery(String exp) {
    ExpressionTree tree = ExpressionTree.build(exp, defaultSymbols());
    tree.getRoot().visit(binder);

    criteriaQuery.select(root).where(binder.getPredicate());
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @Test
  void bindEqualsOperatorWithTwoExpressions() {
    String exp = "{title}={mainTopic}";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article X"));
  }

  @Test
  void bindAndOperator() {
    String exp = "{title} = 'Article 3' and {mainTopic} = 'Love'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article 3"));
  }

  @Test
  void bindOrOperator() {
    String exp = "{title} = 'Article 2' or {title} = 'Article 3'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, asList("Article 2", "Article 3"));
  }

  @Test
  void bindCollectionVariable() {
    String exp = "{comments.author}='david' or {title}='Article 3'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, asList("Article 1", "Article 3"));
  }

  @Test
  void bindCollectionVariable2() {
    String exp = "{title}='Article 3' and {comments.author}='grace'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article 3"));
  }

  @Test
  void bindCollectionVariableMultipleTimes() {
    String exp = "{comments.author}='grace' and {comments.key} = 't3'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article 3"));
  }

  @Test
  void bindCollectionAndEntityVariables() {
    String exp = "{comments.author} = 'david' and {language.name} = 'English'";
    List<Article> articles = executeQuery(exp);
    assertContainsTitles(articles, singletonList("Article 1"));
  }
}
