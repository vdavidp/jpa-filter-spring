package io.dvp.jpa.filter.db;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import io.dvp.jpa.filter.db.entity.Article;
import io.dvp.jpa.filter.db.entity.Dummy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DatabaseBinderIT {

  @PersistenceContext
  EntityManager entityManager;
  AssertContext<Article>.Verifier<String> ctxArticle;
  AssertContext<Dummy>.Verifier<Long> ctxDummy;

  @BeforeEach
  void init() {
    ctxArticle = new AssertContext<>(entityManager, Article.class).new Verifier<String>(
        Article::getTitle);
    ctxDummy = new AssertContext<>(entityManager, Dummy.class).new Verifier<Long>(Dummy::getId);
  }

  @Test
  void bindEqualsOperatorWithOneExpression() {
    String exp = "{title} = 'Article 1'";
    ctxArticle.assertResultContains(exp, singletonList("Article 1"));
  }

  @Test
  void bindEqualsOperatorWithTwoExpressions() {
    String exp = "{title}={mainTopic}";
    ctxArticle.assertResultContains(exp, singletonList("Article X"));
  }

  @Test
  void bindAndOperator() {
    String exp = "{title} = 'Article 3' And {mainTopic} = 'Love'";
    ctxArticle.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindOrOperator() {
    String exp = "{title} = 'Article 2' Or {title} = 'Article 3'";
    ctxArticle.assertResultContains(exp, asList("Article 2", "Article 3"));
  }

  @Test
  void bindCollectionVariable() {
    String exp = "{comments.author}='david' OR {title}='Article 3'";
    ctxArticle.assertResultContains(exp, asList("Article 1", "Article 3"));
  }

  @Test
  void bindCollectionVariable2() {
    String exp = "{title}='Article 3' And {comments.author}='grace'";
    ctxArticle.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindCollectionVariableMultipleTimes() {
    String exp = "{comments.author}='grace' and {comments.key} = 't3'";
    ctxArticle.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindCollectionAndEntityVariables() {
    String exp = "{comments.author} = 'david' AND {language.name} = 'English'";
    ctxArticle.assertResultContains(exp, singletonList("Article 1"));
  }

  @Test
  void bindIsTrueOperator() {
    String exp = "{booleanValue} is true";
    ctxDummy.assertResultContains(exp, singletonList(1L));
  }

  @Test
  void bindIsFalseOperator() {
    String exp = "{booleanValue} IS FALSE";
    ctxDummy.assertResultContains(exp, asList(2L, 3L));
  }

  @Test
  void bindIsNullOperator() {
    String exp = "{bigDecimalValue} Is Null";
    ctxDummy.assertResultContains(exp, singletonList(3L));
  }

  @Test
  void bindIsNotNullOperator() {
    String exp = "{bigDecimalValue} IS NOT NULL";
    ctxDummy.assertResultContains(exp, asList(1L, 2L));
  }

  @Test
  void bindGreaterThanOperator() {
    String exp = "{starts} > 3";
    ctxArticle.assertResultContains(exp, asList("Article 1", "Article 3"));
  }

  @Test
  void bindGreaterThanOrEqualToOperator() {
    String exp = "{starts} >= 3";
    ctxArticle.assertResultContains(exp, asList("Article 1", "Article 2", "Article 3"));
  }

  @Test
  void bindLessThanOperator() {
    String exp = "{starts} < 4";
    ctxArticle.assertResultContains(exp, asList("Article 2", "Article X"));
  }

  @Test
  void bindLessThanOrEqualOperator() {
    String exp = "{starts} <= 4";
    ctxArticle.assertResultContains(exp, asList("Article 2", "Article X", "Article 1"));
  }
}
