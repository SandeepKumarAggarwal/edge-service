package com.edge.service.controller;

import com.edge.service.repository.AccessControlRepository;
import com.edge.service.models.AccessControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/access")
public class AccessController {

	@Autowired
    AccessControlRepository accessControlRepository;

	@GetMapping()
	public ResponseEntity<List<AccessControl>> getAllAccessControls(@RequestParam(required = false) String title) {
		try {
			List<AccessControl> accessControls = new ArrayList<AccessControl>();
				accessControlRepository.findAll().forEach(accessControls::add);
			if (accessControls.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(accessControls, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccessControl> getAccessControlById(@PathVariable("id") long id) {
		Optional<AccessControl> tutorialData = accessControlRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping()
	public ResponseEntity<AccessControl> createAccessControl(@RequestBody AccessControl accessControl) {
		try {
			AccessControl _accessControl = accessControlRepository
					.save(new AccessControl(accessControl.getUser(), accessControl.getClient(), accessControl.getApi(), accessControl.getRateLimit(), accessControl.getRateLimitMinutes()));
			return new ResponseEntity<>(_accessControl, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccessControl> updateAccessControl(@PathVariable("id") long id, @RequestBody AccessControl accessControl) {
		Optional<AccessControl> tutorialData = accessControlRepository.findById(id);

		if (tutorialData.isPresent()) {
			AccessControl _accessControl = tutorialData.get();
			_accessControl.setUser(accessControl.getUser());
			_accessControl.setClient(accessControl.getClient());
			_accessControl.setApi(accessControl.getApi());
            _accessControl.setRateLimit(accessControl.getRateLimit());
			return new ResponseEntity<>(accessControlRepository.save(_accessControl), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteAccessControl(@PathVariable("id") long id) {
		try {
			accessControlRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/all")
	public ResponseEntity<HttpStatus> deleteAllAccessControl() {
		try {
			accessControlRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
