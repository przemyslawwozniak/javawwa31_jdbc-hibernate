package pl.sda.javawwa31.hibernate.relations;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.domain.*;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class RelationsTest {

    @Test
    public void rent_relates_to_customer_and_copy_AND_copy_relates_to_movie() {
        Long rentId, customerId, copyId, movieId;
        Rent rent;
        Customer customer;
        Copy copy;
        Movie movie;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            rent = Rent.builder()
                    .rentPricePerDay(new BigDecimal("3.25"))
                    .borrowedDate(LocalDate.now())
                    .status(RentStatus.IN_RENT)
                    .build();
            customer = Customer.builder()
                    .fullName("Przemyslaw Wozniak")
                    .phone("777 777 777")
                    .address("Pelczynskiego 14D/150")
                    .build();
            movie = Movie.builder()
                    .title("Ogniem i mieczem")
                    .genre(Genre.Historical)
                    .releaseDate(LocalDate.of(1999, 2, 8))
                    .build();
            copy = new Copy();

            //relacja rent - customer zarzadza rent
            rent.setCustomer(customer);
            //relacja rent - copy zarzadza rent
            rent.setCopy(copy);
            //relacja copy - movie zarzadza copy
            copy.setMovie(movie);

            session.save(rent);
            session.save(customer);
            session.save(movie);
            session.save(copy);

            rentId = rent.getId();
            customerId = customer.getId();
            movieId = movie.getId();
            copyId = copy.getId();

            tx.commit();
        }

        assertNotNull(rent.getCopy());
        assertNull(copy.getRent());
        assertNotNull(rent.getCustomer());
        assertNull(customer.getRents());

        assertNotNull(copy.getMovie());
        assertNull(movie.getCopies());

        try(Session session = DefaultSessionService.getSession()) {
            rent = session.get(Rent.class, rentId);
            copy = session.get(Copy.class, copyId);
            movie = session.get(Movie.class, movieId);
            customer = session.get(Customer.class, customerId);
        }

        //po odczytaniu z bazy danych i uwzglednieniu kolumny FK, relacje w JVM zostaja poprawnie ustanowione
        assertNotNull(rent.getCopy());
        assertNotNull(copy.getRent());
        assertNotNull(rent.getCustomer());
        assertNotNull(customer.getRents());
        assertNotNull(copy.getMovie());
        assertNotNull(movie.getCopies());
    }

    @Test
    public void testNonManagedRelationship() {
        Long emailId, msgId;
        Email email;
        Message msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new Email();
            msg = new Message();

            email.setMessage(msg);
            //msg.setEmail(email);

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        assertNotNull(email.getMessage());
        assertNull(msg.getEmail());

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(Email.class, emailId);
            msg = session.get(Message.class, msgId);
        }

        assertNotNull(email.getMessage());
        assertNull(msg.getEmail());
    }

    @Test
    public void testManuallyManagedRelationship() {
        Long emailId, msgId;
        Email email;
        Message msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new Email();
            msg = new Message();

            email.setMessage(msg);
            msg.setEmail(email);

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        //JVM
        assertNotNull(email.getMessage());
        assertNotNull(msg.getEmail());

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(Email.class, emailId);
            msg = session.get(Message.class, msgId);
        }

        //DB
        assertNotNull(email.getMessage());
        assertNotNull(msg.getEmail());
    }

    @Test
    public void testManagedRelationship() {
        Long emailId, msgId;
        EmailMapped email;
        MessageMapped msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new EmailMapped();
            email.setSubject("JavaWWA13");
            msg = new MessageMapped();
            msg.setContent("See you @02.02.2019!");

            //email.setMessage(msg);
            msg.setEmail(email);

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        assertEquals(email.getSubject(), "JavaWWA13");
        assertEquals(msg.getContent(), "See you @02.02.2019!");
        assertNull(email.getMessage());
        assertNotNull(msg.getEmail());

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(EmailMapped.class, emailId);
            msg = session.get(MessageMapped.class, msgId);
        }

        assertNotNull(email.getMessage());
        assertNotNull(msg.getEmail());
    }

    @Test
    public void managed_relationship_called_from_wrong_side_ie_non_managed() {
        Long emailId, msgId;
        EmailMapped email;
        MessageMapped msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new EmailMapped();
            email.setSubject("JavaWWA13");
            msg = new MessageMapped();
            msg.setContent("See you @02.02.2019!");

            email.setMessage(msg);
            //msg.setEmail(email);    //mappedBy jest po stronie email czyli relacja zarzadza msg

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        //JVM
        assertEquals(email.getSubject(), "JavaWWA13");
        assertEquals(msg.getContent(), "See you @02.02.2019!");
        assertNotNull(email.getMessage());  //dla JVM jest ustawione pole msg dla email
        assertNull(msg.getEmail()); //ale nie jest ustawione pole email dla msg

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(EmailMapped.class, emailId);    //nadpisz ref do email obiektem z bazy danych
            msg = session.get(MessageMapped.class, msgId);  //jw
        }

        //DB
        assertNull(email.getMessage()); //poniewaz FK do email znajduje sie po stronie msg a nie zostalo ustawione to nadpisany obiekt email utracil ref do msg
        assertNull(msg.getEmail()); //email nigdy nie zostal powiazany z msg poprzez FK
    }

}


