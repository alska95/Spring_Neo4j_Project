package com.example.location.location.repository.subnode;

import com.example.location.location.entity.Location;
import com.example.location.location.entity.subnode.GmtHours;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GmtHoursRepository extends Neo4jRepository<GmtHours, String> {
}
