# JPA Filter Spring

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/vdavidp/jpa-filter-spring) [![codecov](https://codecov.io/gh/vdavidp/jpa-filter-spring/branch/master/graph/badge.svg?token=CSCU81AV0H)](https://codecov.io/gh/vdavidp/jpa-filter-spring)

It adds filtering capabilities to spring-data crud repositories. Just add the query param **filter** with a predicate to filter the results. It was designed to be extensible in nature so you can define your own operators and operands.


## Quickstart

1. Import the following artificat into your project.
    ```
    <groupId>io.github.vdavidp</groupId>
    <artifactId>jpa-filter-spring-starter</artifactId>
    <version>0.9.0</version>
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
          },
          {
            "text": "Needs more examples",
            "author": "Person 2"
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


## Default Symbols
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

## Customizations
TODO
