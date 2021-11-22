package com.example.efficiency.rdbms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class City {
    @Id
    private int id;

    private String name;

    private String address;

    private int fee;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Person friend;
*/

/*    @OneToMany(fetch = FetchType.LAZY, mappedBy = "start")
    private List<Route> start;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "end")
    private List<Route> end;*/

    public City(int id, String name , String address , int fee) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.fee = fee;
    }
}
