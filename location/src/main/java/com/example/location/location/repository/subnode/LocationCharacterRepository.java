package com.example.location.location.repository.subnode;

import com.example.location.location.entity.subnode.GmtHours;
import com.example.location.location.entity.subnode.LocationCharacter;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCharacterRepository extends Neo4jRepository<LocationCharacter, String> {
}
