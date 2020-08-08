package pl.sda.javawwa31.hibernate.domain;

/*
CREATE TABLE IF NOT EXISTS customers (
	customerId INT(7) AUTO_INCREMENT,
	fullName VARCHAR(255) NOT NULL,
	phone VARCHAR(11) NOT NULL, #xxx-xxx-xxx
	address VARCHAR(255) NOT NULL,
    PRIMARY KEY(customerId)
);
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    @Id
    @GeneratedValue
    Long id;

    @OneToMany(mappedBy = "customer")
    List<Rent> rents;

    @Column(nullable = false)
    String fullName;

    //nalezy zadbac o to aby ten String mial rzeczywiscie tylko 11 znakow
    /*
    You can put the @Builder.ObtainVia annotation on the parameters (in case of a constructor or method)
    or fields (in case of @Builder on a type) to indicate alternative means by which the value
    for that field/parameter is obtained from this instance.
    For example, you can specify a method to be invoked: @Builder.ObtainVia(method = "calculateFoo").
     */
    @Column(nullable = false, length = 11)
    //@Builder.ObtainVia(method = "setPhone")   //do metod ktore WYLICZAJA wartosc
    //to-do: add hibernate validator
    String phone;

    @Column(nullable = false)
    String address;

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    //zeby nie wpakowywac sie w problemy z obsluga wyjatkow itd. po prostu obcinamy do 11 znakow
    /*public void setPhone(String phone) {
        if(phone.length() <= 11)
            this.phone = phone;
        else
            this.phone = phone.substring(0, 10);
    }*/

    public String getAddress() {
        return address;
    }

    public List<Rent> getRents() {
        return rents;
    }
}
