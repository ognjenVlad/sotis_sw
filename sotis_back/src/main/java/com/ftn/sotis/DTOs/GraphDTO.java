package com.ftn.sotis.DTOs;

import java.util.ArrayList;

public class GraphDTO {
	public String subject_title;
	public ArrayList<NodeDTO> nodes;
	public ArrayList<EdgeDTO> edges;
	
	public GraphDTO() {
		this.nodes = new ArrayList<NodeDTO>();
		this.edges = new ArrayList<EdgeDTO>();
	}
	
}
