package com.ftn.sotis.DTOs;

public class NodeDTO {
	public Long id;
	public String question_text;
	public Long question_id;
	
	public NodeDTO() {
		
	}
	
	public NodeDTO(Long id, String question_text, Long question_id) {
		this.id = id;
		this.question_text = question_text;
		this.question_id = question_id;
	}
}
