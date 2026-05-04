package repo;

import models.Movie;

import java.util.HashMap;
import java.util.Map;

public class MovieRepository {
    private final Map<String, Movie> movieMap = new HashMap<>();

    public void save(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }

    public Movie getById(String id) {
        return movieMap.get(id);
    }
}
