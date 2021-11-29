package com.example.location.repository;

import com.example.location.entity.EAIInformation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EAIInformationRepository extends Neo4jRepository<EAIInformation, String > {
}
