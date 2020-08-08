package pl.sda.javawwa31.hibernate.domain;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class OrphanRemovalTest {

    @Test
    public void orphaned_copies_are_removed() {
        //given
        Movie movie = Movie.builder()
                .title("Ogniem i mieczme")
                .genre(Genre.Historical)
                .releaseDate(LocalDate.of(1999, 2, 8))
                .build();
        Long movieId;
        List<Long> copiesId = new ArrayList<>();

        /*Copy c1 = new Copy();
        c1.setMovie(movie);
        Copy c2 = new Copy();
        c2.setMovie(movie);
        Copy c3 = new Copy();
        c3.setMovie(movie);*/

        Copy[] copies = {new Copy(), new Copy(), new Copy()};
        movie.setCopies(Arrays.asList(copies));

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(movie); //should persist copies too due to CASCADE.PERSIST
            movieId = movie.getId();
            tx.commit();
        }

        try(Session session = DefaultSessionService.getSession()) {
            movie = session.get(Movie.class, movieId);

            //pobierzmy ID zapisacnych kopii
            for(Copy c : movie.getCopies())
                copiesId.add(c.getId());
        }

        assertNotNull(movie);
        assertNotNull(movie.getCopies());

        //when
        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(movie);  //powinno usunac movie oraz copies z nim powiazane
            tx.commit();
        }

        //then
        try(Session session = DefaultSessionService.getSession()) {
            movie = session.get(Movie.class, movieId);

            for(Long copyId : copiesId)
                assertNull(session.get(Copy.class, copyId));    //powinno zwrocic null, bo nie ma ich w bazie danych
        }

        assertNull(movie);
    }

}
