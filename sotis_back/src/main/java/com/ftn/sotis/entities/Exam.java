package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Exam {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Subject subject;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Question> questions;

	@Transient
	private HashMap<String,Integer> patternOccurrences = new HashMap<String,Integer>();

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
	
	public boolean isCorrectAnswer(Long questionId, Long choiceId) {
		for (Question q : questions) {
			if (q.getId().equals(questionId)) {
				return q.isCorrect(choiceId);
			}
		}
		return false;
	}

	public HashMap<String,Double> calculateInitialProbabilities() {
		
		return null;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exam other = (Exam) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
