package com.ftn.sotis.controllers;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.sotis.DTOs.GraphDTO;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.security.TokenUtils;
import com.ftn.sotis.services.KnowledgeSpaceService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(value = "/ks")
public class KnowledgeSpaceController {
	
	@Autowired
	private KnowledgeSpaceService ksService;
	
	@Autowired
	TokenUtils jwt;

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

}
