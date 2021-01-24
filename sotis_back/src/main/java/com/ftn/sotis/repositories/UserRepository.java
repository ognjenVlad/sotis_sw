package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ftn.sotis.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	public User findByUsername(String username);
	
}
