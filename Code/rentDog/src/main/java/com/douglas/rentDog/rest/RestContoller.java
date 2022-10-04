package com.douglas.rentDog.rest;

import com.douglas.rentDog.database.entity.Doggo;
import com.douglas.rentDog.database.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(RestContoller.URL_MAPPING)
@RequiredArgsConstructor
public class RestContoller {

    public static final String URL_MAPPING = "/rentdog/v1";

    @Autowired
    DogRepository customerRepository;

    @GetMapping("/sayhello")
    public ResponseEntity<DogJson> sayHello(@RequestHeader(value="Authorization", required = true) String auth){
        Optional<Doggo> customer = customerRepository.findById(1);
        DogJson response = DogJson.builder().dogId(customer.get().getDogId())
                .customerId(CustomerJson.builder().id(customer.get().getCustomer().getCustomerId())
                    .firstName(customer.get().getCustomer().getCustomerFirstName())
                    .lastName(customer.get().getCustomer().getCustomerLastName())
                    .email(customer.get().getCustomer().getCustomerEmail())
                    .dob(customer.get().getCustomer().getCustomerDob())
                    .password(customer.get().getCustomer().getCustomerPassword())
                    .address(customer.get().getCustomer().getCustomerStreetAddress())
                    .city(customer.get().getCustomer().getCustomerCity())
                    .province(customer.get().getCustomer().getCustomerProvince())
                    .postalCode(customer.get().getCustomer().getCustomerPostalCode())
                    .phoneNumber(customer.get().getCustomer().getCustomerPhoneNumber()).build())
                .dogName(customer.get().getDogName())
                .dogBreed(customer.get().getDogBreed())
                .dogSize(customer.get().getDogSize())
                .dogDesc(customer.get().getDogDesc())
                .dogAvailability(customer.get().getDogAvailability())
                .dogPriceHour(customer.get().getDogPriceHour()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
