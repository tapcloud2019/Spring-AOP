package com.example.aop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class exceptioncontroller {

    @RequestMapping(method = RequestMethod.GET, path = "/throw")
    public ResponseEntity<?> getThrow() {
        throw new RuntimeException("Throwing runtime exception from getThrown");
    }
}
