package com.ftn.sotis.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.sotis.DTOs.ChoiceDTO;
import com.ftn.sotis.DTOs.EdgeDTO;
import com.ftn.sotis.DTOs.GraphDTO;
import com.ftn.sotis.DTOs.NodeDTO;
import com.ftn.sotis.DTOs.QuestionDTO;
import com.ftn.sotis.entities.Choice;
import com.ftn.sotis.entities.Edge;
import com.ftn.sotis.entities.Exam;
import com.ftn.sotis.entities.ExamResult;
import com.ftn.sotis.entities.Graph;
import com.ftn.sotis.entities.Node;
import com.ftn.sotis.entities.Question;
import com.ftn.sotis.entities.Subject;
import com.ftn.sotis.entities.User;
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
	EdgeRepository edgeRep;
	
	public GraphDTO addExpectedKnowledge(GraphDTO graphDto) throws InvalidDataException, EntityAlreadyExistsException {
		if (graphDto.subject_id == null) throw new InvalidDataException("Subject id must be defined");
		Subject subject =  subRep.findById(graphDto.subject_id).orElse(null);
		if (subject == null) throw new InvalidDataException("Subject with given id not found");
		
		Graph g = subject.getDomain().getExpectedGraph();
		if (g == null) throw new InvalidDataException("Graph not defined for this exam");
		
		HashMap<Long, Node> nodesMap = new HashMap<Long, Node>();

		g.setEdges(new ArrayList<Edge>());
		
		for (Node n : g.getNodes()) {
			nodesMap.put(n.getId(), n);
		}
		
		for (NodeDTO n : graphDto.nodes) {
			if (!nodesMap.containsKey(n.id)) throw new InvalidDataException("Node with id " + n.id + " not found");
		}
		
		for (EdgeDTO e : graphDto.edges) {
			if (!nodesMap.containsKey(e.source)) throw new InvalidDataException("Edge source node with id " + e.source + " not found ");
			if (!nodesMap.containsKey(e.destination)) throw new InvalidDataException("Edge destination node with id " + e.destination + " not found ");
			if (!g.addEdge(nodesMap.get(e.source), nodesMap.get(e.destination))) 
				throw new InvalidDataException("Invalid request to create edge " + e.source + " - " + e.destination);
		}
		
		if (!g.isValid()) throw new InvalidDataException("Nodes are not connected or graph is cyclic");
		
		subRep.save(subject);
		
		return this.castToDTO(g);
	}
	
	public GraphDTO getExamNodes(Long subject_id) throws EntityDoesNotExistException, InvalidDataException {
		if (subject_id == null) throw new InvalidDataException("Exam id must be defined");
		Graph g;
		Subject subject;
		
		if ((subject = this.subRep.findById(subject_id).orElse(null)) == null) throw new EntityDoesNotExistException("Subject with given ID not found");
		
		g = subject.getDomain().getExpectedGraph();
		if (g == null) throw new InvalidDataException("Nodes not defined for this exam");
		
		return this.castToDTO(g);
	}
	
	public GraphDTO getRealKnowledge(Long exam_id) throws InvalidDataException {
		if (exam_id == null) throw new InvalidDataException("Exam id must be defined");
		Exam exam = examRep.findById(exam_id).orElse(null);
		
		if (exam == null) throw new InvalidDataException("Exam with given ID not found");
		
		ArrayList<String> results = new ArrayList<String>(); 
		ExamResult helpER = null;

		for (ExamResult result : resultRep.findAll()) {
			if (exam.equals(result.getExam()) && result.isFinished()) {
				results.add(result.getResultsArray());
				helpER = result;
			}
		}

		if (helpER == null) throw new InvalidDataException("Exam has no results yet.");
		
		String pythonArguments = helpER.getQuestionIdsOrdered() + ".";
		for (String s : results) {
			pythonArguments += s + ".";
		}
		pythonArguments = pythonArguments.substring(0, pythonArguments.length() - 1);
		
		String s;
		ProcessBuilder builder = new ProcessBuilder("python","kst/evaluateIita.py",pythonArguments);
		
		HashMap<Long, Node> domainNodes = new HashMap<Long, Node>();
		for (Node n : nodeRep.findAll()) {
			domainNodes.put(n.getId(), n);
		}
		
		ArrayList<String> edgesStr = new ArrayList<String>();
		
		try {
			Process p = builder.start();
			p.waitFor();
			
            BufferedReader stdError = new BufferedReader(new 
                    InputStreamReader(p.getErrorStream()));
            
            if (stdError.readLine() != null) throw new InvalidDataException("Invalid data, IITA algorithm failed");

            BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));
	        
            while ((s = stdInput.readLine()) != null) {
            	edgesStr.add(s);
            }
		} catch (Exception e) {
			throw new InvalidDataException("Invalid data, IITA algorithm failed");
		}
		
		Graph g = new Graph();
		for (String edgeStr : edgesStr) {
			this.evaluateEdgeString(g, domainNodes, edgeStr);
		}
		
		Graph retVal = exam.getSubject().getDomain().getExpectedGraph().getSubgraphDifference(g);
		
		return castToDTO(retVal);
	}

	private void evaluateEdgeString(Graph g, HashMap<Long, Node> nodes, String edgeStr) {
		System.out.println(edgeStr);
		String node1_id = edgeStr.split("\\.")[0];
		String node2_id = edgeStr.split("\\.")[1];
		
		Node node1 = nodes.get(Long.parseLong(node1_id));
		Node node2 = nodes.get(Long.parseLong(node2_id));
		
		if (!g.getNodes().contains(nodes.get(node1.getId()))) g.getNodes().add(nodes.get(node1.getId()));
		if (!g.getNodes().contains(nodes.get(node2.getId()))) g.getNodes().add(nodes.get(node2.getId()));
		
		g.addEdge(node1, node2);
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
	
	public QuestionDTO getNextQuestion(String username, Long exam_id) throws InvalidDataException, EntityDoesNotExistException {
		User stud = userRep.findByUsername(username);
		
		if (exam_id == null) return null;
		Exam exam = examRep.findById(exam_id).orElse(null);
		if (exam == null) throw new EntityDoesNotExistException("Exam with given id not found");
		
		Integer questionsAnswered = stud.getActiveExam().getQuestionsAnswered();
		
		Graph g = exam.getSubject().getDomain().getExpectedGraph();
		Question[] questionPair = g.getNextQuestion(exam, questionsAnswered);
		
		return this.castToDTO(questionPair[1]);
	}
	
	private GraphDTO castToDTO(Graph g) {
		GraphDTO retVal = new GraphDTO();
		Long qId;
		for (Node node : g.getNodes()) {
			qId = node.getQuestion() == null ? -1L : node.getQuestion().getId();
			retVal.nodes.add(new NodeDTO(node.getId(),"test-text", qId));
		}
		for (Edge edge : g.getEdges()) {
			retVal.edges.add(new EdgeDTO(edge.getSource().getId(),edge.getDestination().getId(),edge.getStatus()));
		}
		
		return retVal;
	}
	
	private QuestionDTO castToDTO(Question question) {
		QuestionDTO retVal = new QuestionDTO();
		retVal.text = question.getText();
		
		retVal.choices = new ArrayList<ChoiceDTO>();
		for (Choice choice : question.getChoices()) {
			retVal.choices.add(new ChoiceDTO(choice.getText(), null));
		}
		
		return retVal;
	}
	
}
