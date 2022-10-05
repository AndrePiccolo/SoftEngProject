package com.douglas.rentDog.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DogJson {

    @JsonProperty("dog_id")
    private Integer dogId;

    @JsonProperty("customer")
    private CustomerJson customerId;

    @JsonProperty("dog_name")
    private String dogName;

    @JsonProperty("dog_breed")
    private String dogBreed;

    @JsonProperty("dog_size")
    private String dogSize;

    @JsonProperty("dog_desc")
    private String dogDesc;

    @JsonProperty("dog_availability")
    private String dogAvailability;

    @JsonProperty("dog_price_hour")
    private Double dogPriceHour;
}
