package io.dvp.jpa.filter;

import io.dvp.jpa.filter.example.Article;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringIntegrationIT {

    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    void checkAutoconfigurationInstallation() throws UnsupportedEncodingException {
        String filter = URLEncoder.encode("{title}='Article 1' or {title}='Article 3'", "UTF-8");
        String url = "http://localhost:" + port + "/articles?filter=" + filter;
        Article[] articles = restTemplate.getForObject(url, Article[].class);

        assertNotNull(articles);
        assertEquals(2, articles.length);

        List<String> titles = Arrays.stream(articles).map(Article::getTitle).collect(toList());
        assertTrue(titles.containsAll(asList("Article 1", "Article 3")));
    }
}
