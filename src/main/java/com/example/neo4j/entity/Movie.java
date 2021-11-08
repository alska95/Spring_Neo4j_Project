package com.example.neo4j.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
@Setter
public class Movie {
    @Id
    private final String title;

    private final String tagline;

    private Integer released;

    private Long votes;

    public Movie(String title, String tagline) {
        this.title = title;
        this.tagline = tagline;
    }
}
