package com.douglas.rentDog.rest;

import com.douglas.rentDog.database.entity.Customer;
import com.douglas.rentDog.database.repository.CustomerRepository;
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
    CustomerRepository customerRepository;

    @GetMapping("/sayhello")
    public ResponseEntity<CustomerJson> sayHello(@RequestHeader(value="Authorization", required = true) String auth){
        Optional<Customer> customer = customerRepository.findById(1);
        CustomerJson response = CustomerJson.builder().id(customer.get().getCustomerId())
                .firstName(customer.get().getCustomerFirstName())
                .lastName(customer.get().getCustomerLastName())
                .email(customer.get().getCustomerEmail())
                .dob(customer.get().getCustomerDob())
                .password(customer.get().getCustomerPassword())
                .address(customer.get().getCustomerStreetAddress())
                .city(customer.get().getCustomerCity())
                .province(customer.get().getCustomerProvince())
                .postalCode(customer.get().getCustomerPostalCode())
                .phoneNumber(customer.get().getCustomerPhoneNumber()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
