package com.example.aop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class departcontroller {

    @RequestMapping(method = RequestMethod.GET, path = "/bye")
    public ResponseEntity<?> getBye(@RequestParam("name") String username) {
        return ResponseEntity.ok("Bye bye, " + username);
    }
}
