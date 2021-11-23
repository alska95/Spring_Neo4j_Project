package com.example.efficiency.compare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.transform.Result;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComparedResultListDto {
    private List<ResultDto> rdbmsResults;
    private List<ResultDto> neo4jResults;
}
