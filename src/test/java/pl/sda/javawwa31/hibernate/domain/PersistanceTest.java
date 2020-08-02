package pl.sda.javawwa31.hibernate.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

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
    public void saveMovies() {
        SimpleMovie movie = new SimpleMovie("Ogniem i mieczem");
        SimpleMovie movie2 = new SimpleMovie("Planet Earth II");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            session.persist(movie2);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = "saveMovies")
    public void readMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<SimpleMovie> movies = session.createQuery("from Movies", SimpleMovie.class).list();
            assertEquals(movies.size(), 2);
            for(SimpleMovie movie : movies)
                System.out.println(movie.getTitle());
        }
    }



}


