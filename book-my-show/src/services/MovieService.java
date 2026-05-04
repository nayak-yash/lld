package services;

import models.Movie;
import repo.MovieRepository;

public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void createMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public Movie getId(String id) {
        return movieRepository.getById(id);
    }
}
