package com.ftn.sotis.DTOs;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {
	public String text;
	public List<ChoiceDTO> choices = new ArrayList<ChoiceDTO>();
}
