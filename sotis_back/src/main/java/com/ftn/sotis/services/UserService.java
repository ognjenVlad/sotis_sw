package com.ftn.sotis.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.StudentDTO;
import com.ftn.sotis.entities.Authority;
import com.ftn.sotis.entities.User;
import com.ftn.sotis.entities.UserAuthority;
import com.ftn.sotis.enums.UserRoleEnum;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.repositories.AuthorityRepository;
import com.ftn.sotis.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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
	
	private StudentDTO castToDTO(User stud) {
		StudentDTO retVal = new StudentDTO();
		retVal.username = stud.getUsername();
		retVal.index = stud.getStudentId();
		return retVal;
	}

}
