package com.example.neo4j.movie.service;

import com.example.neo4j.movie.dto.MovieDto;
import com.example.neo4j.movie.entity.Movie;
import com.example.neo4j.movie.entity.Role;
import com.example.neo4j.movie.repository.MovieRepository;
import com.example.neo4j.person.entity.Person;
import com.example.neo4j.person.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest
public class CRUDTest {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    private static final String PASSWORD = "movies";

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", () -> "neo4j://localhost:7687"); //도커 사용하지 않고 테스트 진행
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> PASSWORD);
        registry.add("spring.data.neo4j.database", () -> "neo4j");
    } //테스트환경에서 connection을 제공한다.


    @Test
    public void BasicRelationInput(){

        personRepository.deleteAll();
        Person 황경하 = personRepository.save(new Person("Hwang", 1995));
        Person 황경하2 = personRepository.save(new Person("Kyeong", 1995));

        Role role = new Role(황경하);
        Role role2 = new Role(황경하2);

        movieRepository.deleteAll();

        List<Role> a = new ArrayList<>();
        List<Role> b = new ArrayList<>();
        a.add(role);
        b.add(role2);

        MovieDto movieDto = new MovieDto(
                "metrix2"
                ,"welcome to real world"
                ,a
                ,b
                ,1997
                ,0L
        );

        Movie result = movieService.createOrUpdateMovie(movieDto);

        List<Movie> metrix2 = movieRepository.findMovieByTitleLike("metrix2");
        System.out.println("Acted Person = " + metrix2.get(0).getActors().get(0).getPerson().getName());
        System.out.println("Directed Person = " + metrix2.get(0).getDirectors().get(0).getPerson().getName());
        System.out.println("Title  = " + metrix2.get(0).getTitle());

        Assertions.assertThat(result.getTitle()).isEqualTo(metrix2.get(0).getTitle());
    }
}
