package com.example.efficiency.rdbms.repository;

import com.example.efficiency.rdbms.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityJpaRepository extends JpaRepository<City, Integer> {
}
