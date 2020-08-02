package pl.sda.javawwa31.hibernate.domain;

/*
CREATE TABLE IF NOT EXISTS movies (
	movieId INT(7) AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	genre ENUM('Documentary', 'Thriller', 'Musical', 'Comedy', 'Horror', 'Sci-Fi', 'Action', 'Drama', 'Romance') NOT NULL,
	releaseDate DATE NOT NULL,
	description TEXT,
    PRIMARY KEY(movieId)
);

Hibernate utworzyl:
MOVIES (id bigint not null, description varchar(2147483647), genre integer not null,
releaseDate date not null, title varchar(255) not null, primary key (id))
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MOVIES")
public class Movie {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)   //0 - Documentary, 1 - ...
    Genre genre;

    @Column(nullable = false)
    LocalDate releaseDate;

    //domyslnie String mapuje sie na varchar[255]
    //my chcemy mapowac na TEXT
    @Type(type = "text")
    String description;

    /**
     * Overrides this instance fields with non-null fields of other instance.
     *
     * @param other
     * @return true if any changes applies, otherwise return false
     */
    public boolean overrideWithNonNullFields(Movie other) {
        boolean isInstanceChanged = false;

        //TO-DO: Java Reflection API - iterate over object fields and override if other's field is non null
        if(other.genre != null) {
            this.genre = other.genre;
            isInstanceChanged = true;
        }
        if(other.releaseDate != null) {
            this.releaseDate = other.releaseDate;
            isInstanceChanged = true;
        }
        if(other.description != null) {
            this.description = other.description;
            isInstanceChanged = true;
        }

        return isInstanceChanged;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }
}
