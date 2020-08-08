package pl.sda.javawwa31.hibernate.domain;

/*
CREATE TABLE IF NOT EXISTS copies (
	copyId INT(7) AUTO_INCREMENT,
	movieId INT(7) NOT NULL,
	isRented BOOLEAN DEFAULT false,
    PRIMARY KEY(copyId),
    CONSTRAINT fk_copies_movies_movieId FOREIGN KEY(movieId) REFERENCES movies(movieId)
);
 */

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "COPIES")
public class Copy {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Movie movie;

    @OneToOne(mappedBy = "copy")
    Rent rent;

    @ColumnDefault("false")
    boolean isRented;

    public Long getId() {
        return id;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Rent getRent() {
        return rent;
    }
}
