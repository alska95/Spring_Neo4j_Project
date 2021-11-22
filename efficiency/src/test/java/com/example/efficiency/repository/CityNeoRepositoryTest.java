package com.example.efficiency.repository;


import com.example.efficiency.neo4j.entity.CityNeo;
import com.example.efficiency.neo4j.relationship.RouteNeo;
import com.example.efficiency.neo4j.repository.CityNeoRepository;
import com.example.efficiency.neo4j.service.CityNeoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@SpringBootTest
class CityNeoRepositoryTest {

    private static final String PASSWORD = "movies";
    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", () -> "neo4j://localhost:7687"); //도커 사용하지 않고 테스트 진행
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> PASSWORD);
        registry.add("spring.data.neo4j.database", () -> "route");
    } //테스트환경에서 connection을 제공한다.

    @Autowired
    private CityNeoRepository cityNeoRepository;
    
    @Autowired
    private CityNeoService cityNeoService;

    @Test
    @Rollback(value = false)
    @Transactional
    public void efficiencyTest(){
        cityNeoRepository.deleteAll();
        int joinCount = 10;
        int dataCount = 1;
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
            for(int j = 0 ; j < dataCount*5 ; j++){
                int iter = (int)(Math.random()*joinCount+1);
                if(iter == i+1){ //이러면 유효라우트로 견결되기 때문에, 다른 라우트로 바꾸어준다.
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
        long startTime = System.currentTimeMillis();
        Collection<Integer> result = cityNeoService.createDynamicCypher(joinCount, dataCount);
        long spentTime = System.currentTimeMillis() - startTime;
        System.out.println("================ Fetch result end ===============");
        System.out.println("result.size() = " + result.size());

    }

}
