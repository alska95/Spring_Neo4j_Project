package com.example.efficiency.neo4j.relationship;


import com.example.efficiency.neo4j.entity.CityNeo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RouteNeo {
    @Id @GeneratedValue
    Long id;

    int cost;

    @TargetNode
    private CityNeo targetCity;
}
