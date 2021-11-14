package com.example.neo4j.movie.service;


import com.example.neo4j.movie.dto.CastMemberDto;
import com.example.neo4j.movie.dto.MovieDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class MovieServiceTest {

    private static final String PASSWORD = "movies";

    @Autowired
    private MovieService movieService;

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", () -> "neo4j://localhost:7687"); //도커 사용하지 않고 테스트 진행
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> PASSWORD);
        registry.add("spring.data.neo4j.database", () -> "neo4j");
    }

    @BeforeEach
    void setup(@Autowired Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (n) DETACH DELETE n");
                tx.run(""
                        + "CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})\n"
                        + "CREATE (TheMatrixReloaded:Movie {title:'The Matrix Reloaded', released:2003, tagline:'Free your mind'})\n"
                        + "CREATE (TheMatrixRevolutions:Movie {title:'The Matrix Revolutions', released:2003, tagline:'Everything that has a beginning has an end'})\n"
                        + "CREATE (p:Person {name: 'Keanu Reeves', born: 1964}) -[:ACTED_IN {roles: ['Neo']}]-> (TheMatrix)\n");
                return null;
            });
        }
    }

    @Test
    void createByDriver(@Autowired Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (n) DETACH DELETE n"); //노드 전부 제거
                tx.run(""
                        + "CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})");
                return null;
            });
        }
    }

    @Test
    void deleteByDriver(@Autowired Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (n) DETACH DELETE n"); //대상을 제거한다.
                return null;
            });
        }
    }

    @Test
    public void searches_movies_by_title() {
        String title = "Matrix Re";
        assertThat(movieService.searchMoviesByTitle(title))
                .hasSize(2)
                .extracting(mr -> mr.getMovie().getTitle()).containsExactlyInAnyOrder("The Matrix Reloaded", "The Matrix Revolutions");
    }

    @Test
    public void searches_movies_by_title2() {
        String title = "Matrix Re";
        assertThat(movieService.searchMoviesByTitle2(title))
                .hasSize(2)
                .extracting(mr -> mr.getTitle()).containsExactlyInAnyOrder("The Matrix Reloaded", "The Matrix Revolutions");
    }

    @Test
    public void fetches_movie_details() {
        MovieDetailsDto details = movieService.fetchDetailsByTitle("The Matrix");

        assertThat(details.getTitle()).isEqualTo("The Matrix");
        assertThat(details.getCast()).containsExactly(new CastMemberDto("Keanu Reeves", "acted", "Neo"));
    }

    @Test
    public void fetches_d3_graph(@Autowired MovieService service) {
        Map<String, List<Object>> d3Graph = service.fetchMovieGraph();

        for (String key : d3Graph.keySet()) {
            var cur = d3Graph.get(key);
            for (Object i : cur) {
                System.out.println("i = " + i);
            }
        }
        assertThat(d3Graph).isEqualTo(
                Map.of(
                        "links", List.of(
                                Map.of("source", 1, "target", 0)
                        ),
                        "nodes", List.of(
                                Map.of("label", "movie", "title", "The Matrix"),
                                Map.of("label", "actor", "title", "Keanu Reeves")
                        )
                ));
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}