package pl.sda.javawwa31.hibernate.relations;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;
import pl.sda.javawwa31.hibernate.service.DefaultSessionService;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class RelationsTest {

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


