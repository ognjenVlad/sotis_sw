package com.ftn.sotis.DTOs;

import java.util.ArrayList;
import java.util.List;

public class ExamDTO {
	public Long id;
	public Long subjectId;
	public String subjectTitle;
	public List<QuestionDTO> questions = new ArrayList<QuestionDTO>();
}
