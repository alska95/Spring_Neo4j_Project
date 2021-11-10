package com.example.neo4j.person.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
    private String name;
    private Integer born;

    public Person(String name, Integer born) {
        this.name = name;
        this.born = born;
    }
}
