package com.example.efficiency.service;

import com.example.efficiency.compare.dto.ComparedResultDto;
import com.example.efficiency.compare.service.EfficiencyCompareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CompareServiceTest {

    @Autowired
    private EfficiencyCompareService efficiencyCompareService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void efficiencyCompareTest(){

        int joinCount = 5;
        int dataCount = 1;
        int unNecessaryDataRatio = 5;
        ComparedResultDto comparedResultDto = efficiencyCompareService.compareRDBMSandNeo4j(joinCount, dataCount, unNecessaryDataRatio);
        System.out.println("====== joinCount : "+joinCount+" ==== dataCount : " + dataCount+" ==== unNecessaryDataRatio : " + unNecessaryDataRatio + " ======");
        System.out.println("========= neo4J spentTime : " + comparedResultDto.getNeo4jSpentTime() + " ==================");
        System.out.println("========= RDBMS spentTime : " + comparedResultDto.getRDBMSSpentTime() + " ==================");

        System.out.println("========= neo4J resultSize : " + comparedResultDto.getNeoResultList().size() + " ==================");
        System.out.println("========= RDBMS resultSize : " + comparedResultDto.getRdbResultList().size() + " ==================");
        System.out.println("============ test end ==============");
    }

    public void efficiencyCompareTestOneToTarget(){
        int joinCount = 5;
        int dataCount = 1;
        int unNecessaryDataRatio = 5;
        ComparedResultDto comparedResultDto = efficiencyCompareService.compareRDBMSandNeo4j(joinCount, dataCount, unNecessaryDataRatio);
        System.out.println("====== joinCount : "+joinCount+" ==== dataCount : " + dataCount+" ==== unNecessaryDataRatio : " + unNecessaryDataRatio + " ======");
        System.out.println("========= neo4J spentTime : " + comparedResultDto.getNeo4jSpentTime() + " ==================");
        System.out.println("========= RDBMS spentTime : " + comparedResultDto.getRDBMSSpentTime() + " ==================");

        System.out.println("========= neo4J resultSize : " + comparedResultDto.getNeoResultList().size() + " ==================");
        System.out.println("========= RDBMS resultSize : " + comparedResultDto.getRdbResultList().size() + " ==================");
        System.out.println("============ test end ==============");
    }
}
