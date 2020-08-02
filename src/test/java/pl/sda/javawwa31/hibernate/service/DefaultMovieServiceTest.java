package pl.sda.javawwa31.hibernate.service;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Ignore;
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

    @Test
    public void updates_existing_movie() {
        //given:
        movieService.findOrCreateMovie("Random Movie", Genre.Thriller, LocalDate.of(1996, 2, 14));

        //when:
        Movie updated = Movie.builder()
                .title("Random Movie")
                .genre(Genre.Comedy)
                .releaseDate(LocalDate.of(2020, 8, 2))
                .description("Our custom favourite movie! :)")
                .build();

        movieService.updateMovie(updated);

        Movie afterUpdate = movieService.findMovie("Random Movie");
        System.out.println("Po aktualizacji, odczyt z DB: " + afterUpdate);

        //then:
        Assertions.assertThat(afterUpdate).isEqualToIgnoringNullFields(updated)
                .as("Film nie zostal zaktualizowany.");
    }

    //TO-DO
    @Ignore
    @Test
    public void does_not_update_non_existing_movie() {

    }

    @Test
    public void deletes_existing_movie() {
        //given
        movieService.createMovie("My movie", Genre.Documentary, LocalDate.now(), null);
        Movie beforeDelete = movieService.findMovie("My movie");

        //when
        movieService.deleteMovie("My movie");
        Movie afterDelete = movieService.findMovie("My movie");

        //then
        Assertions.assertThat(beforeDelete).isNotNull();
        Assertions.assertThat(afterDelete).isNull();
    }

    @Ignore
    @Test
    public void does_not_delete_non_existing_movie() {

    }

}
