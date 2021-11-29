package com.example.location.repository;

import com.example.location.entity.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LocationRepository extends Neo4jRepository<Location, String > {
}
