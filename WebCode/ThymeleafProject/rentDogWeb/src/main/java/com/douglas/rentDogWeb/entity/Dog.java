package com.douglas.rentDogWeb.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Dog {
    private Integer dogId;
    private String name;
    private String size;
    private String breed;
    private String description;
    private List<String> availability;
    private Double price;
}
