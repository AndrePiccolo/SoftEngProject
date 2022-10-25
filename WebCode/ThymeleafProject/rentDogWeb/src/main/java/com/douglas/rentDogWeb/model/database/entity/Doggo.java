package com.douglas.rentDogWeb.model.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "doggo")
public class Doggo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doggo_id", nullable = false)
    private Integer dogId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "doggo_name", nullable = false, length = 100)
    private String dogName;

    @Column(name = "doggo_breed", nullable = false, length = 100)
    private String dogBreed;

    @Column(name = "doggo_size", nullable = false, length = 50)
    private String dogSize;

    @Column(name = "doggo_description", nullable = false, length = 500)
    private String dogDesc;

    @Column(name = "doggo_availability", nullable = false, length = 20)
    private String dogAvailability;

    @Column(name = "doggo_price_per_hour", nullable = false)
    private Double dogPriceHour;
}