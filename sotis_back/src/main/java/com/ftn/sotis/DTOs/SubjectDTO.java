package com.ftn.sotis.DTOs;

import java.util.ArrayList;

public class SubjectDTO {
	public Long id;
	public String title;
	public String teacher;
	public ArrayList<StudentDTO> students = new ArrayList<StudentDTO>();
}
