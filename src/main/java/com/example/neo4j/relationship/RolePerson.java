package com.example.neo4j.relationship;

import com.example.neo4j.person.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Getter
@Setter
public class RolePerson {

    @Id @GeneratedValue
    private Long id;

    @TargetNode
    private final Person person;

    public RolePerson(Person person){
        this.person = person;
    }
}
