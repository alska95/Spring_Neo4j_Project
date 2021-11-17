package com.example.neo4j.entity;

import com.example.neo4j.relationship.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.*;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {


    @Id
    private String title;

    @Property
    private String tagline;

    @Relationship(type = "ACTED_IN", direction = INCOMING) //RelationShip 설정
    private Set<Role> actors;

    @Relationship(type = "DIRECTED", direction = INCOMING) //RelationShip 설정
    private Set<Role> directors;

    @Property
    private Integer released;

    @Property
    private Long votes;

    public Movie(String title, String tagline) {
        this.title = title;
        this.tagline = tagline;
    }
}
