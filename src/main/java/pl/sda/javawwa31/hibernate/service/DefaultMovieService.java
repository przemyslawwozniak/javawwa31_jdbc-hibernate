package pl.sda.javawwa31.hibernate.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.sda.javawwa31.hibernate.domain.Genre;
import pl.sda.javawwa31.hibernate.domain.Movie;

import java.time.LocalDate;

public class DefaultMovieService implements MovieService {

    @Override
    public Movie findMovie(String title) {
        Query<Movie> query = DefaultSessionService.getSession().createQuery("from Movie m where m.title=:title", Movie.class);
        query.setParameter("title", title);
        return query.uniqueResult();    //zwraca encje lub null
    }

    @Override
    public Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate) {
        Movie m = findMovie(title);
        if(m == null) {
            m = Movie.builder()
                    .title(title)
                    .genre(genre)
                    .releaseDate(releaseDate)
                    .build();

            Session session = DefaultSessionService.getSession();
            Transaction tx = session.beginTransaction();
            session.save(m);
            tx.commit();
        }

        return m;
    }

    @Override
    public Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description) {
        Movie m = Movie.builder()
                .title(title)
                .genre(genre)
                .releaseDate(releaseDate)
                .description(description)
                .build();

        Session session = DefaultSessionService.getSession();
        Transaction tx = session.beginTransaction();
        session.save(m);
        tx.commit();

        return m;
    }
}
