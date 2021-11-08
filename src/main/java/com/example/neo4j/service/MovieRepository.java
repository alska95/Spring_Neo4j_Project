package com.example.neo4j.service;

import com.example.neo4j.entity.Movie;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
interface MovieRepository extends Repository<Movie, String> {

    @Query("MATCH (movie:Movie) WHERE movie.title CONTAINS $title RETURN movie")
    List<Movie> findSearchResults(@Param("title") String title);
}