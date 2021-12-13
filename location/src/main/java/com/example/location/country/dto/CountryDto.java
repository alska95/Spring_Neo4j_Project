package com.example.location.country.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    @JsonProperty(value = "CNT_CD")
    private String CNT_CD;
    @JsonProperty(value = "CNT_NM")
    private String CNT_NM;
    @JsonProperty(value = "FRGN_VAT_FLG")
    private String FRGN_VAT_FLG;
    @JsonProperty(value = "ZN_DIV_BSEL_CD")
    private String ZN_DIV_BSEL_CD;
    @JsonProperty(value = "BKG_ADDR_ORD_CD")
    private String BKG_ADDR_ORD_CD;
    @JsonProperty(value = "CNT_ISO_CD")
    private String CNT_ISO_CD;
    @JsonProperty(value = "EU_CNT_FLG")
    private String EU_CNT_FLG;
    @JsonProperty(value = "DELT_FLG")
    private String DELT_FLG;
    @JsonProperty(value = "UPD_USR_ID")
    private String UPD_USR_ID;
    @JsonProperty(value = "UPD_DT")
    private String UPD_DT;
    @JsonProperty(value = "SCONTI_CD")
    private String SCONTI_CD;

}
