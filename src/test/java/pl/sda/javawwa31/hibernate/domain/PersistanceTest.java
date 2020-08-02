package pl.sda.javawwa31.hibernate.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class PersistanceTest {

    SessionFactory sessionFactory;

    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    public void saveSimpleMovies() {
        SimpleMovie movie = new SimpleMovie("Ogniem i mieczem");
        SimpleMovie movie2 = new SimpleMovie("Planet Earth II");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            session.persist(movie2);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = "saveSimpleMovies")
    public void readSimpleMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<SimpleMovie> movies = session.createQuery("from SimpleMovie", SimpleMovie.class).list();
            assertEquals(movies.size(), 2);
            for(SimpleMovie movie : movies)
                System.out.println(movie.getTitle());
        }
    }

    @Test
    public void saveMovies() {
        Movie movie = Movie.builder()
                .title("Ogniem i Mieczem")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 7))
                .description("Kozacy sie buntuja.")
                .build();

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = "saveMovies")
    public void readMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
            assertEquals(movies.size(), 1);
            for(Movie movie : movies)
                System.out.println(movie);
        }
    }

}


