package com.example.efficiency.compare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComparedResultDto {
    int neo4jSpentTime;
    int rDBMSSpentTime;
    List<Integer> neoResultList;
    List<Integer> rdbResultList;
}
