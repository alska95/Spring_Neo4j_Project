package com.example.efficiency.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class City {
    @Id
    private Long id;

    private String name;

    private String address;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Person friend;
*/

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "start")
    private List<Route> start;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "end")
    private List<Route> end;

    public City(Long id, String name , String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
