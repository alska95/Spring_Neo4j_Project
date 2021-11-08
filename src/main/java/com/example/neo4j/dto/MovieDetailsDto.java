package com.example.neo4j.dto;


import com.example.neo4j.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class MovieDetailsDto {
    private final String title;
    private final List<CastMemberDto> cast;
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MovieDetailsDto that = (MovieDetailsDto) o;
        return Objects.equals(title, that.title) && Objects.equals(cast, that.cast);
    }
    @Override
    public int hashCode(){
        return Objects.hash(title ,cast);
    }
}
