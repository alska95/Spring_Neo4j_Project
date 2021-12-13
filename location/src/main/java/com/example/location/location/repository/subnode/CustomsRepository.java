package com.example.location.location.repository.subnode;

import com.example.location.location.entity.Location;
import com.example.location.location.entity.subnode.Customs;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository //subnode Repository에 있는 애들은 혹시 사용할일이 있을까봐 만들어둠.
public interface CustomsRepository extends Neo4jRepository<Customs, String> {
}
