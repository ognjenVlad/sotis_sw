package com.ftn.sotis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.ftn.sotis.enums.EdgeStatusEnum;

@Entity
public class Edge {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Node source;
	
	@ManyToOne
	private Node destination;
	
	private EdgeStatusEnum status;
	
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		char c='c', h='h', a='a', r='r';
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + c + h + a + r;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	public EdgeStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EdgeStatusEnum status) {
		this.status = status;
	}
}
