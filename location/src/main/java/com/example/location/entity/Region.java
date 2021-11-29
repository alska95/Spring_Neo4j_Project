package com.example.location.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;

import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Region {
    @Id
    private String regionName;
    private String deleteFlag;

    @Relationship(type = "HAS_EAI_INFO", direction = INCOMING)
    private List<EAIInformation> eaiInformation;
}
