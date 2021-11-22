package com.example.efficiency.rdbms.repository.sevenrouterepository;

import com.example.efficiency.rdbms.entity.City;
import com.example.efficiency.rdbms.entity.Route;
import com.example.efficiency.rdbms.entity.sevenroute.RouteA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteOneJpaRepository extends JpaRepository<RouteA, Long> {
     List<Route> findByStartAndEnd(@Param("start") City start, @Param("end") City end);
}
