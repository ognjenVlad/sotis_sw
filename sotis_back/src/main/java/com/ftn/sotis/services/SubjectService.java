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

	public String addSubject(SubjectDTO subjectDto, String teacherUsername) throws EntityAlreadyExistsException, InvalidDataException {
		if (subjectDto.title == null) throw new InvalidDataException("Title not defined");
		
		if (subRep.findByTitle(subjectDto.title)!=null) throw new EntityAlreadyExistsException("Subject with given title already exists");

		Subject subject = castFromDTO(subjectDto); 
		subject.setTitle(subjectDto.title);
		subject.setTeacher(userRep.findByUsername(teacherUsername));
		subject.setDomain(new Domain(subject));
		
		subRep.save(subject);
		
		return "Successful";
	}
	
	public String editSubject(Long id, SubjectDTO subjectDto) throws EntityDoesNotExistException, InvalidDataException {
		Subject subject;
		
		if ((subject = subRep.findById(id).orElse(null)) == null) {
			throw new InvalidDataException("Subject with given title not found");
		}
		
		if (subjectDto.title == null) throw new InvalidDataException("Invalid data. Title not defined");
		subject.setTitle(subjectDto.title);
		subject.getStudents().clear();
		
		if (subject.getStudents() != null) {
			for (StudentDTO stud : subjectDto.students) {
				if(userRep.findByUsername(stud.username) != null) {
					subject.getStudents().add(userRep.findByUsername(stud.username));
				}else {
					throw new InvalidDataException("Student with given username " + stud.username + " not found");
				}
			}
		}
		
		subRep.save(subject);
		return "Successful";
	}
	
	public String removeSubject(Long id) throws EntityDoesNotExistException {
		if (subRep.findById(id).orElse(null) == null) {
			throw new EntityDoesNotExistException("Failed");
		}
		
		subRep.delete(subRep.findById(id).orElse(null));

		return "Successful";
	}
	
	public SubjectStudentsDTO getSubjectStudents(Long id) throws EntityNotFoundException {
		SubjectStudentsDTO retVal = new SubjectStudentsDTO();
		Subject foundSubject;
		
		if ((foundSubject = subRep.findById(id).orElse(null)) == null) throw new EntityNotFoundException("Subject with given title not found");

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
	
	public List<SubjectDTO> getMySubjects(String username){
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
	
	
	/**
	 * Finds students in UserRepository and throws exception if one of them is not found
	 * 
	 * @param subjectDto
	 * @return
	 * @throws InvalidDataException
	 */
	private Subject castFromDTO(SubjectDTO subjectDto) throws InvalidDataException {
		Subject retVal = new Subject();
		User foundStud;
				
		if (subjectDto.students == null) return retVal;

		for (StudentDTO stud : subjectDto.students) {
			if ((foundStud = userRep.findByUsername(stud.username)) == null) {
				throw new InvalidDataException("Student with given username not found");
			}
			else {
				retVal.getStudents().add(foundStud);				
			}
		}
		
		return retVal;
	}
}
