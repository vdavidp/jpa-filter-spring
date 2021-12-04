package io.github.vdavidp.jpa.filter.spring.example;

import io.github.vdavidp.jpa.filter.spring.Filter;
import io.github.vdavidp.jpa.filter.spring.HqlProvider;
import io.github.vdavidp.jpa.filter.spring.SpecificationProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

  @Autowired
  private ArticleRepository articleRepository;

  @GetMapping("/articles")
  List<Article> findAll(@Filter Specification<Article> specification) {
    return articleRepository.findAll(specification);
  }
  
  @GetMapping("/articles2")
  List<Article> findAll2(@Filter(queryParam = "search") Specification<Article> specification) {
    return articleRepository.findAll(specification);
  }
}