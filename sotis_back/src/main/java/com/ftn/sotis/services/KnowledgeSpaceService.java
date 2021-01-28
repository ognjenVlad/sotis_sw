package com.ftn.sotis.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.EdgeDTO;
import com.ftn.sotis.DTOs.GraphDTO;
import com.ftn.sotis.DTOs.NodeDTO;
import com.ftn.sotis.entities.Edge;
import com.ftn.sotis.entities.Graph;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.repositories.ChoiceRepository;
import com.ftn.sotis.repositories.EdgeRepository;
import com.ftn.sotis.repositories.ExamRepository;
import com.ftn.sotis.repositories.GraphRepository;
import com.ftn.sotis.repositories.NodeRepository;
import com.ftn.sotis.repositories.QuestionRepository;
import com.ftn.sotis.repositories.SubjectRepository;
import com.ftn.sotis.repositories.UserRepository;

@Service
public class KnowledgeSpaceService {

	@Autowired
	SubjectRepository subRep;

	@Autowired
	UserRepository userRep;
	
	@Autowired
	ExamRepository examRep;
	
	@Autowired
	QuestionRepository questionRep;
	
	@Autowired
	ChoiceRepository choiceRep;

	@Autowired
	GraphRepository graphRep;
	
	@Autowired
	NodeRepository nodeRep;
	
	@Autowired
	EdgeRepository edgeRe;
	
	public GraphDTO runPisaTest() {
		Graph g = new Graph();
		String s = null;
		ProcessBuilder builder = new ProcessBuilder("python","kst/pisaTest.py");
		
		try {
			Process p = builder.start();
			p.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));
	        
			String[] pair = new String[2];
			pair = stdInput.readLine().split("\\.");
			
			Node temp_n1 = new Node(Long.parseLong(pair[0]));
			Node temp_n2 = new Node(Long.parseLong(pair[1]));
			
			g.addNode(temp_n1);
			g.addNode(temp_n2);
			g.addEdge(temp_n1, temp_n2);
			
            while ((s = stdInput.readLine()) != null) {
    			pair = stdInput.readLine().split("\\.");
    			temp_n1 = new Node(Long.parseLong(pair[0]));
    			temp_n2 = new Node(Long.parseLong(pair[1]));
    			g.addNode(temp_n1);
    			g.addNode(temp_n2);
    			g.addEdge(temp_n1, temp_n2);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this.castToDTO(g);
	}
	
	
	public GraphDTO castToDTO(Graph g) {
		GraphDTO retVal = new GraphDTO();
		for (Node node : g.getNodes()) {
			retVal.nodes.add(new NodeDTO(node.getId(),"test-text"));
		}
		for (Edge edge : g.getEdges()) {
			retVal.edges.add(new EdgeDTO(edge.getSource().getId(),edge.getDestination().getId()));
		}
		
		return retVal;
	}
	
}
