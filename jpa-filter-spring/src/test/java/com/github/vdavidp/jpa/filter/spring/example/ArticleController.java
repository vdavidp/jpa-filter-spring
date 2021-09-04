package com.github.vdavidp.jpa.filter.spring.example;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

  @Autowired
  private ArticleRepository articleRepository;

  @GetMapping("/articles")
  List<Article> findAll(Specification<Article> specification) {
    return articleRepository.findAll(specification);
  }
}
