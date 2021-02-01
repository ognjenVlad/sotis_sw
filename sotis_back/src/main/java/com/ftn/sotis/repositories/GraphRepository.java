package com.ftn.sotis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ftn.sotis.entities.Graph;

public interface GraphRepository extends JpaRepository<Graph, Long> {

}
