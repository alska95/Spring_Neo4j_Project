package com.example.neo4j.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Person {
    @Id
    private final String name;
    private final Integer born;

    public Person(String name, Integer born) {
        this.name = name;
        this.born = born;
    }
}
