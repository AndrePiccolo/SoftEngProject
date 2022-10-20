package com.douglas.rentDogWeb.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    private Integer userId;
    private String email;
    private String bod;
    private String password;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String phone;
}
