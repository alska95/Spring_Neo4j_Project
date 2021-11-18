package com.example.neo4j.relationship;

import com.example.neo4j.entity.Movie;
import com.example.neo4j.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleMovie {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private Movie movie;

    public RoleMovie(Movie movie) {
        this.movie = movie;
    }
}
