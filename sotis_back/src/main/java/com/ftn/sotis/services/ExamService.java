package com.ftn.sotis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.ChoiceDTO;
import com.ftn.sotis.DTOs.ExamDTO;
import com.ftn.sotis.DTOs.QuestionDTO;
import com.ftn.sotis.DTOs.SubjectDTO;
import com.ftn.sotis.DTOs.SubjectStudentsDTO;
import com.ftn.sotis.entities.Choice;
import com.ftn.sotis.entities.Exam;
import com.ftn.sotis.entities.Question;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.entities.User;
import com.ftn.sotis.enums.UserRoleEnum;
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
		examRep.save(exam);
		subRep.save(subject);
		return "Successful";
	}
	
	public String editExam(Subject subject) throws EntityDoesNotExistException, InvalidDataException {
		Subject foundSubject;
		if((foundSubject = subRep.findByTitle(subject.getTitle())) == null) {
			throw new InvalidDataException("Subject with given title not found");
		}
		
		foundSubject.getStudents().clear();
		
		if (subject.getStudents() != null) {
			for (User stud : subject.getStudents()) {
				if(userRep.findByUsername(stud.getUsername()) != null) {
					foundSubject.getStudents().add(userRep.findByUsername(stud.getUsername()));
				}else {
					throw new InvalidDataException("Student with given username " + stud.getUsername() + " not found");
				}
			}
		}
		
		subRep.save(foundSubject);
		return "Successful";
	}
	
	public String removeSubject(String title) throws EntityDoesNotExistException {
		if (subRep.findByTitle(title)==null) {
			throw new EntityDoesNotExistException("Failed");
		}
		
		subRep.delete(subRep.findByTitle(title));

		return "Successful";
	}
	
	public SubjectStudentsDTO getSubjectStudents(String title) throws EntityNotFoundException {
		SubjectStudentsDTO retVal = new SubjectStudentsDTO();
		Subject foundSubject;
		
		if ((foundSubject = subRep.findByTitle(title)) == null) throw new EntityNotFoundException("Subject with given title not found");

		TreeSet<String> subjStudents = new TreeSet<String>();
		for (User stud : foundSubject.getStudents()) {
			subjStudents.add(stud.getUsername());
			retVal.assigned.add(this.castToDTO(stud));
		}
		
		for (User user : userRep.findAll()) {
			// If it's actually student, and is not assigned to this subject
			if (user.getRole().equals(UserRoleEnum.STUDENT_ROLE) && !subjStudents.contains(user.getUsername())) {
				retVal.unassigned.add(this.castToDTO(user));
			}
		}
		
		return retVal;
	}
	
	public List<SubjectDTO> getAllSubjects(){
		List<SubjectDTO> retVal = new ArrayList<SubjectDTO>();
		
		for (Subject sub : subRep.findAll()) {
			retVal.add(this.castToDTO(sub));
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
}
