package com.example.location.location.entity;

import com.example.location.country.Country;
import com.example.location.location.entity.subnode.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    private String LOC_CD;
    private String LOC_NM;
    private String CML_ZN_FLG;
    private String VOP_PORT_MNTR_FLG;
    private String VOP_PORT_FLG;
    private String LOC_LOCL_LANG_NM;
    private String LOC_LAT;
    private Date LOC_LON;
    private String DELT_FLG;
    private String CRE_DT;
    private String NEW_LOC_LON;
    private String CRE_USR_ID;
    private String UPD_USR_ID;
    private String UPD_DT;
    private String MODI_LOC_CD;
    private String MODI_LOC_CD2;
    private String CALL_PORT_FLG;
    private String PORT_INLND_CD;
    private String ZIP_CD;
    private String LOC_AMS_PORT_CD;


    @Relationship(type = "HAS_EQUIPMENT_CONTROL_OF", direction = INCOMING)
    private List<Location> EQ_CTRL_OFC_CD = new ArrayList<>();

    @Relationship(type = "IS_HUB_OF" , direction =  INCOMING)
    private List<Location> HUB_LOC_CD= new ArrayList<>();

    @Relationship(type = "HAS_LOCATION_TYPE_OF" , direction = OUTGOING)
    private List<LocationType> LOC_TP_CD= new ArrayList<>();

    @Relationship(type = "HAS_LONGITUDE_UNIT_OF" , direction = OUTGOING)
    private List<LongitudeUnit> LON_UT_CD= new ArrayList<>();

    @Relationship(type = "HAS_LOCAL_STATE_CODE_OF" , direction = OUTGOING)
    private List<LocalStateCode> LOCL_STE_CD= new ArrayList<>();

    @Relationship(type = "HAS_CUSTOMS_CODE_OF" , direction = OUTGOING)
    private List<Customs> CSTMS_CD= new ArrayList<>();

    @Relationship(type = "HAS_LATITUDE_UNIT_OF" , direction = OUTGOING)
    private List<LatitudeUnit> LAT_UT_CD= new ArrayList<>();

    @Relationship(type = "HAS_LOCATION_CHAR_OF" , direction = OUTGOING)
    private List<LocationCharacter> LOC_CHR_CD= new ArrayList<>();

    @Relationship(type = "HAS_UN_LOCATION_INDICATOR_OF" , direction = OUTGOING)
    private List<UNLocationIndicator> UN_LOC_IND_CD= new ArrayList<>();

    @Relationship(type = "HAS_GMT_HOURS_OF" , direction = OUTGOING)
    private List<LocationCharacter> GMT_HRS= new ArrayList<>();

    ///////////////////////// 다른 테이블 relation //////////////////////

    @Relationship(type = "LOCATED_COUNTRY", direction = INCOMING)
    private List<Country> CNT_CD;
}
