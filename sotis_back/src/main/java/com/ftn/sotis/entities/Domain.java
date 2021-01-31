package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Domain {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Subject subject;
	
	@OneToMany
	private List<Question> questions;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Graph expectedGraph;
	
	public Domain(){
		this.questions =  new ArrayList<Question>();
	}
	
	public Domain(Subject subject){
		this();
		this.subject = subject;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Graph getExpectedGraph() {
		return expectedGraph;
	}

	public void setExpectedGraph(Graph expectedGraph) {
		this.expectedGraph = expectedGraph;
	}

}
