package com.example.efficiency.repository;

import com.example.efficiency.entity.Route;
import com.example.efficiency.entity.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest
class CityJPARepositoryTest {

    @Autowired
    private CityJpaRepository cityJpaRepository;
    @Autowired
    private RouteJpaRepository routeJpaRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Rollback(value = false) //친구가 없는 사람을 찾을때까지 계속 조인하는 시나리오
    public void testEfficiency(){
        Long joinCount = 10L;
        Long dataCount = 10L;
        for(Long i = 1L ; i <= joinCount ; i++){
            cityJpaRepository.save(new City(i,
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36))
            );
        }
        for(Long i = 1L ; i< joinCount ; i++){
            City start = cityJpaRepository.findById(i).get();
            City end = cityJpaRepository.findById(i+1).get();
            for(int j = 0 ; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                routeJpaRepository.save(new Route(start, end ,cost));
            }
        }
        em.flush();
        em.clear();

        System.out.println("====================설정완료===============");
        long startTime = System.currentTimeMillis();

        City currentCity = cityJpaRepository.findById(1L).get();
        City nextCity = cityJpaRepository.findById(currentCity.getId()+1).get();
        List<Route> routeList = routeJpaRepository.findByStartAndEnd(currentCity, nextCity);
        int count = 1;
        while(true){ //작업중
            nextCity = cityJpaRepository.findById(currentCity.getId()+1).get();
            System.out.println("============ Join count = " + count++ + " ===========================");
            routeList = routeJpaRepository.findByStartAndEnd(currentCity, nextCity);
            for(int i = 0 ; i < routeList.size() ; i++){

            }
            currentCity = nextCity;
            break;
        }
        //when

        long spentTime = System.currentTimeMillis()-startTime;
        System.out.println("================ spentTime = " + spentTime+"====================");
        //then
    }
}