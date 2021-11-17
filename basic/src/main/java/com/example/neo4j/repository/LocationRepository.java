package com.example.neo4j.repository;


import com.example.neo4j.entity.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends Neo4jRepository<Location, String> {
    Location findFirstByCityLike(@Param("city") String city);
}
