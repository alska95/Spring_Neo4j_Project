package com.example.location.location.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @JsonProperty(value = "LOC_CD")
    private String LOC_CD;
    @JsonProperty(value = "LOC_NM")
    private String LOC_NM;
    @JsonProperty(value = "CML_ZN_FLG")
    private String CML_ZN_FLG;
    @JsonProperty(value = "VOP_PORT_MNTR_FLG")
    private String VOP_PORT_MNTR_FLG;
    @JsonProperty(value = "VOP_PORT_FLG")
    private String VOP_PORT_FLG;
    @JsonProperty(value = "LOC_LOCL_LANG_NM")
    private String LOC_LOCL_LANG_NM;
    @JsonProperty(value = "LOC_LAT")
    private String LOC_LAT;
    @JsonProperty(value = "LOC_LON")
    private Date LOC_LON;
    @JsonProperty(value = "DELT_FLG")
    private String DELT_FLG;
    @JsonProperty(value = "CRE_DT")
    private String CRE_DT;
    @JsonProperty(value = "NEW_LOC_LON")
    private String NEW_LOC_LON;
    @JsonProperty(value = "CRE_USR_ID")
    private String CRE_USR_ID;
    @JsonProperty(value = "UPD_USR_ID")
    private String UPD_USR_ID;
    @JsonProperty(value = "UPD_DT")
    private String UPD_DT;
    @JsonProperty(value = "MODI_LOC_CD")
    private String MODI_LOC_CD;
    @JsonProperty(value = "MODI_LOC_CD2")
    private String MODI_LOC_CD2;
    @JsonProperty(value = "CALL_PORT_FLG")
    private String CALL_PORT_FLG;
    @JsonProperty(value = "PORT_INLND_CD")
    private String PORT_INLND_CD;
    @JsonProperty(value = "ZIP_CD")
    private String ZIP_CD;
    @JsonProperty(value = "LOC_AMS_PORT_CD")
    private String LOC_AMS_PORT_CD;
    @JsonProperty(value = "EQ_CTRL_OFC_CD")
    private String EQ_CTRL_OFC_CD;
    @JsonProperty(value = "LOC_TP_CD")
    private String LOC_TP_CD;
    @JsonProperty(value = "LON_UT_CD")
    private String LON_UT_CD;
    @JsonProperty(value = "LOCL_STE_CD")
    private String LOCL_STE_CD;
    @JsonProperty(value = "CSTMS_CD")
    private String CSTMS_CD;
    @JsonProperty(value = "LAT_UT_CD")
    private String LAT_UT_CD;
    @JsonProperty(value = "UN_LOC_IND_CD")
    private String UN_LOC_IND_CD;
    @JsonProperty(value = "GMT_HRS")
    private String GMT_HRS;
    @JsonProperty(value = "LOC_CHR_CD")
    private String LOC_CHR_CD;
    @JsonProperty(value = "HUB_LOC_CD")
    private String HUB_LOC_CD;



}
