package com.example.efficiency.rdbms.repository;

import com.example.efficiency.rdbms.entity.City;
import com.example.efficiency.rdbms.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteJpaRepository extends JpaRepository<Route, Long> {
     List<Route> findByStartAndEnd(@Param("start") City start, @Param("end") City end);
}
