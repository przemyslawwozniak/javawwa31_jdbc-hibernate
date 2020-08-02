package pl.sda.javawwa31.hibernate.relations;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue
    Long id;

    String content;

    @OneToOne
    Email email;

}
