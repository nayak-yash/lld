package services;

import models.Show;
import repo.MovieRepository;
import repo.ShowRepository;
import repo.TheatreRepository;

import java.util.List;

public class ShowService {
    private final ShowRepository showRepository;
    private final TheatreRepository theatreRepository;
    private final MovieRepository movieRepository;

    public ShowService(ShowRepository showRepository, TheatreRepository theatreRepository, MovieRepository movieRepository) {
        this.showRepository = showRepository;
        this.theatreRepository = theatreRepository;
        this.movieRepository = movieRepository;
    }

    public void createShow(Show show) {
        showRepository.save(show);
    }

    public Show getShow(String id) {
        return showRepository.getById(id);
    }

    public List<Show> getShowsByMovieTitle(String title) {
        return showRepository.getAll().stream()
                .filter(show -> show.getMovie().getTitle().equalsIgnoreCase(title))
                .toList();
    }
}
