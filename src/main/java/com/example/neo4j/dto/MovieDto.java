package com.example.neo4j.dto;

import com.example.neo4j.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class MovieDto {
    private String title;
    private String tagline;
    private Set<Person> actors = new HashSet<>();
    private Set<Person> directors = new HashSet<>();
    private Integer released;
    private Long votes;
}
