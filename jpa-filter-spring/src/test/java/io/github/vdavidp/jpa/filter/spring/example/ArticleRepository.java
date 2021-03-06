package io.github.vdavidp.jpa.filter.spring.example;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long>,
    JpaSpecificationExecutor<Article> {

}
