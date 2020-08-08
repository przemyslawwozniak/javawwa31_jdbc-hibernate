package pl.sda.javawwa31.hibernate.domain;

/*
CREATE TABLE IF NOT EXISTS rents (
	rentId INT(7) AUTO_INCREMENT,
	copyId INT(7) NOT NULL,
	customerId INT(7) NOT NULL,
	status ENUM('In rent', 'Returned') NOT NULL DEFAULT 'In rent',
	rentPricePerDay DECIMAL(4,2) NOT NULL,
	borrowedDate DATE NOT NULL,
	returnedDate DATE,
    PRIMARY KEY(rentId),
    CONSTRAINT fk_rents_copies_copyId FOREIGN KEY(copyId) REFERENCES copies(copyId),
    CONSTRAINT fk_rents_customers_customerId FOREIGN KEY(customerId) REFERENCES customers(customerId)
);
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RENTS")
public class Rent {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Customer customer;

    @OneToOne
    Copy copy;

    @ColumnDefault("0") //default ordinal = 0 = IN_RENT
    @Column(nullable = false)
    @Enumerated//default = (EnumType.ORDINAL)
    RentStatus status;

    //11,99 DECIMAL(4,2) - musi byc typ BigDecimal, dla double mapuje po prostu na double
    @Column(nullable = false, precision = 4, scale = 2)
    BigDecimal rentPricePerDay;

    @Column(nullable = false)
    LocalDate borrowedDate;

    LocalDate returnedDate;

    public Long getId() {
        return id;
    }

    public RentStatus getStatus() {
        return status;
    }

    public BigDecimal getRentPricePerDay() {
        return rentPricePerDay;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }
}
