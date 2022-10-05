package com.douglas.rentDog.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CustomerJson {

    @JsonProperty("customer_id")
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dob")
    private Date dob;

    @JsonProperty("password")
    private String password;

    @JsonProperty("adrress")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("province")
    private String province;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
