package com.ftn.sotis.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.ChoiceDTO;
import com.ftn.sotis.DTOs.ExamDTO;
import com.ftn.sotis.DTOs.QuestionDTO;
import com.ftn.sotis.entities.Choice;
import com.ftn.sotis.entities.Exam;
import com.ftn.sotis.entities.Question;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.repositories.ChoiceRepository;
import com.ftn.sotis.repositories.ExamRepository;
import com.ftn.sotis.repositories.QuestionRepository;
import com.ftn.sotis.repositories.SubjectRepository;
import com.ftn.sotis.repositories.UserRepository;

@Service
public class ExamService {

	@Autowired
	SubjectRepository subRep;

	@Autowired
	UserRepository userRep;
	
	@Autowired
	ExamRepository examRep;
	
	@Autowired
	QuestionRepository questionRep;
	
	@Autowired
	ChoiceRepository choiceRep;

	public String addExam(ExamDTO examDto) throws EntityAlreadyExistsException, InvalidDataException {
		Subject subject;
		if ((subject = subRep.findByTitle(examDto.subjectTitle)) == null) throw new InvalidDataException("Subjec with given title not found");

		Exam exam = this.castFromDTO(examDto);
		
		for (Question q : exam.getQuestions()) {
			if (q.getText() == null) throw new InvalidDataException("Question text not given");
			for (Choice c : q.getChoices()) {
				if (c.getText() == null) throw new InvalidDataException("Choice text not given");
				if (c.getCorrect() == null) throw new InvalidDataException("Choice correctness not given");
			}
		}
		
		subject.addExam(exam);
		exam.setSubject(subject);
		examRep.save(exam);
		subRep.save(subject);
		return "Successful";
	}
	
	public String editExam(ExamDTO examDto, Long id) throws EntityDoesNotExistException, InvalidDataException {
		Exam exam;
		if((exam = examRep.findById(id).orElse(null)) == null)
			throw new InvalidDataException("Exam with given id not found");
		
		for (Question q : exam.getQuestions()) {
			questionRep.delete(q);
		}
		
		exam.getQuestions().clear();
		ArrayList<Choice> choices;
		
		if (examDto.questions != null) {
			for (QuestionDTO question : examDto.questions) {
				if (question.text == null) throw new InvalidDataException("Invalid data. Question text not given");
				
				choices = new ArrayList<Choice>();
				for (ChoiceDTO choice : question.choices) {
					if (choice.text == null) throw new InvalidDataException("Invalid data. Choice text not given");
					if (choice.correct == null) throw new InvalidDataException("Invalid data. Choice correctness not given");
					
					choices.add(new Choice(choice.text, choice.correct));
				}
				exam.getQuestions().add(new Question(question.text, choices));
			}
		}
		
		examRep.save(exam);
		return "Successful";
	}
	
	public String removeExam(Long id) throws EntityDoesNotExistException {
		if (examRep.findById(id).orElse(null) == null) {
			throw new EntityDoesNotExistException("Failed");
		}
		
		examRep.deleteById(id);

		return "Successful";
	}
	
	public ExamDTO getExam(Long id) {
		Exam exam = examRep.findById(id).orElse(null);
		
		if (exam == null) throw new EntityNotFoundException("Exam with given id not found");
		
		return this.castToDTO(exam);
	}
		
	public List<ExamDTO> getAllExams(Long id){
		List<ExamDTO> retVal = new ArrayList<ExamDTO>();
		
		for (Exam exam : examRep.findAll()) {
			if (!exam.getSubject().getId().equals(id)) continue;
			retVal.add(this.castToDTO(exam));
		}
		
		return retVal;
	}

	private Exam castFromDTO(ExamDTO examDto) {
		Exam retVal = new Exam();
		for (QuestionDTO questionDto : examDto.questions) {
			retVal.addQuestion(this.castFromDTO(questionDto));
		}
		return retVal;
	}
	
	private Question castFromDTO(QuestionDTO questionDto) {
		Question retVal = new Question(questionDto.text);
		for (ChoiceDTO choiceDto : questionDto.choices) {
			retVal.addChoice(choiceDto.text, choiceDto.correct);
		}
		return retVal;
	}
	
	private ExamDTO castToDTO(Exam exam) {
		ExamDTO retVal = new ExamDTO();
		retVal.subjectTitle = exam.getSubject().getTitle();
		
		retVal.questions = new ArrayList<QuestionDTO>();
		for (Question question : exam.getQuestions()) {
			retVal.questions.add(this.castToDTO(question));
		}
		return retVal;
	}
	
	private QuestionDTO castToDTO(Question question) {
		QuestionDTO retVal = new QuestionDTO();
		retVal.text = question.getText();
		
		retVal.choices = new ArrayList<ChoiceDTO>();
		for (Choice choice : question.getChoices()) {
			retVal.choices.add(new ChoiceDTO(choice.getText(), choice.getCorrect()));
		}
		
		return retVal;
	}
}
