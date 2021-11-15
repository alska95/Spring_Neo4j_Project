package com.example.location.entity;


import com.example.location.relationship.MarriedDate;
import com.example.location.relationship.MovedDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    private String name;

    @Relationship(type = "IS_MARRIED_TO", direction = OUTGOING)
    private Set<MarriedDate> marriedDate = new HashSet<>();

    @Relationship(type = "LIVES_IN" , direction = OUTGOING)
    private Set<MovedDate> movedDate = new HashSet<>();
}
