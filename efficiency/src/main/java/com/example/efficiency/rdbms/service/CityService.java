package com.example.efficiency.rdbms.service;

import com.example.efficiency.neo4j.repository.CityNeoRepository;
import com.example.efficiency.neo4j.service.CityNeoService;
import com.example.efficiency.rdbms.entity.City;
import com.example.efficiency.rdbms.entity.Route;
import com.example.efficiency.rdbms.repository.CityJpaRepository;
import com.example.efficiency.rdbms.repository.RouteJpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Service
public class CityService {

    @PersistenceContext
    private EntityManager em;

    private final CityJpaRepository cityJpaRepository;
    private final RouteJpaRepository routeJpaRepository;

    private final CityNeoRepository cityNeoRepository;
    private final CityNeoService cityNeoService;

    public CityService(CityJpaRepository cityJpaRepository, RouteJpaRepository routeJpaRepository, CityNeoRepository cityNeoRepository, CityNeoService cityNeoService) {
        this.cityJpaRepository = cityJpaRepository;
        this.routeJpaRepository = routeJpaRepository;
        this.cityNeoRepository = cityNeoRepository;
        this.cityNeoService = cityNeoService;
    }

    public int calculateCityTraverseCostJPA(int joinCount , int dataCount, int unNecessaryDataRatio){
        for(int i = 1 ; i <= joinCount ; i++){
            cityJpaRepository.save(new City(i,
                            "City"+i,
                            Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                            (int)(Math.random()*10)
                    )
            );
        }
        for(int i = 1 ; i< joinCount ; i++){ //다음 city와 이어지는 라우트 (유효한 라우트 - 조인할때 필요)
            City start = cityJpaRepository.findById(i).get();
            City end = cityJpaRepository.findById(i+1).get();
            for(int j = 0 ; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                routeJpaRepository.save(new Route(start, end ,cost));
            }
        }
        for(int i = 1 ; i< joinCount ; i++){ //유효하지 않은 라우트의 갯수(조인할때 필요 없는 데이터)
            City start = cityJpaRepository.findById(i).get();
            for(int j = 0 ; j < dataCount*unNecessaryDataRatio ; j++){
                int iter = (int)(Math.random()*joinCount+1);
                if(iter == i+1){ //이러면 유효라우트로 연결되기 때문에, 다른 라우트로 바꾸어준다.
                    iter += i+1 < joinCount ? 1: -1;
                    if(iter == 0)
                        iter = joinCount-1;
                }
                City end = cityJpaRepository.findById(iter).get();
                int cost = (int)(Math.random()*10000);
                routeJpaRepository.save(new Route(start, end ,cost));
            }
        }
        em.flush();
        em.clear();
        String sql="";
        char[] variantList = new char[joinCount+1];
        for(int i = 1; i < joinCount ;i++){
            variantList[i] = (char)(65 + i);
        }
        /**
         *
         * select
         * B.cost*(1+ Bcity.fee/100)+C.cost*(1+ Ccity.fee/100)+D.cost*(1+ Dcity.fee/100)
         *  from
         * (select * from route where start_id = 1) B , (select * from route where start_id = 2) C,
         * (select * from route where start_id = 3) D ,
         * (select fee from city where id = 1) as Bcity,
         * (select fee from city where id = 2) as Ccity,
         * (select fee from city where id =3) as Dcity
         * where 1=1
         * and B.end_id = C.start_id and C.end_id = D.start_id ;
         *
         *                 이런 쿼리를 동적으로 만들어서 NativeQuery로 날리고자
         *                 아래와 같은 코드를 작성함..
         * */

        StringBuilder st = new StringBuilder();
        st.append("select ");
        for(int i = 1 ; i < joinCount ; i++){
            st.append( variantList[i]+".cost*(1+ "+variantList[i]+"city.fee/100)+");
        }
        st.deleteCharAt(st.length()-1);
        st.append(" from ");
        for(int i = 1 ; i < joinCount ; i++){
            st.append("(select * from route where start_id = "+i+") "+variantList[i]+" ,");
        }
        for(int i = 1 ; i < joinCount ; i++){
            st.append("(select fee from city where id = "+i+") "+variantList[i]+"city ,");
        }
        st.deleteCharAt(st.length()-1);
        st.append("where 1=1 ");
        for(int i = 1 ; i < joinCount-1 ; i++){
            st.append("and "+variantList[i]+".end_id = "+variantList[i+1]+".start_id ");
        }
        st.append(";");
        int startTime = (int)System.currentTimeMillis();
        List<Integer> result = em.createNativeQuery(st.toString())
                .getResultList();
        int spentTime = (int)System.currentTimeMillis()-startTime;
        System.out.println("spentTime = " + spentTime);
        System.out.println("result.size() = " + result.size());
        return spentTime;
    }
}
