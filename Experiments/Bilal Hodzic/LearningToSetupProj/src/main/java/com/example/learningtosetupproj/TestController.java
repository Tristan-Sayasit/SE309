package com.example.learningtosetupproj;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(
            value = "/getPersons",
            produces = "application/json",
            method = {RequestMethod.GET}
    )
    public ResponseEntity<String> GetTestData(){
        return new ResponseEntity<>("Test data returned", HttpStatus.OK);
    }
}
