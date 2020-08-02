package pl.sda.javawwa31.hibernate.relations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MessageMapped {
    @Id
    @GeneratedValue
    Long id;
    String content;
    @OneToOne
    EmailMapped email;  //FK do email

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailMapped getEmail() {
        return email;
    }

    public void setEmail(EmailMapped email) {
        this.email = email;
    }
}
