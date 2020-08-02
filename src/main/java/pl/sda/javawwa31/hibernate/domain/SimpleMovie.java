package pl.sda.javawwa31.hibernate.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Movies")
public class SimpleMovie {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)   //SQL: NOT NULL
    String title;

    //konstruktor bezargumentowy jest wymagany przez Hibernate
    public SimpleMovie() {
    }

    public SimpleMovie(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
