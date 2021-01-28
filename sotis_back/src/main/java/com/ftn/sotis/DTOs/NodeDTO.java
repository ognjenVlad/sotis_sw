package com.ftn.sotis.DTOs;

public class NodeDTO {
	public Long id;
	public String question_text;
	
	public NodeDTO() {
		
	}
	
	public NodeDTO(Long id, String question_text) {
		this.id = id;
		this.question_text = question_text;
	}
}
