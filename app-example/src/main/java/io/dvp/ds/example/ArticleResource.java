package io.dvp.ds.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticleResource {

    @GetMapping("/articles")
    List<Article> findArticles() {
        return new ArrayList<>();
    }
}
