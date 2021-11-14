package com.example.neo4j.person.entity;

import com.example.neo4j.relationship.RolePerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
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

    @Relationship(type = "ACTED_IN", direction = OUTGOING) //RelationShip 설정
    private Set<RolePerson> actors;

    @Relationship(type = "DIRECTED", direction = OUTGOING) //RelationShip 설정
    private Set<RolePerson> directors;

    public Person(String name, Integer born) {
        this.name = name;
        this.born = born;
    }
}
