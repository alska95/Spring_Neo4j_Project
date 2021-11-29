package com.example.location.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    @Id
    private String locationName;
    private String portInlandFlag;
    private String bulkPortFlag;
    private String locationGridNumber;
    private String gmtHours;
    @Property(name = "UTC_gap_hour_content")
    private String utcGapHourContent;
    private String callPortFlag;
    private Date beforeOfficeChangeDate;
    private String vesselOperationPortFlag;
    private String vesselOperationLocationURL;
    private String vesselOperationPortMonitorFlag;
    private String vesselOperationBunkeringPortFlag;
    private String locationLocalLanguageName;
    private String locationLatitude;
    private String locationLongitude;
    private String deleteFlag;
    private String newLocationLatitude;
    private String newLocationLongitude;


    @Relationship(type = "HAS_EQUIPMENT_CONTROL_OF", direction = INCOMING)
    private List<Location> EQControlLocation;

    @Relationship(type = "IS_HUB_OF" , direction =  INCOMING)
    private List<Location> HubLocation;

    @Relationship(type = "LOCATED_CONTINENT" , direction = OUTGOING)
    private List<Continent> locatedContinent;

//    @Relationship(type = "LOCATED_SUBCONTINENT")



}
