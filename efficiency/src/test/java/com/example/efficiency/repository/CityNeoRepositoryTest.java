package com.example.efficiency.repository;


import com.example.efficiency.neo4j.entity.CityNeo;
import com.example.efficiency.neo4j.repository.CityNeoRepository;
import com.example.efficiency.rdbms.entity.City;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@SpringBootTest
public class CityNeoRepositoryTest {

    private CityNeoRepository cityNeoRepository;

    @Test
    public void efficiencyTest(){
        int joinCount =9;
        int dataCount = 5;
        for(int i = 1 ; i <= joinCount ; i++){
            cityNeoRepository.save(new CityNeo(i,
                            Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                            Long.toString(UUID.randomUUID().getMostSignificantBits(), 36),
                            (int)(Math.random()*10)
                    )
            );
        }

    }

}
