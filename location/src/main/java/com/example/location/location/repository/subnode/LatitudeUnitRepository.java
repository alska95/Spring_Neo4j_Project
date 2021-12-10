package com.example.location.location.repository.subnode;

import com.example.location.location.entity.subnode.LatitudeUnit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LatitudeUnitRepository extends Neo4jRepository<LatitudeUnit, String> {
}
