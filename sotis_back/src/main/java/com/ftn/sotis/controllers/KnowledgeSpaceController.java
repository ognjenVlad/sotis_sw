package com.ftn.sotis.controllers;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

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
import com.ftn.sotis.DTOs.QuestionAnswerDTO;
import com.ftn.sotis.DTOs.QuestionDTO;
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
    		value = "/{exam_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> getRealState(@PathVariable Long exam_id) {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.getRealKnowledge(exam_id), HttpStatus.OK);
		} catch (EntityNotFoundException | InvalidDataException e) {
			GraphDTO g = new GraphDTO();
			g.subject_title = e.getMessage();
			return new ResponseEntity<GraphDTO>(g, HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value =  "/problems/{exam_id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> getDomainNodes(@PathVariable Long exam_id) {
    	try {
			return new ResponseEntity<GraphDTO>(ksService.getExamNodes(exam_id), HttpStatus.OK);
		} catch (EntityDoesNotExistException | InvalidDataException e) {
			GraphDTO g = new GraphDTO();
			g.subject_title = e.getMessage();
			return new ResponseEntity<GraphDTO>(g, HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value =  "/nextQuestion",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDTO> getNextQuestion(@RequestBody QuestionAnswerDTO answerDto, HttpServletRequest request) {
		String token = request.getHeader("X-Auth-Token");
		String username = jwt.getUsernameFromToken(token);

		try {
			return new ResponseEntity<QuestionDTO>(ksService.getNextQuestion(username, answerDto), HttpStatus.OK);
		} catch (Exception e) {
			QuestionDTO q = new QuestionDTO();
			q.text = e.getMessage();
			q.id = -1L;
			return new ResponseEntity<QuestionDTO>(q, HttpStatus.BAD_REQUEST);
		}
    }

    @RequestMapping(
    		value =  "/currentQuestion",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDTO> getNextQuestion(@PathVariable Long examId, HttpServletRequest request) {
		String token = request.getHeader("X-Auth-Token");
		String username = jwt.getUsernameFromToken(token);

		try {
			return new ResponseEntity<QuestionDTO>(ksService.getCurrentQuestion(username, examId), HttpStatus.OK);
		} catch (Exception e) {
			QuestionDTO q = new QuestionDTO();
			q.text = e.getMessage();
			q.id = -1L;
			return new ResponseEntity<QuestionDTO>(q, HttpStatus.BAD_REQUEST);
		}
    }
}
