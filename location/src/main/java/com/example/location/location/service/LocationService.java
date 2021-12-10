package com.example.location.location.service;

import com.example.location.location.dto.LocationDto;
import com.example.location.location.dto.ResponseLocationDto;
import com.example.location.location.entity.Location;
import com.example.location.location.entity.subnode.*;
import com.example.location.location.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LocationService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private DatabaseSelectionProvider databaseSelectionProvider;

    public ResponseLocationDto save(LocationDto locationDto) {
        Location saved = locationRepository.save(locationDtoToEntity(locationDto));
        return entityToResponse(saved);
    }

    public ResponseLocationDto update(LocationDto locationDto) {
        Location updated = locationRepository.save(locationDtoToEntity(locationDto));
        return entityToResponse(updated);
    }

    public void delete(String locationId) {
        locationRepository.deleteById(locationId);
    }
    public ResponseLocationDto find(String locationId) {
        return entityToResponse(locationRepository.findById(locationId).orElse(null));
    }


    private ResponseLocationDto entityToResponse(Location location) {
        return modelMapper.map(location, ResponseLocationDto.class);
    }

    //locationDto를 Entity로 변환합니다.
    private Location locationDtoToEntity(LocationDto locationDto) {

        Location location = modelMapper.map(locationDto, Location.class);
        insertRelationship(location, locationDto);

        log.info("location un size = [{}]", location.getUN_LOC_IND_CD().size());


        return location;
    }

    private void insertRelationship(Location location, LocationDto locationDto) {

        insertRelationshipOfSubNode(location.getCSTMS_CD(), locationDto.getCSTMS_CD(), Customs.class , location.getLOC_CD());
        insertRelationshipOfSubNode(location.getGMT_HRS(), locationDto.getGMT_HRS(), GmtHours.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getLAT_UT_CD(), locationDto.getLAT_UT_CD(), LatitudeUnit.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getLOC_CHR_CD(), locationDto.getLOC_CHR_CD(), LocationCharacter.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getLOC_TP_CD(), locationDto.getLOC_TP_CD(), LocationType.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getLON_UT_CD(), locationDto.getLON_UT_CD(), LongitudeUnit.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getUN_LOC_IND_CD(), locationDto.getUN_LOC_IND_CD(), UNLocationIndicator.class, location.getLOC_CD());
        insertRelationshipOfSubNode(location.getLOCL_STE_CD(), locationDto.getLOCL_STE_CD(), LocalStateCode.class, location.getLOC_CD());

        insertSelfRelationship(location.getEQ_CTRL_OFC_CD() ,locationDto.getEQ_CTRL_OFC_CD() , location.getLOC_CD());
        insertSelfRelationship(location.getHUB_LOC_CD() , locationDto.getHUB_LOC_CD(), location.getLOC_CD());

    }

    private void insertSelfRelationship(List relation, String targetId, String loc_cd) {
        Optional<Location> tmp = Optional.empty();
        if (targetId != null)
            tmp = locationRepository.findById(targetId);
        if (!tmp.isEmpty()) {
            relation.add(tmp.get());
            StringBuilder sb = new StringBuilder();
            sb.append("MATCH (:Location {LOC_CD :\"")
                    .append(targetId)
                    .append("\"})-[n]-(:Location {LOC_CD : \"")
                    .append(loc_cd)
                    .append(" \" }) delete n");
            neo4jClient.query(sb.toString())
                    .in(database())
                    .run();
        }
    }

    //(대상 관계 ,  관계에 들어갈 subNode) 대상관계에 subnode를 value에 따라 생성하고 넣어줍니다.
    private <T> void insertRelationshipOfSubNode(List relation, String value, Class<T> type, String loc_cd) {
        if(value == null)
            return;
        log.info("this is called : [{}][{}][{}]",relation,value,type);
        String name = type.getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("MATCH (:")
                .append(name)
                .append(")-[n]-(:Location {LOC_CD : \"")
                .append(loc_cd)
                .append(" \" }) delete n");
        neo4jClient.query(sb.toString())
                .in(database())
                .run();
        try {
            T result = type.getConstructor(String.class).newInstance(value);
            relation.add(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

//    private <T extends BaseFormat> T valueToEntity(String target , Class<T> type){
//        return type.cast((T) new BaseFormat(target));
//    }
}
