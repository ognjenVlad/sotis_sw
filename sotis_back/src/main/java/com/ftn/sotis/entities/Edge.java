package com.ftn.sotis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Edge {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Node source;
	
	@ManyToOne
	private Node destination;
	
	public Edge() {
		
	}
	
	public Edge(Node source, Node destination) {
		this.source = source;
		this.destination = destination;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}
	
	public boolean contains(Node node) {
		if (this.source.equals(node)) return true;
		if (this.destination.equals(node)) return true;
		
		return false;
	}
}
