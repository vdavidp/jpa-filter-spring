# JPA Filter Spring

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/vdavidp/jpa-filter-spring) [![codecov](https://codecov.io/gh/vdavidp/jpa-filter-spring/branch/master/graph/badge.svg?token=CSCU81AV0H)](https://codecov.io/gh/vdavidp/jpa-filter-spring)

It adds filtering capabilities to spring-data crud repositories. Just add the query param **filter** with a predicate to filter the results. It was designed to be extensible in nature so you can define your own operators and operands.

Additionally you can manually parse and execute predicates. Below you will find instructions on doing that.

## Quickstart

1. Import the following artificat into your project.
    ```
    <groupId>io.github.vdavidp</groupId>
    <artifactId>jpa-filter-spring-starter</artifactId>
    <version>...</version>
    ```
1. Create classes for following structure.
    ```json
    [
      {
        "title": "Article 1",
        "releasedDate": "2021-10-01",
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
      },
      {
        "title": "Article 2",
        "releasedDate": "2021-10-01",
        "comments": [
          {
            "text": "Go for it",
            "author": "Person 1"
          }
        ]
      }
    ]
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

    @GetMapping("/articles")
    List<Article> findAll(Specification<Article> specification) {
      return articleRepository.findAll(specification);
    }
    ```
1. Make a request including a **filter** query param. The value must be encoded e.g.
    http://host/articles?filter=%7BreleasedDate%7D%20%3D%20Date%20%272021-10-01%27%20and%20%7Bcomments.author%7D%20%3D%20%27Person%201%27

    The filter query param decoded is: 
    ```
    {releasedDate} = Date '2021-10-01' and {comments.author} = 'Person 1'
    ```

## Constraints
For binary operators, the left side needs to be an expression, right side can be an expression or constant e.g.

Not Valid:
```
'John Wick' = {fullName}
5.34 >= {amount}
```

Valid:
```
{fullName} = 'John Wick'
{amount} <= 5.34
```

## Default Symbols
Precedence of operators is same as defined for java. https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html

### Operands
integer, long, float, double, BigDecimal, BigInteger, varchar and variables.

Refer for examples to https://github.com/vdavidp/jpa-filter/blob/master/jpa-filter/src/test/java/com/github/vdavidp/jpa/filter/db/SupportedVariableTypesIT.java

### Operators
The default operators are (case insensitive):
=, and, or, Is True, Is False, Is Null, Is Not Null, Date, >, >=, <, <=

Refer for examples to https://github.com/vdavidp/jpa-filter/blob/master/jpa-filter/src/test/java/com/github/vdavidp/jpa/filter/db/DatabaseBinderIT.java

## Customizations
TODO
