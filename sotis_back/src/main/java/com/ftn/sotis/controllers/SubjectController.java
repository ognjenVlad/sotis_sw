package com.ftn.sotis.controllers;

import java.util.List;

import javax.persistence.EntityNotFoundException;
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

import com.ftn.sotis.DTOs.SubjectDTO;
import com.ftn.sotis.DTOs.SubjectStudentsDTO;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.SubjectService;

@RestController
@CrossOrigin
//@CrossOrigin(origins = "192.168.0.12:8080")
@RequestMapping(value = "/subject")
public class SubjectController {
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	TokenUtils jwt;

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSubject(@RequestBody SubjectDTO subject, HttpServletRequest request) {
		String token = request.getHeader("Auth-Token");
		String username = jwt.getUsernameFromToken(token);
		
    	try {
			return new ResponseEntity<String>(subjectService.addSubject(subject,username), HttpStatus.OK);
		} catch (EntityAlreadyExistsException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeSubject(@PathVariable Long id) {
    	try {
			return new ResponseEntity<String>(subjectService.removeSubject(id),HttpStatus.OK);
		} catch (EntityDoesNotExistException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editSubject(@RequestBody SubjectDTO subject) {
    	try {
			return new ResponseEntity<String>(subjectService.editSubject(subject),HttpStatus.OK);
		} catch (EntityDoesNotExistException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/students/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectStudentsDTO> getSubjectStudents(@PathVariable Long id) {
    	try {
			return new ResponseEntity<SubjectStudentsDTO>(subjectService.getSubjectStudents(id),HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<SubjectStudentsDTO>(new SubjectStudentsDTO(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubjectDTO>> getSubjects(HttpServletRequest request) {
		String token = request.getHeader("Auth-Token");
		String username = jwt.getUsernameFromToken(token);
		return new ResponseEntity<List<SubjectDTO>>(subjectService.getMySubjects(username),HttpStatus.OK);
    }
}
