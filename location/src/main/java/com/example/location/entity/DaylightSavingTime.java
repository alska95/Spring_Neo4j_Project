package com.example.location.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Getter
@Setter
@Node
@NoArgsConstructor
@AllArgsConstructor
public class DaylightSavingTime {
    @Id
    private String daylightSavingTimeYear;
    private String daylightSavingTimeMinutes;
    private String start_daylight_saving_time_rule_description;
    private String end_daylight_saving_time_rule_description;
    private String start_daylight_saving_time_date;
    private String end_daylight_saving_time_date;
    private String delete_flag;

    @Relationship(type = "IS_NOT_APPLIED" ,direction = INCOMING)
    private List<State> notAppliedStates;

    @Relationship(type = "HAS_POLICY" , direction = INCOMING)
    private List<Country> countryHasPolicy;
}
