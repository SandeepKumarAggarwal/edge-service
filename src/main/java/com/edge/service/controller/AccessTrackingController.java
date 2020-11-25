package com.edge.service.controller;

import com.edge.service.models.AccessTracking;
import com.edge.service.repository.AccessTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/access/tracking")
public class AccessTrackingController {

    @Autowired
    AccessTrackingRepository accessTrackingRepository;

    @GetMapping()
    public ResponseEntity<List<AccessTracking>> getAllAccessTracking(@RequestParam(required = false) String title) {
        try {
            List<AccessTracking> accessTrackings = new ArrayList<AccessTracking>();
            accessTrackingRepository.findAll().forEach(accessTrackings::add);
            if (accessTrackings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accessTrackings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessTracking> getAccessTrackingByID(@PathVariable("id") long id) {
        Optional<AccessTracking> accessTracking = accessTrackingRepository.findById(id);

        if (accessTracking.isPresent()) {
            return new ResponseEntity<>(accessTracking.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAccessTracking(@PathVariable("id") long id) {
        try {
            accessTrackingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<HttpStatus> deleteAllAccessTracking() {
        try {
            accessTrackingRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
