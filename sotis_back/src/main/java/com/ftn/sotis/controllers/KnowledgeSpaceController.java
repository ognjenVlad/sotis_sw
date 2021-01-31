package com.ftn.sotis.controllers;

import java.util.ArrayList;

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

import com.ftn.sotis.DTOs.GraphDTO;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.KnowledgeSpaceService;

@RestController
@CrossOrigin
@RequestMapping(value = "/ks")
public class KnowledgeSpaceController {
	
	@Autowired
	private KnowledgeSpaceService ksService;
	
	@Autowired
	TokenUtils jwt;

    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> addExpectedKnowledge(@RequestBody GraphDTO graphDto) {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.addExpectedKnowledge(graphDto), HttpStatus.OK);
		} catch (EntityNotFoundException | InvalidDataException | EntityAlreadyExistsException e) {
			GraphDTO g = new GraphDTO();
			g.subject_title = e.getMessage();
			return new ResponseEntity<GraphDTO>(g, HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> getExam() {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.runPisaTest(), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<GraphDTO>(new GraphDTO(), HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> getRealState(@PathVariable Long id) {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.getRealKnowledge(id), HttpStatus.OK);
		} catch (EntityNotFoundException | InvalidDataException e) {
			return new ResponseEntity<GraphDTO>(new GraphDTO(), HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value =  "/bfs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<Node>> getBfs() {
    	try {
			return new ResponseEntity<ArrayList<Node>>(ksService.runPisaTestBfs(), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<ArrayList<Node>>(new ArrayList<Node>(), HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value =  "/problems/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> getDomainNodes(@PathVariable Long id) {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.getDomainNodes(id), HttpStatus.OK);
		} catch (EntityDoesNotExistException e) {
			return new ResponseEntity<GraphDTO>(new GraphDTO(), HttpStatus.BAD_REQUEST);
		}
    }
}
