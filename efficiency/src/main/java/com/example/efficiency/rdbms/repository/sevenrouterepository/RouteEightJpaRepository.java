package com.example.efficiency.rdbms.repository.sevenrouterepository;

import com.example.efficiency.rdbms.entity.City;
import com.example.efficiency.rdbms.entity.Route;
import com.example.efficiency.rdbms.entity.sevenroute.RouteG;
import com.example.efficiency.rdbms.entity.sevenroute.RouteH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteEightJpaRepository extends JpaRepository<RouteH, Long> {
     List<Route> findByStartAndEnd(@Param("start") City start, @Param("end") City end);
}
