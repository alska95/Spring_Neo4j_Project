package com.example.neo4j.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class CastMemberDto {
    private final String name;
    private final String job;
    private final String role;

    public CastMemberDto(String name, String job) {
        this(name, job, null);
    }

    public CastMemberDto withRole(String role) {
        return new CastMemberDto(this.name, this.job, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberDto that = (CastMemberDto) o;
        return Objects.equals(name, that.name) && Objects.equals(job, that.job) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, job, role);
    }
}
