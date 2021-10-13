package io.github.vdavidp.jpa.filter.spring;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vdavidp.jpa.filter.spring.example.Article;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultIntegrationIT {

  @LocalServerPort
  int port;

  RestTemplate restTemplate = new RestTemplate();

  @Test
  void checkAutoconfigurationInstallation() throws UnsupportedEncodingException {
    String filter = "title:'Article 1' OR title:'Article 3'";
    String url = "http://localhost:" + port + "/articles?filter=" + filter;
    Article[] articles = restTemplate.getForObject(url, Article[].class);

    assertNotNull(articles);
    assertEquals(2, articles.length);

    List<String> titles = Arrays.stream(articles).map(Article::getTitle).collect(toList());
    assertTrue(titles.containsAll(asList("Article 1", "Article 3")));
  }
  
  @Test
  void useAnotherQueryParamName() {
    String filter = "title:'Article 1'";
    String url = "http://localhost:" + port + "/articles2?search=" + filter;
    Article[] articles = restTemplate.getForObject(url, Article[].class);
    
    assertEquals(1, articles.length);
  }
}
