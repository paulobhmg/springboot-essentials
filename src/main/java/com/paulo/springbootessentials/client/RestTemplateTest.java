package com.paulo.springbootessentials.client;

import com.paulo.springbootessentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class RestTemplateTest {
    public static final String URI = "http://localhost:8080";

    public static void main(String[] args) {
        log.info("ResponseEntity by RestTemplate.getForEntity()");
        ResponseEntity<Anime> responseEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 8);
        log.info(responseEntity);
        log.info("---------------------------------------------");

        log.info("Anime Object by RestTemplate.getForObject()");
        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 50);
        log.info(object);
        log.info("---------------------------------------------");

        log.info("Anime array by RestTemplate.getForObject()");
        Anime[] objects = new RestTemplate().getForObject("http://localhost:8080/animes/list", Anime[].class);
        log.info(Arrays.toString(objects));
        log.info("---------------------------------------------");

        log.info("Anime List by RestTemplate.exchange(), using ParameterizedTYpeReference: ");
        //SupertypeToken
        ResponseEntity<List<Anime>> response = new RestTemplate().exchange("http://localhost:8080/animes/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });
        log.info(response);
        log.info(response.getBody());
        log.info("---------------------------------------------");

       /* log.info("Anime Object by RestTemplate.postForObject()");
        Anime berserk = Anime.builder().name("berserk").build();
        Anime anime = new RestTemplate().postForObject("http://localhost:8080/animes", berserk, Anime.class);
        log.info(anime);
        log.info("---------------------------------------------");*/


        Anime samurai = Anime.builder().name("Samuray").build();
        ResponseEntity<Anime> exchange = new RestTemplate().exchange(
                "http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>((samurai), createJsonHeaders()), Anime.class);
        log.info(exchange);
        log.info("---------------------------------------------");

        Anime animeToBeUpdated = exchange.getBody();
        animeToBeUpdated.setName("Edited name.0");
        ResponseEntity<Void> responseUpdate = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<Anime>(animeToBeUpdated, createJsonHeaders()),
                Void.class);
        log.info(responseUpdate);
        log.info("---------------------------------------------");

        Anime animeToBeDeleted = exchange.getBody();
        ResponseEntity<Void> responseDelete = new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeToBeDeleted.getId());
    }

    private static String getUri(String path) {
        return String.format("%s%s", URI, path);
    }

    private static HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
