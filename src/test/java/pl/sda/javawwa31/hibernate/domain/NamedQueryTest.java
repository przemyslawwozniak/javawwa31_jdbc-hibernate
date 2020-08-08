package pl.sda.javawwa31.hibernate.domain;

import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import java.time.LocalDate;
import java.util.List;

public class NamedQueryTest {

    @Test
    public void finds_all_movies_produced_by_given_company() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .company("SDA Movies")
                .build();

        Movie movie2 = Movie.builder()
                .title("Ogniem i mieczem 2")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .company("SDA Movies")
                .build();

        Movie movie3 = Movie.builder()
                .title("Avatar")
                .genre(Genre.Sci_Fi)
                .releaseDate(LocalDate.of(2008, 11, 24))
                .company("Disney")
                .build();

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            session.persist(movie2);
            session.persist(movie3);
            tx.commit();
        }

        //when
        List<Movie> moviesBySDA;
        try(Session session = DefaultSessionService.getSession()) {
            Query query = session.getNamedQuery("movie.findByCompany");
            query.setParameter("company", "SDA Movies");
            moviesBySDA = query.list();
        }

        //then
        Assertions.assertThat(moviesBySDA).isNotNull();
        Assertions.assertThat(moviesBySDA).hasSize(2);
    }

}
