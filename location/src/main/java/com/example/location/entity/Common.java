package com.example.location.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Common {
    @Id
    private String createdUserId;
    private String createdDate;
    private String updateUserId;
    private String updateDate;
}
