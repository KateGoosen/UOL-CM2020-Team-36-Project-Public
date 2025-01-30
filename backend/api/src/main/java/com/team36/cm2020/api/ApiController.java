package com.team36.cm2020.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

}
