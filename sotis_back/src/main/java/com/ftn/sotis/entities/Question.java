package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String text;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Choice> choices;
	
	public Question(){
		this.choices = new ArrayList<Choice>();
	}
	
	public Question(String text){
		this();
		this.text = text;
	}
	
	Question(String text, ArrayList<Choice> choices){
		this();
		this.text = text;
		this.choices = choices;
	}
	
	public void addChoice(Choice choice) {
		this.choices.add(choice);
	}

	public void addChoice(String text, Boolean correct) {
		this.choices.add(new Choice(text,correct));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	
}
