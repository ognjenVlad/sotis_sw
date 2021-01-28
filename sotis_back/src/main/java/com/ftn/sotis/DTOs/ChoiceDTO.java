package com.ftn.sotis.DTOs;

public class ChoiceDTO {
	public String text;
	public Boolean correct;
	
	public ChoiceDTO() {}
	
	public ChoiceDTO(String text, Boolean correct) {
		this.text = text;
		this.correct = correct;
	}
}
