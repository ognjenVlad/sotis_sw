package com.ftn.sotis.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.SubjectService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(value = "/subject")
public class SubjectController {
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	TokenUtils jwt;

	@PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @RequestMapping(
    		value = "/role",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSubject(@RequestBody Subject subject, HttpServletRequest request) {
		String token = request.getHeader("Auth-Token");
		String username = jwt.getUsernameFromToken(token);
    	try {
			return new ResponseEntity<String>(subjectService.addSubject(subject,username), HttpStatus.OK);
		} catch (EntityAlreadyExistsException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

	@PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @RequestMapping(
    		value = "/role/{name}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeSubject(@PathVariable String name) {
    	try {
			return new ResponseEntity<String>(subjectService.removeSubject(name),HttpStatus.OK);
		} catch (EntityDoesNotExistException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

	@PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @RequestMapping(
    		value = "/role",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editSubject(@RequestBody Subject subject) {
    	try {
			return new ResponseEntity<String>(subjectService.editSubject(subject),HttpStatus.OK);
		} catch (EntityDoesNotExistException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }
}
