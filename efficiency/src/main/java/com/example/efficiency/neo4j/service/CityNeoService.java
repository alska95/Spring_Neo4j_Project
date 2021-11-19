package com.example.efficiency.neo4j.service;

import com.example.efficiency.neo4j.entity.CityNeo;
import com.example.efficiency.neo4j.relationship.RouteNeo;
import com.example.efficiency.neo4j.repository.CityNeoRepository;
import org.neo4j.driver.Driver;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

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
     * r1.cost*(1+c1.fee) +
     * r2.cost*(1+c2.fee);
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

    public int calculateCityTraverseCostNeo(int joinCount, int dataCount , int unNecessaryDataRatio){
        cityNeoRepository.deleteAll();
        for(int i = 1 ; i <= joinCount ; i++){
            cityNeoRepository.save(new CityNeo(i,
                            "City"+i,
                            Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                            (int)(Math.random()*10)
                    )
            );
        }
        for(int i = 1 ; i < joinCount ; i++){//다음 city와 이어지는 라우트 (유효한 라우트 - 조인할때 필요)
            CityNeo start = cityNeoRepository.findById(i).get();
            CityNeo end = cityNeoRepository.findById(i+1).get();
            for(int j = 0; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
                cityNeoRepository.save(start);
            }
        }
        for(int i = 1 ; i< joinCount ; i++){ //유효하지 않은 라우트의 갯수(조인할때 필요 없는 데이터)
            CityNeo start = cityNeoRepository.findById(i).get();
            for(int j = 0 ; j < dataCount*unNecessaryDataRatio ; j++){
                int iter = (int)(Math.random()*joinCount+1);
                if(iter == i+1){ //이러면 유효라우트로 연결되기 때문에, 다른 라우트로 바꾸어준다.
                    iter += i+1 < joinCount ? 1: -1;
                    if(iter == 0)
                        iter = joinCount-1;
                }
                CityNeo end = cityNeoRepository.findById(iter).get();
                int cost = (int)(Math.random()*10000);
                start.getRoute().add(new RouteNeo(end, cost));
                cityNeoRepository.save(start);
            }
        }

        System.out.println("================ Fetch result start ===============");
        int startTime = (int)System.currentTimeMillis();
        Collection<Integer> result = createDynamicCypher(joinCount, dataCount);
        int spentTime = (int)System.currentTimeMillis() - startTime;
        System.out.println("================ Fetch result end ===============");
        System.out.println("result.size() = " + result.size());
        return spentTime;
    }

    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

}