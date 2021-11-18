package com.example.efficiency.neo4j.entity;

import com.example.efficiency.neo4j.relationship.RouteNeo;
import com.example.efficiency.rdbms.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CityNeo {
    @Id
    private int id;

    private String name;

    private String address;

    private int fee;

    @Relationship(type = "FLIGHT_ROUTE" ,direction = OUTGOING)
    private List<RouteNeo> route;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "start")
//    private List<Route> start;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "end")
//    private List<Route> end;

    public CityNeo(int id, String name , String address , int fee) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.fee = fee;
    }
}
