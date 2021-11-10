package com.example.neo4j.person.repository;

import com.example.neo4j.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface PersonRepository extends Neo4jRepository<Person , String> {
}
