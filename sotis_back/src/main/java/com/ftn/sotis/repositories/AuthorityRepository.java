package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ftn.sotis.entities.Authority;


public interface AuthorityRepository extends JpaRepository<Authority, Long>{
	Authority findByName(String name);
}
