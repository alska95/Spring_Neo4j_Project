package com.example.efficiency.rdbms.entity.sevenroute;

import com.example.efficiency.rdbms.entity.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RouteK {
    @Id @GeneratedValue
    private Long id;

    private int cost;

    @ManyToOne
    @JoinColumn
    private City start;

    @ManyToOne
    @JoinColumn
    private City end;

    public RouteK(City start, City end , int cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }
}
