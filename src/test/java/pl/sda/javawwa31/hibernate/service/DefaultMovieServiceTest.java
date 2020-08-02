package pl.sda.javawwa31.hibernate.service;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.domain.Genre;
import pl.sda.javawwa31.hibernate.domain.Movie;

import java.time.LocalDate;

public class DefaultMovieServiceTest {

    MovieService movieService = new DefaultMovieService();

    @Test
    public void creates_and_finds_movie() {
        //given

        //when
        movieService.createMovie("Ogniem i Mieczem",
                Genre.Historical,
                LocalDate.of(1992, 2, 8),
                "Rzeczpospolita Obojga Narodow - poczatek upadku.");

        Movie m = movieService.findMovie("Ogniem i Mieczem");

        //then
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Ogniem i Mieczem")
                .hasFieldOrPropertyWithValue("genre", Genre.Historical)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1992, 2, 8))
                .hasFieldOrPropertyWithValue("description", "Rzeczpospolita Obojga Narodow - poczatek upadku.")
                .as("Film nie zostal poprawnie zapisany w bazie danych.");
    }

    @Test
    public void does_not_find_non_existing_movie() {
        //given

        //when
        Movie m = movieService.findMovie("Listonosz Pat");

        //then
        Assertions.assertThat(m).isNull();
    }

    @Test
    public void finds_and_does_not_create_existing_movie() {
        //given
        movieService.createMovie("Planet Earth II", Genre.Documentary, LocalDate.of(2016, 11, 6), null);

        //when
        Movie m = movieService.findOrCreateMovie("Planet Earth II", Genre.Comedy, LocalDate.of(1999, 9, 9));

        //then
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Planet Earth II")
                .hasFieldOrPropertyWithValue("genre", Genre.Documentary)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2016, 11, 6))
                .hasFieldOrPropertyWithValue("description", null)
        .as("Film zostal ponownie dodany do tabeli w bazie danych, pomimo ze juz tam sie znajdowal.");
    }

    @Test
    public void does_not_find_and_creates_movie() {
        //given

        //when
        movieService.findOrCreateMovie("Seven", Genre.Thriller, LocalDate.of(1996, 2, 14));

        Movie m = movieService.findMovie("Seven");

        //then
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Seven")
                .hasFieldOrPropertyWithValue("genre", Genre.Thriller)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1996, 2, 14))
                .hasFieldOrPropertyWithValue("description", null)
                .as("Film nie zostal dodany do tabeli w bazie danych, pomimo, ze nie istnial.");
    }

}
