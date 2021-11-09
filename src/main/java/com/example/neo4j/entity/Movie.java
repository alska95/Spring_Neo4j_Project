package com.example.neo4j.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.*;

@Node
@Getter
@Setter
@AllArgsConstructor
public class Movie {
    @Id
    private final String title;

    @Property
    private final String tagline;

    @Relationship(type = "ACTED_IN", direction = INCOMING) //RelationShip 설정
    private Set<Person> actors = new HashSet<>();

    @Relationship(type = "DIRECTED", direction = INCOMING) //RelationShip 설정
    private Set<Person> directors = new HashSet<>();

    @Property
    private Integer released;

    @Property
    private Long votes;

    public Movie(String title, String tagline) {
        this.title = title;
        this.tagline = tagline;
    }
}
