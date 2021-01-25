package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sotis.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

}
