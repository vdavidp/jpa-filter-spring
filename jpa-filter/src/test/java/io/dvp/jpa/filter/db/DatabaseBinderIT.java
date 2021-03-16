package io.dvp.jpa.filter.db;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import io.dvp.jpa.filter.db.entity.Article;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DatabaseBinderIT {
  @PersistenceContext
  EntityManager entityManager;
  AssertContext<Article>.Verifier<String> ctx;

  @BeforeEach
  void init() {
    ctx = new AssertContext<>(entityManager, Article.class).new Verifier<String>(Article::getTitle);
  }

  @Test
  void bindEqualsOperatorWithOneExpression() {
    String exp = "{title} = 'Article 1'";
    ctx.assertResultContains(exp, singletonList("Article 1"));
  }

  @Test
  void bindEqualsOperatorWithTwoExpressions() {
    String exp = "{title}={mainTopic}";
    ctx.assertResultContains(exp, singletonList("Article X"));
  }

  @Test
  void bindAndOperator() {
    String exp = "{title} = 'Article 3' And {mainTopic} = 'Love'";
    ctx.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindOrOperator() {
    String exp = "{title} = 'Article 2' Or {title} = 'Article 3'";
    ctx.assertResultContains(exp, asList("Article 2", "Article 3"));
  }

  @Test
  void bindCollectionVariable() {
    String exp = "{comments.author}='david' OR {title}='Article 3'";
    ctx.assertResultContains(exp, asList("Article 1", "Article 3"));
  }

  @Test
  void bindCollectionVariable2() {
    String exp = "{title}='Article 3' And {comments.author}='grace'";
    ctx.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindCollectionVariableMultipleTimes() {
    String exp = "{comments.author}='grace' and {comments.key} = 't3'";
    ctx.assertResultContains(exp, singletonList("Article 3"));
  }

  @Test
  void bindCollectionAndEntityVariables() {
    String exp = "{comments.author} = 'david' AND {language.name} = 'English'";
    ctx.assertResultContains(exp, singletonList("Article 1"));
  }
}
