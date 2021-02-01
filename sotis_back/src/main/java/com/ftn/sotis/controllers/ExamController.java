package com.ftn.sotis.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sotis.DTOs.ExamDTO;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.ExamService;

@RestController
@CrossOrigin
@RequestMapping(value = "/exam")
public class ExamController {

	@Autowired
	private ExamService examService;
	
	@Autowired
	TokenUtils jwt;

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addExam(@RequestBody ExamDTO examDto) {
    	try {
			return new ResponseEntity<String>(examService.addExam(examDto), HttpStatus.OK);
		} catch (EntityAlreadyExistsException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeExam(@PathVariable Long id) {
    	try {
			return new ResponseEntity<String>(examService.removeExam(id),HttpStatus.OK);
		} catch (EntityDoesNotExistException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editExam(@RequestBody ExamDTO examDto) {
    	try {
			return new ResponseEntity<String>(examService.editExam(examDto),HttpStatus.OK);
		} catch (EntityDoesNotExistException | InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamDTO> getExam(@PathVariable Long id) {
    	try {
			return new ResponseEntity<ExamDTO>(examService.getExam(id),HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<ExamDTO>(new ExamDTO(), HttpStatus.BAD_REQUEST);
		}
    }
	
//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/all/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExamDTO>> getAllExams(@PathVariable Long id) {
    	try {
			return new ResponseEntity<List<ExamDTO>>(examService.getAllExams(id),HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<List<ExamDTO>>(new ArrayList<ExamDTO>(), HttpStatus.BAD_REQUEST);
		}
    }

//	@PreAuthorize("hasAuthority('PROFESSOR_ROLE')")
    @RequestMapping(
    		value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExamDTO>> getAllExams() {
    	try {
			return new ResponseEntity<List<ExamDTO>>(examService.getAll(),HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<List<ExamDTO>>(new ArrayList<ExamDTO>(), HttpStatus.BAD_REQUEST);
		}
    }
}