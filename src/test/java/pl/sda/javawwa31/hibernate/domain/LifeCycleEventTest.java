package pl.sda.javawwa31.hibernate.domain;

import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LifeCycleEventTest {

    @Test
    public void days_from_release_are_calculated() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczme")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .build();
        Long movieId;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            movieId = movie.getId();
            tx.commit();
        }

        //when
        try(Session session = DefaultSessionService.getSession()) {
            movie = session.get(Movie.class, movieId);
        }

        //then
        Assertions.assertThat(movie.getDaysFromRelease()).isEqualTo(ChronoUnit.DAYS.between(
                LocalDate.of(1999, 2, 8),
                LocalDate.now()
        ));
    }

}
