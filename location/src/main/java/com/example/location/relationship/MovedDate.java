package com.example.location.relationship;

import com.example.location.entity.Person;
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
public class MovedDate {

    @Id@GeneratedValue
    private Long id;

    @TargetNode //targetNode는 relationship선언해줄 node의 대상 node로 설정해준다.(방향 상관 x)
    private Person person;
}
