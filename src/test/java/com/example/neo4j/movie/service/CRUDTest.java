package com.example.neo4j.movie.service;

import com.example.neo4j.movie.dto.MovieDto;
import com.example.neo4j.movie.dto.MovieTitleDirectorDto;
import com.example.neo4j.movie.entity.Movie;
import com.example.neo4j.relationship.Role;
import com.example.neo4j.movie.repository.MovieRepository;
import com.example.neo4j.person.entity.Person;
import com.example.neo4j.person.repository.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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


    @BeforeEach
    public void setUp(){
        personRepository.deleteAll();
        Person 황경하 = personRepository.save(new Person("Hwang", 1995));
        Person 황경하2 = personRepository.save(new Person("Kyeong", 1995));
        //새로운 PersonNode 2개를 집어넣는다.

        Role role = new Role(황경하);
        Role role2 = new Role(황경하2);
        //새로운 관계 2개를 정의한다.

        movieRepository.deleteAll();

        List<Role> a = new ArrayList<>();
        List<Role> b = new ArrayList<>();
        a.add(role);
        b.add(role2);
        //관계 2개를 리스트에 넣는다.

        MovieDto movieDto = new MovieDto(
                "metrix2"
                ,"welcome to real world"
                ,a
                ,b
                ,1997
                ,0L
        );
        //관계 2개와 properties를 가지는 MovieDto가 완성됐다.
        movieService.createOrUpdateMovie(movieDto);
        //완성된 MovieNode를 집어넣는다.
    }


    @Test
    public void BasicSearch(){
        List<Movie> metrix2 = movieRepository.findMovieByTitleLike("metrix2");
        System.out.println("Acted Person = " + metrix2.get(0).getActors().get(0).getPerson().getName());
        System.out.println("Directed Person = " + metrix2.get(0).getDirectors().get(0).getPerson().getName());
        System.out.println("Title  = " + metrix2.get(0).getTitle());
        Assertions.assertThat("metrix2").isEqualTo(metrix2.get(0).getTitle());
    }

    @Test
    public void regexTest(){
        List<Movie> metri = movieRepository.findRegexByTitle("metri");
        System.out.println("metri.size() = " + metri.size());
        System.out.println("metri title = " + metri.get(0).getTitle());
        Assertions.assertThat("metrix2").isEqualTo(metri.get(0).getTitle());
    }

    @Test
    public void relationShipTest(){
        List<Person> acted_in = personRepository.findPersonByRelationship();
        System.out.println("acted_in = " + acted_in.get(0).getName());
    }
    
    @Test
    public void projectionTest(){
        List<MovieTitleDirectorDto> met = movieRepository.findTaglineAndDirectorByTitle("met");
        System.out.println("met.get(0).getDirectorName() = " + met.get(0).getDirectorName());
        System.out.println("met.get(0).getTagline() = " + met.get(0).getTagline());
    }


}