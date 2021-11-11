package com.example.neo4j.movie.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieTitleDirectorDto {
    private String tagline;
    private String directorName;
}
