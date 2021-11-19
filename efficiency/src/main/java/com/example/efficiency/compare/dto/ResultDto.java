package com.example.efficiency.compare.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultDto {
    private int spentTime;
    private List<Integer> resultList;
}
