package com.ftn.sotis.DTOs;

public class EdgeDTO {
	public Long source;
	public Long destination;
	
	public EdgeDTO() {
		
	}
	
	public EdgeDTO(Long source, Long destination) {
		this.source = source;
		this.destination = destination;
	}
}
