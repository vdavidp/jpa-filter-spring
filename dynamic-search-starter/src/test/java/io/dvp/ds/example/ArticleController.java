package io.dvp.ds.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {

    @GetMapping("/articles")
    ResponseEntity<String> findAll(Specification<Article> specification) {
        if (specification != null) {
            return ResponseEntity.ok("Found specification");
        } else {
            return ResponseEntity.status(500).body("Not found specification");
        }
    }
}
