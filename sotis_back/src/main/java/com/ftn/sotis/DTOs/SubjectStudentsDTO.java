package com.ftn.sotis.DTOs;

import java.util.ArrayList;

public class SubjectStudentsDTO {
	public ArrayList<StudentDTO> assigned;
	public ArrayList<StudentDTO> unassigned;
	
	public SubjectStudentsDTO() {
		this.assigned = new ArrayList<StudentDTO>();
		this.unassigned = new ArrayList<StudentDTO>();
	}
}
