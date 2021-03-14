package io.dvp.jpa.filter.spring.starter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StarterIT {

  @LocalServerPort
  int port;

  RestTemplate restTemplate = new RestTemplate();

  @Test
  void checkAutoconfigurationStarter() throws UnsupportedEncodingException {
    String filter = URLEncoder.encode("{title}='Article 1' or {title}='Article 3'", "UTF-8");
    String url = "http://localhost:" + port + "/articles?filter=" + filter;
    ResponseEntity<String> msg = restTemplate.getForEntity(url, String.class);

    assertEquals(200, msg.getStatusCodeValue());
  }
}
