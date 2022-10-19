package com.douglas.rentDogWeb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class RentDogController {

    @GetMapping(path = "/login")
    public void login() {
    }

    @GetMapping(path = "/home")
    public void home() {
    }

    @GetMapping(path = "/contract")
    public void contract() {
    }

    @GetMapping(path = "/registerdog")
    public void registerdog() {
    }

    @GetMapping(path = "/registeruser")
    public void registeruser() {
    }

    @GetMapping(path = "/search")
    public void search() {
    }
}
