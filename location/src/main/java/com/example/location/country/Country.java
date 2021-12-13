package com.example.location.country;

import com.example.location.subcontinent.SubContinent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Node
public class Country {
    @Id
    private String CNT_CD;
    private String CNT_NM;
    private String FRGN_VAT_FLG;
    private String ZN_DIV_BSEL_CD;
    private String BKG_ADDR_ORD_CD;
    private String CNT_ISO_CD;
    private String EU_CNT_FLG;
    private String DELT_FLG;
    private String UPD_USR_ID;
    private String UPD_DT;
    private List<SubContinent> SCONTI_CD;
}
