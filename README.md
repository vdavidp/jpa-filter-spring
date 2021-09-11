# JPA Filter Spring

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/vdavidp/jpa-filter-spring) [![codecov](https://codecov.io/gh/vdavidp/jpa-filter-spring/branch/master/graph/badge.svg?token=CSCU81AV0H)](https://codecov.io/gh/vdavidp/jpa-filter-spring)

It adds filtering capabilities to spring-data crud repositories. Just add the query param **filter** with a predicate to filter the results. It was designed to be extensible in nature so you can define your own operators and operands.


## Quickstart

1. Import the following artificat into your project.
    ```
    <groupId>io.github.vdavidp</groupId>
    <artifactId>jpa-filter-spring-starter</artifactId>
    <version>0.3.0</version>
    ```
1. Create classes for following structure. Let's suppose *releaseDate* is of type java.util.Date.
    ```json
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

Operators must have a weight between 1 and 99 included. If you are defining a new operator you can use standard weight defined in https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/el/Symbol.java

### Operands
integer, long, float, double, BigDecimal, BigInteger, varchar and variables.

Refer for examples to https://github.com/vdavidp/jpa-filter/blob/master/jpa-filter/src/test/java/com/github/vdavidp/jpa/filter/db/SupportedVariableTypesIT.java

### Operators
The default operators are (case insensitive):
=, and, or, Is True, Is False, Is Null, Is Not Null, Date, >, >=, <, <=

Refer for examples to https://github.com/vdavidp/jpa-filter/blob/master/jpa-filter/src/test/java/com/github/vdavidp/jpa/filter/db/DatabaseBinderIT.java

## Customizations
You can remove, rename existing or add new symbols. In order to do that just expose a bean which implements [ExpressionTreeConfigurator](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/main/java/io/github/vdavidp/jpa/filter/spring/ExpressionTreeConfigurator.java)

### Removing a Symbol
Just remove from the list of symbols and from the map of mappers as follows:
```java
@Override
public void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> mappers) {
  mappers.remove("=");
}

@Override
public void modifySymbols(List<Symbol> symbols) {
  symbols.removeIf(s -> s instanceof BinaryOperator && ((BinaryOperator)s).getSymbol().equals("="));
}
```
### Rename a Symbol
Take a look a this example which at the end is just manipulation of the list and map provided by the interface. https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/test/java/io/github/vdavidp/jpa/filter/spring/CustomConfiguration.java

### Add a new Symbol
To add a new operator you need to follow 3 steps.
1. Create the operator using the [BinaryOperator](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/el/BinaryOperator.java) or [UnaryOperator](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/el/BinaryOperator.java) classes. You will find examples in [ExpressionTree.defaultSymbols](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/el/ExpressionTree.java#L90) method.
1. Create the binder. The following is an example for the ":" (equals) operator . For binary operators, the first removed object from deque is the right side, the next one is the left side. You will find plenty of examples in class [Mappers](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter/src/main/java/io/github/vdavidp/jpa/filter/db/Mappers.java)

    ```java
    class MyBinders
        public static BiFunction<Deque<Object>, CriteriaBuilder, Object> equalTo() {
            return (deque, cb) -> {
              Object right = deque.removeFirst();
              Expression left = (Expression) deque.removeFirst();
              if (right instanceof Expression) {
                return cb.equal(left, (Expression) right);
              } else {
                return cb.equal(left, right);
              }
            };
        }
    }
    ```
1. Finally you need to implement the [ExpressionTreeConfigurator](https://github.com/vdavidp/jpa-filter-spring/blob/master/jpa-filter-spring/src/main/java/io/github/vdavidp/jpa/filter/spring/ExpressionTreeConfigurator.java) interface and register your new operator.
    
    ```
    @Configuration
    public class CustomConfiguration {

      @Bean
      ExpressionTreeConfigurator configurator() {
        return new ExpTreeConfiguration();
      }
    }
    
    class ExpTreeConfiguration implements ExpressionTreeConfigurator {
      @Override
      public void modifyMappers(Map<String, BiFunction<Deque<Object>, CriteriaBuilder, Object>> mappers) {
        mappers.put(":", MyBinders.equalTo());
      }

      @Override
      public void modifySymbols(List<Symbol> symbols) {
        symbols.add(new BinaryOperator(":", Symbol.WEIGHT_20));
      }
    }
    ```
