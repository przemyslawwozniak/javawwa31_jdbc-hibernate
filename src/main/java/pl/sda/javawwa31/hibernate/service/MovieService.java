package pl.sda.javawwa31.hibernate.service;

import pl.sda.javawwa31.hibernate.domain.Genre;
import pl.sda.javawwa31.hibernate.domain.Movie;

import java.time.LocalDate;

public interface MovieService {

    Movie findMovie(String title);
    Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate);
    Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description);

}
