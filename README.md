# JPA Filter

[![CircleCI](https://circleci.com/gh/circleci/circleci-docs.svg?style=shield)](https://circleci.com/gh/vdavidp/jpa-filter) [![codecov](https://codecov.io/gh/vdavidp/jpa-filter/branch/master/graph/badge.svg?token=CSCU81AV0H)](https://codecov.io/gh/vdavidp/jpa-filter)

It adds filtering capabilities to spring-data crud repositories. Just add the query param **filter** with a predicate to filter the results. 

Additionally you can manually parse and execute expressions.

## Quickstart

Import the following artificat into your project.

```
<groupId>com.github.vdavidp</groupId>
<artifactId>jpa-filter-spring-starter</artifactId>
<version>0.1.0</version>
```
Make a request including the filter param query. The value must be encoded.

http://host/persons?filter=%7BfullName%7D+%3D+%27John+Wick%27+and+%7Bbirthday%7D+%3D+Date+%271984-07-04%27

The filter param query decoded is: 
{fullName} = 'John Wick' and {birthday} = Date '1984-07-04'
