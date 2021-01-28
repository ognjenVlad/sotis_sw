package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Exam {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Subject subject;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Question> questions;
	
	@OneToOne
	private Graph expectedGraph;
	
	@OneToOne
	private Graph calculatedGraph;
	
	public Exam(){
		this.questions = new ArrayList<Question>();
	}
	
	public Exam(Subject subject){
		this();
		this.subject = subject;
	}
	
	public void addQuestion(Question question) {
		this.questions.add(question);
	}

	public void addQuestion(String text, ArrayList<Choice> choices) {
		this.addQuestion(text, choices);
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
	
}
