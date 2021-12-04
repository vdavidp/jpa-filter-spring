package io.github.vdavidp.jpa.filter.spring.starter.example;

import io.github.vdavidp.jpa.filter.spring.Filter;
import io.github.vdavidp.jpa.filter.spring.HqlProvider;
import io.github.vdavidp.jpa.filter.spring.SpecificationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
  
  @Autowired
  SpecificationProvider specificationProvider;
  
  @Autowired
  HqlProvider hqlProvider;

  @GetMapping("/articles")
  ResponseEntity<String> findAll(@Filter Specification<Article> specification) {
    if (specification != null) {
      return ResponseEntity.ok("Found specification");
    } else {
      return ResponseEntity.status(500).body("Not found specification");
    }
  }
}
