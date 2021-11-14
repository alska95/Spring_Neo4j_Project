package com.example.neo4j.movie.service;

import com.example.neo4j.movie.dto.CastMemberDto;
import com.example.neo4j.movie.dto.MovieDetailsDto;
import com.example.neo4j.movie.dto.MovieDto;
import com.example.neo4j.movie.dto.MovieResultDto;
import com.example.neo4j.movie.entity.Movie;
import com.example.neo4j.movie.repository.MovieRepository;
import org.neo4j.driver.*;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final Neo4jClient neo4jClient;
    private final Driver driver;
    private final DatabaseSelectionProvider databaseSelectionProvider;

    MovieService(MovieRepository movieRepository,
                 Neo4jClient neo4jClient,
                 Driver driver,
                 DatabaseSelectionProvider databaseSelectionProvider) {

        this.movieRepository = movieRepository;
        this.neo4jClient = neo4jClient;
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    public Movie createOrUpdateMovie(MovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.getTitle(), //pk
                movieDto.getTagline(),
                movieDto.getDirectors(),
                movieDto.getActors(),
                movieDto.getReleased(),
                movieDto.getVotes()
        );
        return movieRepository.save(movie);
    }

    public MovieDto createByClient(MovieDto movieDto) {
        return this.neo4jClient
                .query("" +
                        "CREATE (TheMatrix:Movie {title: $title, released: $released, tagline: $tagline})"
                )
                .in(database())
                .bindAll(Map.of("title", movieDto.getTitle()))
                .bindAll(Map.of("released", movieDto.getReleased()))
                .bindAll(Map.of("tagline", movieDto.getTagline()))
                .fetchAs(MovieDto.class)
                .one()
                .orElse(null);
    }

    public void deleteMovie(MovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.getTitle(), //pk
                movieDto.getTagline(),
                movieDto.getDirectors(),
                movieDto.getActors(),
                movieDto.getReleased(),
                movieDto.getVotes()
        );
        movieRepository.delete(movie);
    }


    public MovieDetailsDto fetchDetailsByTitle(String title) {
        return this.neo4jClient
                .query("" +
                        "MATCH (movie:Movie {title: $title}) " +
                        "OPTIONAL MATCH (person:Person)-[r]->(movie) " +
                        "WITH movie, COLLECT({ name: person.name, job: REPLACE(TOLOWER(TYPE(r)), '_in', ''), role: HEAD(r.roles) }) as cast " +
                        "RETURN movie { .title, cast: cast }"
                )
                .in(database())
                .bindAll(Map.of("title", title))
                .fetchAs(MovieDetailsDto.class)
                .mappedBy(this::toMovieDetails)
                .one()
                .orElse(null);
    }

    public int voteInMovieByTitle(String title) {
        return this.neo4jClient
                .query("MATCH (m:Movie {title: $title}) " +
                        // $title과 일치하는 Movie를 선택한다.
                        "WITH m, coalesce(m.votes, 0) AS currentVotes " +
                        //선택한 Movie의 값이 null이면 0으로 선택하고 currentVotes라는 변수값을 입힌다.
                        "SET m.votes = currentVotes + 1;")
                // 선택한 Movie의 votes 값을 현재 votes에 1을 더한값으로 set 한다.
                .in(database())
                .bindAll(Map.of("title", title))
                .run()
                .counters()
                .propertiesSet();
    }

    public List<MovieResultDto> searchMoviesByTitle(String title) {
        return this.movieRepository.findSearchResults(title)
                .stream()
                .map(MovieResultDto::new) //.map(v-> new MovieResultDto(v))
                .collect(Collectors.toList());
    }

    public List<MovieDto> searchMoviesByTitle2(String title) {
        return movieRepository.findMovieByTitleLike(title)
                .stream()
                .map(MovieDto::new) //v-> new MovieResultDto(v)
                .collect(Collectors.toList());
    }

    /**
     * Neo4jClient나 Repository 사용하지 않고 Driver사용하여 db접근
     */
    public Map<String, List<Object>> fetchMovieGraph() {

        var nodes = new ArrayList<>();
        var links = new ArrayList<>();

        try (Session session = sessionFor(database())) {
            var records = session.readTransaction(tx -> tx.run(""
                    + " MATCH (m:Movie) <- [r:ACTED_IN] - (p:Person)"
                    + " WITH m, p ORDER BY m.title, p.name"
                    + " RETURN m.title AS movie, collect(p.name) AS actors"
            ).list());
            records.forEach(record -> {
                var movie = Map.of("label", "movie", "title", record.get("movie").asString());

                var targetIndex = nodes.size();
                nodes.add(movie);

                record.get("actors").asList(Value::asString).forEach(name -> {
                    var actor = Map.of("label", "actor", "title", name);

                    int sourceIndex;
                    if (nodes.contains(actor)) {
                        sourceIndex = nodes.indexOf(actor);
                    } else {
                        nodes.add(actor);
                        sourceIndex = nodes.size() - 1;
                    }
                    links.add(Map.of("source", sourceIndex, "target", targetIndex));
                });
            });
        }
        return Map.of("nodes", nodes, "links", links);
    }

    private Session sessionFor(String database) {
        if (database == null) {
            return driver.session();
        }
        return driver.session(SessionConfig.forDatabase(database));
    }

    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

    private MovieDetailsDto
    toMovieDetails(TypeSystem ignored, org.neo4j.driver.Record record) {
        var movie = record.get("movie");
        return new MovieDetailsDto(
                movie.get("title").asString(),
                movie.get("cast").asList((member) -> {
                    var result = new CastMemberDto(
                            member.get("name").asString(),
                            member.get("job").asString()
                    );
                    var role = member.get("role");
                    if (role.isNull()) {
                        return result;
                    }
                    return result.withRole(role.asString());
                })
        );
    }
}