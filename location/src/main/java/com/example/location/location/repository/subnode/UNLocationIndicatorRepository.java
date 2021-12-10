package com.example.location.location.repository.subnode;

import com.example.location.location.entity.subnode.GmtHours;
import com.example.location.location.entity.subnode.UNLocationIndicator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UNLocationIndicatorRepository extends Neo4jRepository<UNLocationIndicator, String> {
}
