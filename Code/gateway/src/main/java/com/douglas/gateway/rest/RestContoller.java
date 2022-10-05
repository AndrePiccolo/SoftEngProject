package com.douglas.gateway.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestContoller.URL_MAPPING)
@RequiredArgsConstructor
public class RestContoller {

    public static final String URL_MAPPING = "/rentdog/v1";
}
