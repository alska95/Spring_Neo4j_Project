package com.example.efficiency.neo4j.service;

import com.example.efficiency.compare.dto.ResultDto;
import com.example.efficiency.neo4j.entity.CityNeo;
import com.example.efficiency.neo4j.relationship.RouteNeo;
import com.example.efficiency.neo4j.repository.CityNeoRepository;
import org.neo4j.driver.Driver;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CityNeoService {

    private final CityNeoRepository cityNeoRepository;
    private final Neo4jClient neo4jClient;
    private final Driver driver;
    private final DatabaseSelectionProvider databaseSelectionProvider;

    CityNeoService(CityNeoRepository cityNeoRepository,
                 Neo4jClient neo4jClient,
                 Driver driver,
                 DatabaseSelectionProvider databaseSelectionProvider) {

        this.cityNeoRepository = cityNeoRepository;
        this.neo4jClient = neo4jClient;
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    /**
    * match
     * (c1:CityNeo {name:"City1"})-
     * [r1:FLIGHT_ROUTE]
     * ->
     * (c2:CityNeo {name:"City2"})
     * -
     * [r2:FLIGHT_ROUTE]
     * ->
     * (c3:CityNeo{name:"City3"})
     * return
     * r1.cost*(1+c1.fee/100) +
     * r2.cost*(1+c2.fee/100);
     * 이와 같은 cyper쿼리를 동적으로 생성
    *
    * */
    public List<Integer> createDynamicCypher(int joinCount, int dataCount){
        StringBuilder sb = new StringBuilder();
        sb.append("match ");

        for(int i = 1 ; i <= joinCount ; i++){
            sb.append("(c"+i+":CityNeo {name:\"City"+i+"\"})-[r"+i+":FLIGHT_ROUTE] ->");
        }
        int deleteIndex = sb.indexOf("-[r"+joinCount);

        sb.delete(deleteIndex,sb.length());

        sb.append("return ");
        for(int i = 1 ; i < joinCount ; i++){
            sb.append("r"+i+".cost*(1+c"+i+".fee) +");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        System.out.println("==========calculate start ==============");
        long startTime = System.currentTimeMillis();
        Collection<Integer> all = neo4jClient
                .query(sb.toString())
                .in(database())
                .fetchAs(Integer.class)
                .all();
        long spentTime = System.currentTimeMillis()-startTime;
        System.out.println("==========calculate end ==============");
        System.out.println("spentTime = " + spentTime);
        List<Integer> collect = all.stream().collect(Collectors.toList());

        return collect;
    }

    @Transactional
    public ResultDto calculateCityTraverseCostNeo(int joinCount, int dataCount , int unNecessaryDataRatio){
        createDataSet(joinCount, dataCount, unNecessaryDataRatio);

        System.out.println("================ Fetch result start ===============");
        int startTime = (int)System.currentTimeMillis();
        Collection<Integer> result = createDynamicCypher(joinCount, dataCount);
        int spentTime = (int)System.currentTimeMillis() - startTime;
        System.out.println("================ Fetch result end ===============");
        System.out.println("result.size() = " + result.size());
        List<Integer> resultList = result.stream().collect(Collectors.toList());
        return new ResultDto(spentTime, resultList);
    }

    @Transactional
    public List<ResultDto> calculateCostNeoStartingOne(int joinCount, int dataCount , int unNecessaryDataRatio){
        if(joinCount < 2)
            joinCount = 2;

        createDataSet(joinCount, dataCount, unNecessaryDataRatio);
        List<ResultDto> resultDtos = new ArrayList<>();
        for(int i = 2 ; i <= joinCount ; i++){
            System.out.println("================ Fetch result start ===============");
            int startTime = (int)System.currentTimeMillis();
            Collection<Integer> result = createDynamicCypher(i, dataCount);
            int spentTime = (int)System.currentTimeMillis() - startTime;
            System.out.println("================ Fetch result end ===============");
            System.out.println("result.size() = " + result.size());
            List<Integer> resultList = result.stream().collect(Collectors.toList());
            resultDtos.add(new ResultDto(spentTime, resultList));
        }
        return resultDtos;
    }

    @Transactional
    public List<ResultDto> repeatJoinByTwoNode(int joinCount, int dataCount , int unNecessaryDataRatio){
        createRepeatableDataSet(joinCount, dataCount, unNecessaryDataRatio);

        return null;
    }

    private void createRepeatableDataSet(int joinCount, int dataCount, int unNecessaryDataRatio) {
        cityNeoRepository.deleteAll();
        List<CityNeo> cityNeos = new ArrayList<>();
        cityNeos.add(new CityNeo(0, "Dummy" , "NotUsed" , 0));

        for(int i = 1 ; i <= 2 ; i++){
            cityNeos.add(new CityNeo(i,
                    "City"+i,
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                    (int)(Math.random()*10),
                    new ArrayList<>()));
            cityNeoRepository.saveAll(cityNeos);
        }


        for(int i = 1 ; i < joinCount ; i++){ //다음 city와 이어지는 라우트 (유효한 라우트 - 조인할때 필요)
            CityNeo start = cityNeos.get(i);
            CityNeo end = cityNeos.get(i+1);
            for(int j = 0; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
            }
        }

        for(int i = 1 ; i< joinCount ; i++){ //유효하지 않은 라우트의 갯수(조인할때 필요 없는 데이터)
            CityNeo start = cityNeos.get(i);
            for(int j = 0 ; j < dataCount*unNecessaryDataRatio ; j++){
                int iter = (int)(Math.random()*joinCount+1);
                if(iter == i+1){ //이러면 유효라우트로 연결되기 때문에, 다른 라우트로 바꾸어준다.
                    iter += i+1 < joinCount ? 1: -1;
                    if(iter == 0)
                        iter = joinCount-1;
                }
                CityNeo end = cityNeos.get(iter);
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
            }
        }

        cityNeoRepository.saveAll(cityNeos);
    }


    public List<Integer> createRepeatableDynamicCypher(int joinCount, int dataCount){
        StringBuilder sb = new StringBuilder();
        sb.append("match ");

        for(int i = 1 ; i <= joinCount ; i++){
            sb.append("(c"+i+":CityNeo {name:\"City"+i+"\"})-[r"+i+":FLIGHT_ROUTE] ->");
        }
        int deleteIndex = sb.indexOf("-[r"+joinCount);

        sb.delete(deleteIndex,sb.length());

        sb.append("return ");
        for(int i = 1 ; i < joinCount ; i++){
            sb.append("r"+i+".cost*(1+c"+i+".fee) +");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        System.out.println("==========calculate start ==============");
        long startTime = System.currentTimeMillis();
        Collection<Integer> all = neo4jClient
                .query(sb.toString())
                .in(database())
                .fetchAs(Integer.class)
                .all();
        long spentTime = System.currentTimeMillis()-startTime;
        System.out.println("==========calculate end ==============");
        System.out.println("spentTime = " + spentTime);
        List<Integer> collect = all.stream().collect(Collectors.toList());

        return collect;
    }

    private void createDataSet(int joinCount, int dataCount , int unNecessaryDataRatio){
        cityNeoRepository.deleteAll();
        List<CityNeo> cityNeos = new ArrayList<>();
        cityNeos.add(new CityNeo(0, "Dummy" , "NotUsed" , 0));

        for(int i = 1 ; i <= joinCount ; i++){
            cityNeos.add(new CityNeo(i,
                    "City"+i,
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                    (int)(Math.random()*10),
                    new ArrayList<>()));
            cityNeoRepository.saveAll(cityNeos);
        }


        for(int i = 1 ; i < joinCount ; i++){//다음 city와 이어지는 라우트 (유효한 라우트 - 조인할때 필요)
            CityNeo start = cityNeos.get(i);
            CityNeo end = cityNeos.get(i+1);
            for(int j = 0; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
            }
        }

        for(int i = 1 ; i< joinCount ; i++){ //유효하지 않은 라우트의 갯수(조인할때 필요 없는 데이터)
            CityNeo start = cityNeos.get(i);
            for(int j = 0 ; j < dataCount*unNecessaryDataRatio ; j++){
                int iter = (int)(Math.random()*joinCount+1);
                if(iter == i+1){ //이러면 유효라우트로 연결되기 때문에, 다른 라우트로 바꾸어준다.
                    iter += i+1 < joinCount ? 1: -1;
                    if(iter == 0)
                        iter = joinCount-1;
                }
                CityNeo end = cityNeos.get(iter);
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
            }
        }

        cityNeoRepository.saveAll(cityNeos);

    }

    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

}
