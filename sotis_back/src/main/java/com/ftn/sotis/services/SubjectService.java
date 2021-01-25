package com.ftn.sotis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.StudentDTO;
import com.ftn.sotis.DTOs.SubjectDTO;
import com.ftn.sotis.DTOs.SubjectStudentsDTO;
import com.ftn.sotis.entities.Domain;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.entities.User;
import com.ftn.sotis.enums.UserRoleEnum;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.repositories.SubjectRepository;
import com.ftn.sotis.repositories.UserRepository;

@Service
public class SubjectService {

	@Autowired
	SubjectRepository subRep;

	@Autowired
	UserRepository userRep;

	public String addSubject(Subject subject, String teacherUsername) throws EntityAlreadyExistsException, InvalidDataException {
		if (subject.getTitle() == null) throw new EntityAlreadyExistsException("Failed");
		
		if (subRep.findByTitle(subject.getTitle())!=null) throw new InvalidDataException("Title not defined");

		User teacher = userRep.findByUsername(teacherUsername);
		
		Subject newSubject = new Subject(); 
		newSubject.setDomain(new Domain(newSubject));
		newSubject.setTeacher(teacher);
		newSubject.setTitle(subject.getTitle());
		
		subRep.save(newSubject);
		
		return "Successful";
	}
	
	public String editSubject(Subject subject) throws EntityDoesNotExistException, InvalidDataException {
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

	private SubjectDTO castToDTO(Subject sub) {
		SubjectDTO retVal = new SubjectDTO();
		retVal.title = sub.getTitle();
		retVal.teacher = sub.getTeacher() == null ? "" : sub.getTeacher().getUsername();
		return retVal;
	}
	
	private StudentDTO castToDTO(User stud) {
		StudentDTO retVal = new StudentDTO();
		retVal.username = stud.getUsername();
		retVal.index = stud.getStudentId();
		return retVal;
	}
}
