package com.example.location.location.repository;

import com.example.location.location.entity.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

public interface LocationRepository extends Neo4jRepository<Location, String> {
}
