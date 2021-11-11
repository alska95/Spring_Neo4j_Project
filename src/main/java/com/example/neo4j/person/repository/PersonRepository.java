package com.example.neo4j.person.repository;

import com.example.neo4j.person.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends Neo4jRepository<Person , String> {

    @Query("match(p:Person)-[:ACTED_IN]->(m:Movie) return p")
    List<Person> findPersonByRelationship();

    @Query("match(p:Person)-[:ACTED_IN]->(m:Movie) " +
            "where m contains $title return p")
    List<Person> findPersonByRelationShipAndMovieTitle(@Param("title") String title);

}
