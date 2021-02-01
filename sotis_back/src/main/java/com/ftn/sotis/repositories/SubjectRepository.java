package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sotis.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
	Subject findByTitle(String title);

}
