package pl.sda.javawwa31.hibernate.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

public class ValidationTest {

    @Test
    public void movie_validation_success() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("Polski film historyczny z 1999 roku, w reżyserii Jerzego Hoffmana, na podstawie powieści Henryka Sienkiewicza pod tym samym tytułem.")
                .avgScore(8.5)
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_custom_release_date_validation() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1995, 2, 8))
                .description("Polski film historyczny z 1999 roku, w reżyserii Jerzego Hoffmana, na podstawie powieści Henryka Sienkiewicza pod tym samym tytułem.")
                .avgScore(8.5)
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_missing_title() {
        //given
        Movie movie = Movie.builder()
                //.title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("Polski film historyczny z 1999 roku, w reżyserii Jerzego Hoffmana, na podstawie powieści Henryka Sienkiewicza pod tym samym tytułem.")
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_too_short_desc() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("Polski film historyczny.")
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_too_long_desc() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("IOIWBoDcpQGdBybt5lYnONi3Fo9CjSd99VWpLJCWlnpJA9L80Hyqx01UtbKkyAmgRsx1S4nLjysQbHZRiia4ygYWd7IpgfQPc5aSNHSOiRc9gOIlkYWLFdjsGlH8tIjzlxA4UygTs61XLiDqNGv0hWSeLwW06R57EGUgysoggfEyJ4Y3wE7N3k3i0rK2DD3cSan0ub5eadtwxDC4AJfOMvayFww90732lsNUKxHoD3gihIDJ5QXyPVYXNc4GhhZsKyHIbM221u2hykumSzH4bYXn4SJIJlLSLqx1okpkxCn1bF0zXMg5XMqaAOFoolTemgH0m3guXxwIUtFrZG54VAGhcC8Qen2uT66CpuDLyObOlUrtIcGsvXAJgEpTDiwVs9cJE5WLlNqvWhjHyPE23DQNh6VSG0iJrlcWoxMJmQw5YR4fPM9a3hFrfPaoH80Wvu2YC2pkquOzOuOmcxowJrnoKgixOnQjut6rf6sDzo2tVf9tNUnYJGwRjb4XwcMoh3rlK8FYO25nVxjPGb5BOhm8CHdwGIGOKmR8Xr\n")
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_avg_score_above_10() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("Polski film historyczny z 1999 roku, w reżyserii Jerzego Hoffmana, na podstawie powieści Henryka Sienkiewicza pod tym samym tytułem.")
                .avgScore(11.9)
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

    @Test(expectedExceptions = {ConstraintViolationException.class})
    public void movie_validation_breaks_on_avg_score_below_0() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .description("Polski film historyczny z 1999 roku, w reżyserii Jerzego Hoffmana, na podstawie powieści Henryka Sienkiewicza pod tym samym tytułem.")
                .avgScore(-7.0)
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(movie);
            tx.commit();
        }
    }

}
