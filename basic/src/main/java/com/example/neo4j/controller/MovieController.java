package com.example.neo4j.controller;

import com.example.neo4j.dto.movie.MovieResultDto;
import com.example.neo4j.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/movie/{title}/vote")
    public int voteByTitle(@PathVariable("title") String title) {
        return movieService.voteInMovieByTitle(title);
    }

    @GetMapping("/search")
    List<MovieResultDto> search(@RequestParam("q") String title) {
        return movieService.searchMoviesByTitle(stripWildcards(title));
    }

    @GetMapping("/graph")
    public Map<String, List<Object>> getGraph() {
        return movieService.fetchMovieGraph();
    }

    private static String stripWildcards(String title) {
        String result = title;
        if (result.startsWith("*")) {
            result = result.substring(1);
        }
        if (result.endsWith("*")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

}
