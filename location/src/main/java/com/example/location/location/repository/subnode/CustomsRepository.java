package com.example.location.location.repository.subnode;

import com.example.location.location.entity.Location;
import com.example.location.location.entity.subnode.Customs;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomsRepository extends Neo4jRepository<Customs, String> {
}
