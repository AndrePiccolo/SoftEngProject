package com.douglas.rentDog.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestContoller.URL_MAPPING)
public class RestContoller {

    public static final String URL_MAPPING = "/rentdog/v1";

    @GetMapping("/sayhello")
    public ResponseEntity<String> sayHello(@RequestHeader(value="Authorization", required = true) String auth){
        return ResponseEntity.status(HttpStatus.OK).body("Hello, it is working");
    }

}
