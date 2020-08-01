package pl.sda.javawwa31.jdbc.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/*
    'offices', 'CREATE TABLE `offices` (\n  `officeCode` varchar(10) NOT NULL,\n
    `city` varchar(50) NOT NULL,\n
    `phone` varchar(50) NOT NULL,\n
    `addressLine1` varchar(50) NOT NULL,\n

    `addressLine2` varchar(50) DEFAULT NULL,\n
    `state` varchar(50) DEFAULT NULL,\n

    `country` varchar(50) NOT NULL,\n
    `postalCode` varchar(15) NOT NULL,\n
    `territory` varchar(10) NOT NULL,\n

    PRIMARY KEY (`officeCode`)\n) ENGINE=InnoDB DEFAULT CHARSET=latin1'

     */
@Builder
@Getter
public class Office {

    @NonNull
    private final String city, phone, addressLine1, country, teritory, officeCode, postalCode;

    private String addressLine2, state;
}
