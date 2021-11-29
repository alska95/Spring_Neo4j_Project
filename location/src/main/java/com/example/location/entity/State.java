package com.example.location.entity;

import org.springframework.data.neo4j.core.schema.Id;

public class State {
    @Id
    private String stateName;
    private String deleteFlag;
    private String modifiedStateCode;
}
