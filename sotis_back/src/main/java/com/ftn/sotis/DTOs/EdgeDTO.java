package com.ftn.sotis.DTOs;

import com.ftn.sotis.enums.EdgeStatusEnum;

public class EdgeDTO {
	public Long source;
	public Long destination;
	public EdgeStatusEnum status;
	
	public EdgeDTO() {
		
	}
	
	public EdgeDTO(Long source, Long destination) {
		this.source = source;
		this.destination = destination;
	}

	public EdgeDTO(Long source, Long destination, EdgeStatusEnum status) {
		this.source = source;
		this.destination = destination;
		this.status = status;
	}
}
