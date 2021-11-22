package com.example.efficiency.compare.service;

import com.example.efficiency.compare.dto.ComparedResultDto;
import com.example.efficiency.compare.dto.ResultDto;
import com.example.efficiency.neo4j.service.CityNeoService;
import com.example.efficiency.rdbms.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EfficiencyCompareService {

    final CityNeoService cityNeoService;
    final CityService cityService;

    public EfficiencyCompareService(CityNeoService cityNeoService, CityService cityService) {
        this.cityNeoService = cityNeoService;
        this.cityService = cityService;
    }

    public ComparedResultDto compareRDBMSandNeo4j(int joinCount, int dataCount, int unNecessaryDataRatio){

        ResultDto resultForRDBMS = cityService.calculateCostWithSeparateRouteJPA(joinCount, dataCount, unNecessaryDataRatio);
        ResultDto resultForNeo4j = cityNeoService.calculateCityTraverseCostNeo(joinCount, dataCount, unNecessaryDataRatio);

        return new ComparedResultDto(resultForNeo4j.getSpentTime(), resultForRDBMS.getSpentTime(), resultForNeo4j.getResultList() , resultForRDBMS.getResultList());

    }

    public ComparedResultDto compareRDBMSandNeo4jInRange(int joinCount, int dataCount, int unNecessaryDataRatio){

        ResultDto resultForRDBMS = cityService.calculateCityTraverseCostJPA(joinCount, dataCount, unNecessaryDataRatio);
        List<ResultDto> resultForNeo4j = cityNeoService.calculateCostNeoStartingOne(joinCount, dataCount, unNecessaryDataRatio);


        return null;

    }
}
