package com.example.efficiency.compare.service;

import com.example.efficiency.neo4j.service.CityNeoService;
import com.example.efficiency.rdbms.service.CityService;
import org.springframework.stereotype.Service;

@Service
public class EfficiencyCompareService {

    final CityNeoService cityNeoService;
    final CityService cityService;

    public EfficiencyCompareService(CityNeoService cityNeoService, CityService cityService) {
        this.cityNeoService = cityNeoService;
        this.cityService = cityService;
    }

    public void compareRDBMSandNeo4j(int joinCount, int dataCount, int unNecessaryDataRatio){

        int resultForNeo4j = cityNeoService.calculateCityTraverseCostNeo(joinCount, dataCount, unNecessaryDataRatio);
        int resultForRDBMS = cityService.calculateCityTraverseCostJPA(joinCount, dataCount, unNecessaryDataRatio);

    }
}
