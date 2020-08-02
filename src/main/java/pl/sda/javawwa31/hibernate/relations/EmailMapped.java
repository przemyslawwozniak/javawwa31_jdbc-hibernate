package pl.sda.javawwa31.hibernate.relations;

import javax.persistence.*;

@Entity
public class EmailMapped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String subject;
    @OneToOne(mappedBy = "email") //the only change we introduced
    MessageMapped message;

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

    public MessageMapped getMessage() {
        return message;
    }

    public void setMessage(MessageMapped message) {
        this.message = message;
    }
}

