package com.mtb.twitter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtb.twitter.services.ValidationService;

@RestController
@RequestMapping(value = "/validate")
public class ValidationController {

	public ValidationService validationService;
	
	public ValidationController(ValidationService validationService) {
		this.validationService = validationService;
	}
	
	@GetMapping("/tag/exists/{label}")
	public ResponseEntity<Boolean> validateTagExists(@PathVariable String label) {
		return new ResponseEntity<Boolean>(validationService.validateTagExists(label), HttpStatus.OK);
	}
	
	@GetMapping("/username/exists/@{username}")
	public ResponseEntity<Boolean> validateUsernameExists(@PathVariable String username) {
		return new ResponseEntity<Boolean>(validationService.validateUsernameExists(username), HttpStatus.OK);
	}
	
	@GetMapping("/username/available/@{username}")
	public ResponseEntity<Boolean> validateUsernameAvailable(@PathVariable String username) {
		return new ResponseEntity<Boolean>(validationService.validateUsernameAvailable(username), HttpStatus.OK);
	}
}
