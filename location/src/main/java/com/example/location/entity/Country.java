package com.example.location.entity;

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
    private String countryName;
    private String currencyCode;
    private String foreignVatFlag;
    private String zoneDivisionBaselineCC;
    private String bookingAddressOrderD;
    private String countryIsoCode;
    private String euCountryFlag;
    private String deleteFlag;

}
