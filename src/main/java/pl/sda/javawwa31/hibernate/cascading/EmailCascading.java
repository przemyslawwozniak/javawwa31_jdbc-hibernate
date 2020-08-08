package pl.sda.javawwa31.hibernate.cascading;

import javax.persistence.*;

@Entity
public class EmailCascading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String subject;
    @OneToOne(mappedBy = "email", cascade = CascadeType.PERSIST)
    MessageCascading message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MessageCascading getMessage() {
        return message;
    }

    public void setMessage(MessageCascading message) {
        this.message = message;
    }
}

