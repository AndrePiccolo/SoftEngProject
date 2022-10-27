package com.douglas.rentDogWeb.controller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DogRequest {

    private String name;
    private String size;
    private String breed;
    private String description;
    private String price;
    private Integer sunday;
    private Integer monday;
    private Integer tuesday;
    private Integer wednesday;
    private Integer thursday;
    private Integer friday;
    private Integer saturday;
}