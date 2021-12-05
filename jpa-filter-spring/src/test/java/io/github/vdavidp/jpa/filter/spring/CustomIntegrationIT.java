package io.github.vdavidp.jpa.filter.spring;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vdavidp.jpa.filter.spring.example.Article;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(CustomConfiguration.class)
public class CustomIntegrationIT {

  @LocalServerPort
  int port;

  RestTemplate restTemplate = new RestTemplate();

  @Test
  void checkCustomizeDefaultConfiguration() throws UnsupportedEncodingException {
    String url = "http://localhost:" + port + "/articles?filter=title:'Article 2' OR active IS TRUE";
    Article[] articles = restTemplate.getForObject(url, Article[].class);
    
    assertEquals(3, articles.length);
  }
}
