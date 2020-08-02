package pl.sda.javawwa31.hibernate.relations;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Email {
    @Id
    @GeneratedValue
    Long id;

    String subject;

    @OneToOne//(mappedBy = "email") //IMPORTANT!
    Message message;

}