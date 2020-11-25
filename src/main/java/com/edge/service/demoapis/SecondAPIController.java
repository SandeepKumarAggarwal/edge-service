package com.edge.service.demoapis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secondapi")
public class SecondAPIController {

    @GetMapping("/summary")
    public ResponseEntity<String> getSummary() {
        return new ResponseEntity<String>("Second API Summary", HttpStatus.OK);
    }

}
