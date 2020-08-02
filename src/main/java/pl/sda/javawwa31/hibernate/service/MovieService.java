package pl.sda.javawwa31.hibernate.service;

import pl.sda.javawwa31.hibernate.domain.Genre;
import pl.sda.javawwa31.hibernate.domain.Movie;

import java.time.LocalDate;

public interface MovieService {

    Movie findMovie(String title);
    Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate);
    Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description);

    /**
     * Lookups movie by given title in database and updates fields according to passed non-null fields from movie instance
     *
     * @param movie
     * @return
     */
    Movie updateMovie(Movie movie);

    boolean deleteMovie(String title);

}
