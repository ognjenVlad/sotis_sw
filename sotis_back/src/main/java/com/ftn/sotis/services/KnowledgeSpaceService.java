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
import com.ftn.sotis.DTOs.QuestionAnswerDTO;
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
	
	@Autowired
	ExamResultRepository examResultRep;
	
	public GraphDTO addExpectedKnowledge(GraphDTO graphDto) throws InvalidDataException, EntityAlreadyExistsException {
		if (graphDto.subject_id == null) throw new InvalidDataException("Subject id must be defined");
		Subject subject =  subRep.findById(graphDto.subject_id).orElse(null);
		if (subject == null) throw new InvalidDataException("Subject with given id not found");
		
		Graph g = subject.getDomain().getExpectedGraph();
		if (g == null) throw new InvalidDataException("Graph not defined for this exam");
		
		HashMap<Long, Node> nodesMap = new HashMap<Long, Node>();
		
		g.setRoot(graphDto.nodes.get(0).id);
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
		if (exam.getSubject().getDomain().getExpectedGraph().getEdges().size() == 0) throw new InvalidDataException("Expected graph not defined");
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
		String node1_id = edgeStr.split("\\.")[0];
		String node2_id = edgeStr.split("\\.")[1];
		
		Node node1 = nodes.get(Long.parseLong(node1_id));
		Node node2 = nodes.get(Long.parseLong(node2_id));
		
		if (!g.getNodes().contains(nodes.get(node1.getId()))) g.getNodes().add(nodes.get(node1.getId()));
		if (!g.getNodes().contains(nodes.get(node2.getId()))) g.getNodes().add(nodes.get(node2.getId()));
		
		g.addEdge(node1, node2);
	}
	
	public QuestionDTO getCurrentQuestion(String username, Long examId) throws InvalidDataException {
		if (examId == null) throw new InvalidDataException("Exam id not defined");
		Exam exam = examRep.findById(examId).orElse(null);
		Graph g = exam.getSubject().getDomain().getExpectedGraph();
		if (g.isValid()) throw new InvalidDataException("Expected knowledge not defined for this exam");
		
		User stud = userRep.findByUsername(username);
		ExamResult activeExam= stud.getActiveExam();
		Question q = g.getNextQuestion(exam, activeExam.getQuestionsAnswered())[1];
		
		return this.castToDTO(q);
	}
	
	public QuestionDTO getNextQuestion(String username, QuestionAnswerDTO questionAnswerDto) throws InvalidDataException, EntityDoesNotExistException {
		User stud = userRep.findByUsername(username);
		
		if (questionAnswerDto.examId == null) throw new InvalidDataException("Exam id not defined");
		Exam exam = examRep.findById(questionAnswerDto.examId).orElse(null);
		if (exam == null) throw new EntityDoesNotExistException("Exam with given id not found");
		
		Graph g = exam.getSubject().getDomain().getExpectedGraph();
		if (g.isValid()) throw new InvalidDataException("Expected knowledge not defined for this exam");
		ExamResult activeExam= stud.getActiveExam();
		
		
		if (activeExam == null) {
			activeExam = new ExamResult(exam, stud);
			activeExam.setQuestionsAnswered(0);
			Question q = g.getNextQuestion(exam, 0)[1];
			stud.setActiveExam(activeExam);
			if (q == null) throw new InvalidDataException("Something went wrong");
			
			userRep.save(stud);
			return this.castToDTO(q);
		}
		
		Integer questionsAnswered = activeExam.getQuestionsAnswered();
		activeExam.setQuestionsAnswered(questionsAnswered + 1);
		questionsAnswered++;
		
		
		if (!activeExam.isFinished()) {
			Question[] qs = g.getNextQuestion(exam, questionsAnswered);
			boolean correct = exam.isCorrectAnswer(questionAnswerDto.questionId, questionAnswerDto.choiceId);
			activeExam.addAnswer(qs[0], correct);
			
			userRep.save(stud);
			
			return this.castToDTO(qs[1]);
		}
		
		Question[] qs = g.getNextQuestion(exam, questionsAnswered);
		boolean correct = exam.isCorrectAnswer(questionAnswerDto.questionId, questionAnswerDto.choiceId);
		activeExam.addAnswer(qs[0], correct);
		userRep.save(stud);
		examResultRep.save(activeExam);
		stud.setActiveExam(null);
		Question q = new Question();
		q.setText("Exam finished");
		q.setId(-1L);
		return this.castToDTO(q);
	}
	
	private GraphDTO castToDTO(Graph g) {
		GraphDTO retVal = new GraphDTO();
		Long qId;
		for (Node node : g.getNodes()) {
			qId = node.getQuestion() == null ? -1L : node.getQuestion().getId();
			retVal.nodes.add(new NodeDTO(node.getId(),node.getQuestion().getText(), qId));
		}
		for (Edge edge : g.getEdges()) {
			retVal.edges.add(new EdgeDTO(edge.getSource().getId(),edge.getDestination().getId(),edge.getStatus()));
		}
		
		return retVal;
	}
	
	private QuestionDTO castToDTO(Question question) {
		QuestionDTO retVal = new QuestionDTO();
		retVal.text = question.getText();
		retVal.id = question.getId();
		
		retVal.choices = new ArrayList<ChoiceDTO>();
		for (Choice choice : question.getChoices()) {
			retVal.choices.add(new ChoiceDTO(choice.getId(),choice.getText(), null));
		}
		
		return retVal;
	}
	
}
