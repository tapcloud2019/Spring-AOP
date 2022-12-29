package com.example.aop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class arrivalcontroller {

    @RequestMapping(method = RequestMethod.GET, path = "/hello")
    public ResponseEntity<?> getHello(@RequestParam("name") String username) {
        return ResponseEntity.ok("Hello, " + username);
    }
}
