package com.ftn.sotis.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.math.ec.ScaleYPointMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.EdgeDTO;
import com.ftn.sotis.DTOs.GraphDTO;
import com.ftn.sotis.DTOs.NodeDTO;
import com.ftn.sotis.entities.Edge;
import com.ftn.sotis.entities.ExamResult;
import com.ftn.sotis.entities.Graph;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.entities.Question;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.exceptions.EntityAlreadyExistsException;
import com.ftn.sotis.exceptions.EntityDoesNotExistException;
import com.ftn.sotis.exceptions.InvalidDataException;
import com.ftn.sotis.repositories.ChoiceRepository;
import com.ftn.sotis.repositories.EdgeRepository;
import com.ftn.sotis.repositories.ExamRepository;
import com.ftn.sotis.repositories.ExamResultRepository;
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
	ExamResultRepository resultRep;
	
	@Autowired
	ChoiceRepository choiceRep;

	@Autowired
	GraphRepository graphRep;
	
	@Autowired
	NodeRepository nodeRep;
	
	@Autowired
	EdgeRepository edgeRe;
	
	public GraphDTO addExpectedKnowledge(GraphDTO graphDto) throws InvalidDataException, EntityAlreadyExistsException {
		Graph g = new Graph();
		Question temp;
		ArrayList<Long> questionIds = new ArrayList<Long>();
		Map<Long, Node> questionNodeMapping = new HashMap<Long, Node>();
		
		for (NodeDTO n : graphDto.nodes) {
			temp = questionRep.findById(n.question_id).orElse(null);
			if (temp == null) throw new InvalidDataException("Question with ID " + n.question_id + "not found");
			if (questionIds.contains(n.question_id)) throw new InvalidDataException("Question with ID " + n.question_id + " represents multiple nodes");
			g.addNode(new Node(temp));
			questionIds.add(n.question_id);
			questionNodeMapping.put(n.question_id, g.getNodes().get(g.getNodes().size()-1));
		}
		
		for (EdgeDTO e : graphDto.edges) {
			if (!questionIds.contains(e.source)) throw new InvalidDataException("Edge not valid. Question with ID " + e.source + " not found");
			if (!questionIds.contains(e.destination)) throw new InvalidDataException("Edge not valid. Question with ID " + e.destination + " not found");
			g.addEdge(questionNodeMapping.get(e.source),questionNodeMapping.get(e.destination));
		}
		
		if (!g.isValid()) throw new InvalidDataException("Nodes are not connected or graph is cyclic");
		
		g = graphRep.save(g);
		
		return this.castToDTO(g);
	}
	
	public GraphDTO getDomainNodes(Long id) throws EntityDoesNotExistException {
		Graph g = new Graph();
		Subject sub;
		
		if ((sub = this.subRep.findById(id).orElse(null)) == null) throw new EntityDoesNotExistException("Subject with given ID not found");
		
		for (Question q : sub.getDomain().getQuestions()) {
			g.addNode(new Node(q));
		}
		
		return this.castToDTO(g);
	}
	
	public GraphDTO getRealKnowledge(Long id) throws InvalidDataException {
		Subject subject = subRep.findById(id).orElse(null);
		
		if (subject == null) throw new InvalidDataException("Subject with given ID not found");
		
		ArrayList<String> results = new ArrayList<String>(); 
		
		
		for (ExamResult result : resultRep.findAll()) {
			if (subject.getExams().contains(result.getExam()) && result.isFinished()) {
				System.out.println("Get result array ->" + result.getResultsArray());
				results.add(result.getResultsArray());
			}
		}
		
		System.out.println("Size of result : " + results.size());
		
		String pythonArguments = "";
				
		System.out.println(pythonArguments);
		
		//pythonArguments = pythonArguments.substring(0, pythonArguments.length() - 1);
	
		
		System.out.println(pythonArguments);
		
		Graph g = new Graph();
		String s;
		ProcessBuilder builder = new ProcessBuilder("python","kst/evaluateIita.py",pythonArguments);
		
		try {
			Process p = builder.start();
			p.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));
	        
//			String[] pair = new String[2];
//			pair = stdInput.readLine().split("\\.");
//			
//			Node temp_n1 = new Node(Long.parseLong(pair[0]));
//			Node temp_n2 = new Node(Long.parseLong(pair[1]));
//			
//			g.addNode(temp_n1);
//			g.addNode(temp_n2);
//			g.addEdge(temp_n1, temp_n2);
			System.out.println("Read next");
            while ((s = stdInput.readLine()) != null) {
            	System.out.println("NOT-ERROR:" + s);
//    			pair = s.split("\\.");
//    			temp_n1 = new Node(Long.parseLong(pair[0]));
//    			temp_n2 = new Node(Long.parseLong(pair[1]));
//    			g.addNode(temp_n1);
//    			g.addNode(temp_n2);
//    			g.addEdge(temp_n1, temp_n2);
            }
            BufferedReader stdError = new BufferedReader(new 
                    InputStreamReader(p.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                System.out.println("ERROR:" +s);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
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
    			pair = s.split("\\.");
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
	
	public ArrayList<Node> runPisaTestBfs() {
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
    			pair = s.split("\\.");
    			temp_n1 = new Node(Long.parseLong(pair[0]));
    			temp_n2 = new Node(Long.parseLong(pair[1]));
    			g.addNode(temp_n1);
    			g.addNode(temp_n2);
    			g.addEdge(temp_n1, temp_n2);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}

		return g.getNextBfs(0L);
	}
	
	public GraphDTO castToDTO(Graph g) {
		GraphDTO retVal = new GraphDTO();
		Long qId;
		for (Node node : g.getNodes()) {
			qId = node.getQuestion() == null ? -1L : node.getQuestion().getId();
			retVal.nodes.add(new NodeDTO(node.getId(),"test-text", qId));
		}
		for (Edge edge : g.getEdges()) {
			retVal.edges.add(new EdgeDTO(edge.getSource().getId(),edge.getDestination().getId()));
		}
		
		return retVal;
	}
	
	
	
}
