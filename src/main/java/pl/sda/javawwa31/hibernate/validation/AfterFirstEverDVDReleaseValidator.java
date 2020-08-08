package pl.sda.javawwa31.hibernate.validation;

import pl.sda.javawwa31.hibernate.domain.Movie;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterFirstEverDVDReleaseValidator implements ConstraintValidator<AfterFirstEverDVDRelease, Movie> {

    @Override
    public void initialize(AfterFirstEverDVDRelease constraintAnnotation) {

    }

    @Override
    public boolean isValid(Movie movie, ConstraintValidatorContext constraintValidatorContext) {
        return movie.getReleaseDate().isAfter(LocalDate.of(1996, 12, 20));
    }
}
