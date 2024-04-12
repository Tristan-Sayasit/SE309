package com.example.testingjava11jar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(
            value = "/welcome",
            method = RequestMethod.GET
    )
    public ResponseEntity<String> Welcome(){
        return new ResponseEntity<>("Worked like a charm", HttpStatus.OK);
    }
}
