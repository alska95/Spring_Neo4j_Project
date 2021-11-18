package com.example.efficiency.repository;

import com.example.efficiency.rdbms.entity.Route;
import com.example.efficiency.rdbms.entity.City;
import com.example.efficiency.rdbms.repository.CityJpaRepository;
import com.example.efficiency.rdbms.repository.RouteJpaRepository;
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
    @Rollback(value = false) //도시를 순회하며 최소값, 최대값 , 평균 순회 비용을 찾는 테스트
    public void testEfficiency(){
        int joinCount =9;
        int dataCount = 5;
        for(int i = 1 ; i <= joinCount ; i++){
            cityJpaRepository.save(new City(i,
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                    Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                    (int)(Math.random()*10)
                    )
            );
        }
        for(int i = 1 ; i< joinCount ; i++){ //다음 city와 이어지는 라우트
            City start = cityJpaRepository.findById(i).get();
            City end = cityJpaRepository.findById(i+1).get();
            for(int j = 0 ; j < dataCount ; j++){
                int cost = (int)(Math.random()*10000);
                routeJpaRepository.save(new Route(start, end ,cost));
            }
        }
        for(int i = 1 ; i< joinCount ; i++){ //랜덤 라우트
            City start = cityJpaRepository.findById(i).get();
            for(int j = 0 ; j < dataCount ; j++){
                int iter = (int)(Math.random()*joinCount);
                iter = iter > 1 ? iter : 1;
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
        long startTime = System.currentTimeMillis();
        List<Integer> resultList = em.createNativeQuery(st.toString())
                .getResultList();
        long spentTime = System.currentTimeMillis()-startTime;
        System.out.println("spentTime = " + spentTime);
//        for (Integer integer : resultList) {
//            System.out.println("integer = " + integer);
//        }
//        for(int i = 0; i < joinCount ;i++){
//            char tmp = (char)(65 + i);
//            sql = "select " + tmp + ".cost from Route "+tmp+" where "+tmp+".start.id = "+i;
//        }
//        em.createQuery(sql);
//        System.out.println("====================설정완료===============");
//        long startTime = System.currentTimeMillis();




//        City currentCity = cityJpaRepository.findById(1L).get();
//        City nextCity = cityJpaRepository.findById(currentCity.getId()+1).get();
//        List<Route> currentRoute = routeJpaRepository.findByStartAndEnd(currentCity, nextCity);
//        int count = 1;
//        while(true){ //작업중
//            if(currentCity.getId() == joinCount)
//                break;
//            nextCity = cityJpaRepository.findById(currentCity.getId()+1).get();
//            System.out.println("============ Join count = " + count++ + " ===========================");
//            List<Route> nextRoute = new ArrayList<>();
//            for(int i = 0 ; i < currentRoute.size() ; i++){
//                int cost = currentRoute.get(i).getCost();
//                City newEnd = cityJpaRepository.findById(nextCity.getId()+1).get();
//                List<Route> next = routeJpaRepository.findByStartAndEnd(nextCity, newEnd);
//                for(int j = 0 ; j < next.size() ; j++){
//                    next.get(j).setCost(next.get(j).getCost() + cost);
//                }
//            }
//            currentCity = nextCity;
//        }
//        //when
//
//        long spentTime = System.currentTimeMillis()-startTime;
//        System.out.println("================ spentTime = " + spentTime+"====================");
//        //then
    }
}