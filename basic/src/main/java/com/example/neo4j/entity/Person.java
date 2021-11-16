package com.example.neo4j.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class Person {

    @Id
    private String name;

    private Integer born;

/*    @Relationship(type = "ACTED_IN", direction = OUTGOING) //RelationShip 설정
    private Set<RolePerson> actors;

    @Relationship(type = "DIRECTED", direction = OUTGOING) //RelationShip 설정
    private Set<RolePerson> directors;*/

    @Relationship(type = "FRIEND" )
    private Set<Person> friends = new HashSet<>();

    @Relationship(type = "DIRECTED", direction = OUTGOING)
    private Set<Movie> directed;

    @Relationship(type = "LIVED_IN", direction = OUTGOING)
    private Set<Location> livedIn = new HashSet<>();

    public Person(String name, Integer born) {
        this.name = name;
        this.born = born;
    }
}
