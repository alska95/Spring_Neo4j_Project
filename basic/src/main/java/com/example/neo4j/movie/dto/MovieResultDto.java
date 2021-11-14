package com.example.neo4j.movie.dto;

import com.example.neo4j.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class MovieResultDto {
    private final Movie movie;

    @Override
    public int hashCode() {
        return movie.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieResultDto that = (MovieResultDto) o;
        return Objects.equals(movie, that.movie);
    }
}
