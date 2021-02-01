package com.ftn.sotis.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.StudentDTO;
import com.ftn.sotis.entities.Authority;
import com.ftn.sotis.entities.Choice;
import com.ftn.sotis.entities.Domain;
import com.ftn.sotis.entities.Exam;
import com.ftn.sotis.entities.ExamResult;
import com.ftn.sotis.entities.Graph;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.entities.Question;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.entities.User;
import com.ftn.sotis.entities.UserAuthority;
import com.ftn.sotis.enums.UserRoleEnum;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.repositories.AuthorityRepository;
import com.ftn.sotis.repositories.ChoiceRepository;
import com.ftn.sotis.repositories.EdgeRepository;
import com.ftn.sotis.repositories.ExamRepository;
import com.ftn.sotis.repositories.ExamResultRepository;
import com.ftn.sotis.repositories.GraphRepository;
import com.ftn.sotis.repositories.NodeRepository;
import com.ftn.sotis.repositories.QuestionRepository;
import com.ftn.sotis.repositories.SubjectRepository;
import com.ftn.sotis.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	SubjectRepository subRep;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ExamRepository examRep;
	
	@Autowired
	QuestionRepository questionRep;
	
	@Autowired
	ExamResultRepository resultRep;
	
	@Autowired
	ChoiceRepository choiceRep;

	@Autowired
	GraphRepository graphRep;
	
	@Autowired
	NodeRepository nodeRep;
	
	@Autowired
	EdgeRepository edgeRep;

	@Autowired
	ExamResultRepository examResultRep;

	@Autowired
	private AuthorityRepository authRepository;
	
	public User registerUser(User user) throws EntityAlreadyExistsException, InvalidDataException {		
		if(userRepository.findByUsername(user.getUsername()) != null) {
			throw new EntityAlreadyExistsException("Username taken");
		}
		
		boolean valid = true;
		valid = valid && !(user.getPassword() == null);
		valid = valid && !(user.getFirstName() == null);
		valid = valid && !(user.getLastName() == null);
		valid = valid && !(user.getEmail() == null);
		valid = valid && !(user.getRole() == null);
		valid = valid && !(user.getStudentId() == null && user.getRole() == UserRoleEnum.STUDENT_ROLE);
		
		if (!valid) {
			throw new InvalidDataException("Input data not complete");
		}
		
		UserAuthority authorities = new UserAuthority();
		
		Authority auth = authRepository.findByName(user.getRole().name());
		auth.getUserAuthorities().add(authorities);
		authorities.setAuthority(auth);
		authorities.setUser(user);
		user.getUserAuthorities().add(authorities);
		
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		user.setPassword(enc.encode(user.getPassword()));
		userRepository.save(user);

		user.setPassword(null);
		user.setUserAuthorities(null);
		return user;
		
	}
	
	public ArrayList<StudentDTO> getStudents() {
		ArrayList<StudentDTO> retVal = new ArrayList<StudentDTO>();
		for (User user : userRepository.findAll()) {
			if (user.getRole().equals(UserRoleEnum.STUDENT_ROLE)) {
				retVal.add(this.castToDTO(user));
			}
		}
		return retVal;
	}
	
	public Long getStatus(String username) {
		User stud = userRepository.findByUsername(username);
		if (!stud.getRole().equals(UserRoleEnum.STUDENT_ROLE)) return -1L;
		
		return stud.getActiveExam() == null ? -1L : stud.getActiveExam().getExam().getId();
	}
	
	public String persistTestData() {
		if (subRep.findByTitle("test subject") != null) return "Already loaded";
		
		Subject subject = new Subject("test subject", this.userRepository.findByUsername("prof"));
		subject.setDomain(new Domain(subject));
		Exam exam1 = new Exam(subject);
		Exam exam2 = new Exam(subject);
		subject.addQuestionToDomain(null);
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		Question tempQ;
		for (int i = 0; i < 10; i++) {
			tempQ = new Question("Question" + i);
			for (int j = 0; j<3; j++) {
				tempQ.addChoice(new Choice("Question" + i + "Choice" + j, j%2 == 0));
			}
			questions.add(tempQ);
			questionRep.save(tempQ);
			subject.addQuestionToDomain(tempQ);
		}
		
		Graph g = new Graph();
		for (int i = 0; i < 10; i++) {
			if (i < 5) {
				exam1.addQuestion(questions.get(i));
			} else {
				exam2.addQuestion(questions.get(i));
			}
			g.addNode(new Node(questions.get(i)));
		}

		subject.getDomain().setExpectedGraph(g);
		subRep.save(subject);
		examRep.save(exam1);
		examRep.save(exam2);
		ExamResult tempER;

		ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
		for (int i = 0; i < users.size(); i++) {
			if (!users.get(i).getRole().equals(UserRoleEnum.STUDENT_ROLE)) continue;
			
			subject.getStudents().add(users.get(i));
			
			if (i < (users.size()-2)/2) {
				tempER = new ExamResult(exam1,users.get(i));
				for (Question q : exam1.getQuestions()) {
					tempER.addAnswer(q, Math.random() < 0.5);
				}
				tempER.setQuestionsAnswered(5);
				examResultRep.save(tempER);
			} else {
				tempER = new ExamResult(exam2,users.get(i));
				for (Question q : exam2.getQuestions()) {
					tempER.addAnswer(q, Math.random() < 0.5);
				}
				tempER.setQuestionsAnswered(5);
				examResultRep.save(tempER);
			}
		}
		
		return "Subject id: " + subject.getId() + " - Exam1 id: " + exam1.getId() + " - Exam2 id: " + exam2.getId();
	}
	
	private StudentDTO castToDTO(User stud) {
		StudentDTO retVal = new StudentDTO();
		retVal.username = stud.getUsername();
		retVal.index = stud.getStudentId();
		retVal.firstName = stud.getFirstName();
		retVal.lastName = stud.getLastName();
		return retVal;
	}

}
