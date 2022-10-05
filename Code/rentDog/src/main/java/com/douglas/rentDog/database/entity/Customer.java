package com.douglas.rentDog.database.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "customer_first_name", nullable = false, length = 50)
    private String customerFirstName;

    @Column(name = "customer_last_name", nullable = false, length = 100)
    private String customerLastName;

    @Column(name = "customer_email", nullable = false, length = 100)
    private String customerEmail;

    @Temporal(TemporalType.DATE)
    @Column(name = "customer_dob", nullable = false)
    private Date customerDob;

    @Column(name = "customer_password", nullable = false, length = 250)
    private String customerPassword;

    @Column(name = "customer_street_address", nullable = false, length = 200)
    private String customerStreetAddress;

    @Column(name = "customer_city", nullable = false, length = 100)
    private String customerCity;

    @Column(name = "customer_province", nullable = false, length = 100)
    private String customerProvince;

    @Column(name = "customer_postal_code", nullable = false, length = 6)
    private String customerPostalCode;

    @Column(name = "customer_phone_number", nullable = false, length = 13)
    private String customerPhoneNumber;

    @OneToMany(mappedBy = "customer")
    private Set<Doggo> dog;
}