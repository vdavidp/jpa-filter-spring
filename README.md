# JPA Filter Spring

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/vdavidp/jpa-filter-spring) [![codecov](https://codecov.io/gh/vdavidp/jpa-filter-spring/branch/master/graph/badge.svg?token=CSCU81AV0H)](https://codecov.io/gh/vdavidp/jpa-filter-spring) [![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

It adds filtering capabilities to spring-data crud repositories. Just add the query param **filter** with a predicate to filter the results. 

The library was designed to be extensible in nature. You can define your own operators and operands. Manually creation of predicate is possible for spring specifications and raw HQL queries. You can go even further and use the ExpressionTree component with a custom Visitor for your own purposes.

Starting at version 0.12.0 it only works with java 17 and spring boot 3.


## Quickstart

1. Import the following artificat into your project.
    ```
    <groupId>io.github.vdavidp</groupId>
    <artifactId>jpa-filter-spring-starter</artifactId>
    <version>0.12.0</version>
    ```
1. Create classes for following structure.
    ```json
      {
        "title": "Article 1",
        "author": "John",
        "comments": [
          {
            "text": "Interesting",
            "author": "Person 1"
          }
        ]
      }
    ```
1. Define a repository
    ```java
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
    import org.springframework.data.repository.CrudRepository;

    public interface ArticleRepository extends CrudRepository<Article, Long>,
        JpaSpecificationExecutor<Article> {

    }
    ```
1. In a controller put:
    ```java
    import org.springframework.data.jpa.domain.Specification;
    import io.github.vdavidp.jpa.filter.spring.Filter;

    @GetMapping("/articles")
    List<Article> findAll(@Filter Specification<Article> specification) {
      return articleRepository.findAll(specification);
    }
    ```
1. Make a request including a **filter** query param, e.g.
    ```
    http://host/articles?filter=author:'John' OR comments.author:'Person 1'
    ```
  
### Manual creation of predicate
Sometimes you may need to pre process the provided filter and after that then create the predicate. You can do it for both [specification](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/SpecificationProviderIT.java) and [HQL](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/HqlProviderIT.java) predicates

## Default Configuration

By default it is installed some operators and operands. You can see an usage example [here](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/DefaultWebIntegrationIT.java).

Following operands are supported:
* real numbers e.g. 123456699393848437273
* decimals e.g. 12.33221
* strings e.g. 'didid', 'That\\'s', 'single slash \\\\'
* uuids e.g. 697833f8-a808-45c6-b339-b632065189e6
* variables e.g. abc, abc123, abc.abc

Following operators are supported:
* : equals, e.g. ?filter=abc:23
* ! not equals, e.g. ?filter=abc!23
* < less than, e.g. ?filter=abc<43
* \> greater than, e.g. ?filter=abc>33
* AND e.g. ?filter=abc:33 AND cde:'kdi'
* OR e.g. ?filter=name:'one' OR name:'two'

Of course parentheses can be used to force order. For example, these 2 predicates are different:

* ?filter=role:'Boss' OR age>21 AND  age<100
* ?filter=role:'Boss' OR (age>21 AND age<100)

## Custom Configuration
Does your project require some operator or operand the library is missing? Here you can find an example how to provide your own [configuration](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/CustomConfiguration.java) for both specifications and HQL. This [test](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/CustomWebIntegrationIT.java) shows how it looks like to use it.

## Raw Usage
If needed you can create an [expression tree](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/test/java/io/github/vdavidp/jpa/filter/el/ExpressionTreeTest.java) from a filter and traverse the tree with a custom [Visitor](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/el/Visitor.java). For example, the [FieldExistingVerifier](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/main/java/io/github/vdavidp/jpa/filter/spring/visitor/FieldExistingVerifier.java) visitor is used to validate a field name exists in the target entity of the filter. Another simple visitor is the [HqlBinder](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/db/HqlBinder.java).
