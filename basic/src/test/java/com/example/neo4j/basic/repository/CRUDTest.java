package com.example.neo4j.basic.repository;

import com.example.neo4j.dto.movie.MovieDto;
import com.example.neo4j.dto.movie.MovieTitleDirectorDto;
import com.example.neo4j.entity.Location;
import com.example.neo4j.entity.Movie;
import com.example.neo4j.relationship.Role;
import com.example.neo4j.repository.LocationRepository;
import com.example.neo4j.repository.MovieRepository;
import com.example.neo4j.entity.Person;
import com.example.neo4j.repository.PersonRepository;
import com.example.neo4j.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest
public class CRUDTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LocationRepository locationRepository;

    private static final String PASSWORD = "movies";

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", () -> "neo4j://localhost:7687"); //도커 사용하지 않고 테스트 진행
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> PASSWORD);
        registry.add("spring.data.neo4j.database", () -> "movies");
    } //테스트환경에서 connection을 제공한다.


    @BeforeEach
    @Transactional
    @Rollback(value = false)
    public void setUp() {
        personRepository.deleteAll();
        locationRepository.deleteAll();
        movieRepository.deleteAll();

        Person 황경하 = personRepository.save(new Person("Hwang", 1995));
        Person 황경하2 = personRepository.save(new Person("Kyeong", 1995));
        Person 황경하3 = personRepository.save(new Person("Ha", 1995));
        Location suwon = locationRepository.save(new Location("suwon"));
        황경하.getFriends().add(황경하2);
        황경하.getFriends().add(황경하3);

        황경하.getLivedIn().add(suwon);
        황경하2.getLivedIn().add(suwon);
        황경하3.getLivedIn().add(suwon);

        //새로운 PersonNode 2개를 집어넣는다.

        Role role = new Role(황경하);
        Role role2 = new Role(황경하2);
        Role role3 = new Role(황경하3);
        Role role4 = new Role(황경하3);
        //새로운 관계 2개를 정의한다.



        Set<Role> a = new HashSet<>();
        Set<Role> b = new HashSet<>();
        a.add(role);
        b.add(role2);
        a.add(role3);
        b.add(role4); //role3를 2개넣어줬는데 b에 add(role3)를 함과 동시에 a.add( role3)가 무시됨. rp는 하나의 관계에 대입되어야 하는듯.
        //관계 2개를 리스트에 넣는다.

        MovieDto movieDto = new MovieDto(
                "metrix2"
                , "welcome to real world"
                , a
                , b
                , 1997
                , 0L
        );
        //관계 2개와 properties를 가지는 MovieDto가 완성됐다.

        movieService.createOrUpdateMovie(movieDto);
        //완성된 MovieNode를 집어넣는다.
    }



    @Test
    public void depthTest(){ //depth = infinite
        System.out.println("=======depthTest===========");
        Movie metrix2 = movieRepository.findFirstByTitleLike("metrix2"); //method명을 기반으로 자동 구현된 default SpringDataNeo4j Method

//        metrix2.getActors().forEach(v->v.getPerson().getDirectors().forEach(a->System.out.println("result = " + a.getMovie().getTitle())));
        //추가 쿼리가 발생하지 않음. 처음 fetch해올때 모든 related types를 가져온다.
        System.out.println("==========depthTest end===========");

        assertThat(metrix2.getActors().getClass()).isEqualTo(LinkedHashSet.class);
        //프록시가 아니라 실제 객체기 때문에 테스트 통과
    }

    @Test
    public void persistenceContextTest(){
        System.out.println("=======persistenceContextTest===========");
        Location suwon = locationRepository.findById("suwon").get();//method명을 기반으로 자동 구현된 default SpringDataNeo4j Method
        System.out.println("================persistenceContextTest mid=====================");
        Location suwon2 = locationRepository.findById("suwon").get();
        System.out.println("==========persistenceContextTest end===========");
        assertThat(suwon).isNotEqualTo(suwon2); //SDN은 persistenceContext와 비슷한 기능을 제공하지 않기에? 이 테스트는 통과함.
        // entitymanager과 비슷한 기능이 있는지 찾아봐야할듯
    }

    @Test
    public void fetchStrategyTest(){
        System.out.println("=======fetchStrategyTest===========");
        Movie metrix2 = movieRepository.findFirstByTitleLike("metrix2"); //method명을 기반으로 자동 구현된 default SpringDataNeo4j Method
        System.out.println("================fetchStrategyTest middle=====================");
        System.out.println("metrix2.getActors().getClass() = " + metrix2.getActors().getClass()); // 타입이 프록시가 아님

        metrix2.getActors().forEach(a -> System.out.println("actor.getPerson().getName() = " + a.getPerson().getName()));
        //추가 쿼리가 발생하지 않음. 처음 fetch해올때 모든 related types를 가져온다.
        System.out.println("==========fetchStrategyTest end===========");

        assertThat(metrix2.getActors().getClass()).isEqualTo(LinkedHashSet.class);
        //프록시가 아니라 실제 객체기 때문에 테스트 통과
    }
    //모든 연산마다 모든 노드를 가져오는것은 말이 안됨.
    //persistenceContext와 같은 무언가를 사용하지 않고도 어떻게 해야 fetch lazy같은 효과를 낼 수 있을지

    @Test
    @Transactional
    @Rollback(value = false)
    public void dirtyCheckTest(){ //당연하지만 동작 안함 entityManager같은게 있을까??
        Person hwang = personRepository.findFirstByName("Hwang");
        hwang.setBorn(2020);
    }

    @Test
    public void biDirectionTest() { //양방향관계 설정 순환 참조가 발생하지는 않는지?

        Movie metrix2 = movieRepository.findFirstByTitleLike("metrix2");
        metrix2.getActors().forEach(v-> System.out.println("actorList = " + v.getPerson().getName()));

        System.out.println("===================biDirectionTest==========");
        Person hwang = personRepository.findFirstByName("Hwang");
        System.out.println("===================biDirectionTest mid==========");
        hwang.getDirectors().forEach(v-> System.out.println("v.getMovie().getTitle() = " + v.getMovie().getTitle()));
        System.out.println("===================biDirectionTest end==========");
    }


    @Test
    public void regexTest() {
        List<Movie> metri = movieRepository.findRegexByTitle("metri");
        System.out.println("metri.size() = " + metri.size());
        System.out.println("metri title = " + metri.get(0).getTitle());
        assertThat("metrix2").isEqualTo(metri.get(0).getTitle());
    }

    @Test
    public void BasicSearch() {
        List<Movie> metrix2 = movieRepository.findMovieByTitleLike("metrix2");
        metrix2.get(0).getDirectors().forEach(person -> System.out.println("person.getDirectors().getBorn() = " + person.getPerson().getBorn()));
        metrix2.get(0).getDirectors().forEach(person -> System.out.println("person.getDirectors().getName() = " + person.getPerson().getName()));
        System.out.println("Title  = " + metrix2.get(0).getTitle());
        assertThat("metrix2").isEqualTo(metrix2.get(0).getTitle());
    }


    @Test
    public void relationShipTest() {
        List<Person> acted_in = personRepository.findPersonByRelationship();
        System.out.println("acted_in = " + acted_in.get(0).getName());
    }

    @Test
    public void projectionTest() {
        List<MovieTitleDirectorDto> met = movieRepository.findByProjection("met");
        System.out.println("met.get(0).getDirectorName() = " + met.get(0).getDirectorName());
        System.out.println("met.get(0).getTagline() = " + met.get(0).getTagline());
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void updateTest() {
        Person hwang = personRepository.findFirstByName("Hwang");
        hwang.setBorn(2023);
        personRepository.save(hwang);

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


}
