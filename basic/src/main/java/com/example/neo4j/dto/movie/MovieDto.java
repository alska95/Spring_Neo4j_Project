package com.example.neo4j.dto.movie;

import com.example.neo4j.entity.Movie;
import com.example.neo4j.relationship.RolePerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class MovieDto {
    private String title;
    private String tagline;
    private Set<RolePerson> actors;
    private Set<RolePerson> directors;
    private Integer released;
    private Long votes;

    public MovieDto(Movie movie) {
        title = movie.getTitle();
        tagline = movie.getTagline();
        actors = movie.getActors();
        directors = movie.getDirectors();
        released = movie.getReleased();
        votes = movie.getVotes();
    }

    public MovieDto(String title, String tagline) {
        this.title = title;
        this.tagline = tagline;
    }
}
