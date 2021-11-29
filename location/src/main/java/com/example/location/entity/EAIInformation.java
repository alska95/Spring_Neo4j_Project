package com.example.location.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EAIInformation {
    @Id
    private String EAIInterfaceId;
    private String EAIEventDate;
    private String EDWUpdateDate;
}
