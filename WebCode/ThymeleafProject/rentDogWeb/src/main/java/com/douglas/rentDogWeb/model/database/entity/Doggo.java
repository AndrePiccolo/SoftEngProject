package com.douglas.rentDogWeb.model.database.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doggo")
public class Doggo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doggo_id", nullable = false)
    private Integer dogId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false , updatable = false)
    private Customer customer;

    @Column(name = "doggo_name", nullable = false, length = 100)
    private String dogName;

    @Column(name = "doggo_breed", nullable = false, length = 100)
    private String dogBreed;

    @Column(name = "doggo_size", nullable = false, length = 50)
    private String dogSize;

    @Column(name = "doggo_description", nullable = false, length = 500)
    private String dogDesc;

    @Column(name = "doggo_availability_sunday", nullable = false, length = 1)
    private Integer availabilitySunday;

    @Column(name = "doggo_availability_monday", nullable = false, length = 1)
    private Integer availabilityMonday;

    @Column(name = "doggo_availability_tuesday", nullable = false, length = 1)
    private Integer availabilityTuesday;

    @Column(name = "doggo_availability_wednesday", nullable = false, length = 1)
    private Integer availabilityWednesday;

    @Column(name = "doggo_availability_thursday", nullable = false, length = 1)
    private Integer availabilityThursday;

    @Column(name = "doggo_availability_friday", nullable = false, length = 1)
    private Integer availabilityFriday;

    @Column(name = "doggo_availability_saturday", nullable = false, length = 1)
    private Integer availabilitySaturday;

    @Column(name = "doggo_price_per_hour", nullable = false)
    private Double dogPriceHour;

    @Column(name = "doggo_active", nullable = false, length = 1)
    private Integer dogActive;
}