package com.example.efficiency.neo4j.repository;

import com.example.efficiency.neo4j.entity.CityNeo;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CityNeoRepository extends Neo4jRepository<CityNeo, Integer> {
}
