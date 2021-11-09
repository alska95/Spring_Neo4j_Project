package com.example.neo4j.repository;

import com.example.neo4j.entity.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //그냥 명시적으로 적어둠 (SimpleNeo4jRepository가 @Repository빈으로 이미 등록되어 있다.)
public interface MovieRepository extends Neo4jRepository<Movie, String> {

    @Query("MATCH (movie:Movie) WHERE movie.title CONTAINS $title RETURN movie")
    List<Movie> findSearchResults(@Param("title") String title);
    List<Movie> findAllByTitleLike(@Param("title") String title); //스프링 DataJpa처럼 메소드명을 기반으로 구현체를 구현해준다.
}

