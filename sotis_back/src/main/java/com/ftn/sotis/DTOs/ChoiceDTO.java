package com.ftn.sotis.DTOs;

public class ChoiceDTO {
	public Long choiceId;
	public String text;
	public Boolean correct;
	
	public ChoiceDTO() {}
	
	public ChoiceDTO(String text, Boolean correct) {
		this.text = text;
		this.correct = correct;
	}

	public ChoiceDTO(Long choiceId, String text, Boolean correct) {
		this.choiceId = choiceId;
		this.text = text;
		this.correct = correct;
	}
}
