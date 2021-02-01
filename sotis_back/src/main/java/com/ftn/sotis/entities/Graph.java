package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.ftn.sotis.enums.EdgeStatusEnum;

@Entity
public class Graph {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Node> nodes;
	
	@OneToOne
	private Node root;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Edge> edges;
	
	@Transient
	private ArrayList<Node> bfsNodesSorted = new ArrayList<Node>();

	public Graph() {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}
	
	public Graph(Node root, List<Node> nodes, List<Edge> edges) {
		this.root = root;
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public boolean addEdge(Node source, Node destination) {
		if (source == null) {
			System.out.println("Err1");
			return false;
		}
		if (destination == null) {
			System.out.println("Err2");
			return false;
		}
		if (source.equals(destination)) {
			System.out.println("Err3");
			return false;
		}
		
		if (!this.nodes.contains(source)) {
			System.out.println("Err4");
			return false;
		}
		if (!this.nodes.contains(destination)) {
			System.out.println("Err5");
			return false;
		}
		
		boolean sameVertex;
		for (Edge edge : edges) {
			sameVertex = edge.equals(new Edge(source,destination));
			// edge.contains(source) && edge.contains(destination);
			if (sameVertex) {
				System.out.println("Err6");
				return false;
			}
		}
		
		this.edges.add(new Edge(source,destination));
		return true;
	}
		
	public boolean addNode(Node node) {
		if (node ==null) return false;
		if (root == null) {
			root = node;
			this.nodes.add(node);
		}
		if (this.nodes.contains(node)) { 
			return false;
		}
		
		this.nodes.add(node);
		return true;
	}
	
	public Question[] getNextQuestion(Exam exam, Integer questionsAnswered) {
		this.sortNodesBfsOrder();
		
		Question[] retVal = new Question[2];
		
		retVal[0] = null;
		retVal[1] = null;
		
		int questionsCounter = 0;
		for (Node n : this.bfsNodesSorted) {
			if (exam.getQuestions().contains(n.getQuestion())) {
				if (questionsAnswered == 0) {
					retVal[1] = n.getQuestion();
					return retVal;
				}
				if (questionsCounter == questionsAnswered) {
					retVal[1] = n.getQuestion();
					return retVal;
				}else if (questionsCounter < questionsAnswered){
					retVal[0] = n.getQuestion();
					questionsCounter++;
				}
			}
		}

		return retVal;
	}
	
	private void sortNodesBfsOrder() {
		this.bfsNodesSorted.add(this.root);
		this.getBfsGeneration(root);
		
		for (int i = 1; i < this.bfsNodesSorted.size(); i++) {
			getBfsGeneration(this.bfsNodesSorted.get(i));
		}
	}
	
	
	private void getBfsGeneration(Node node) {
		ArrayList<Edge> currentLevelVertices = new ArrayList<Edge>();
		ArrayList<Node> currentLevelNodes = new ArrayList<Node>();
		
		for (Edge e : this.edges) {
			if (e.getSource().equals(node)) currentLevelVertices.add(e);
		}
		
		for (Edge e : currentLevelVertices) {
			currentLevelNodes.add(e.getDestination());
		}

		currentLevelNodes.sort(new Comparator<Node>() {
			@Override
			public int compare(Node arg0, Node arg1) {
				return (int) (arg0.getId() - arg1.getId());
			}
		});

		for (Node n : currentLevelNodes) {
			if (!this.bfsNodesSorted.contains(n)) {
				this.bfsNodesSorted.add(n);
			}
		}
		
	}
	
	/**
	 * Checks if graph is directed and not cyclic.
	 * 
	 * @return
	 */
	public boolean isValid() {
		ArrayList<Node> visited = new ArrayList<Node>();
		visited.add(this.root);
		this.sortNodesBfsOrder();
		boolean isOneway = this.bfsNodesSorted.size() == this.nodes.size();

		return !this.isCyclic(root, visited) && isConnected() && isOneway;
	}
	
	private boolean isConnected() {
		Boolean found;
		for (Node n : this.nodes) {
			found = false;
			for (Edge e : this.edges) {
				if (e.contains(n)) found = true;
			}
			if (!found) return false;
		}
		
		return true;
	}
	
	private boolean isCyclic(Node node, ArrayList<Node> visitedNodes) { 
		
		ArrayList<Edge> toVisit = new ArrayList<Edge>();
		
		for (Edge e : this.edges) {
			if (e.getSource().equals(node)) {
				toVisit.add(e);
			}
		}
		
		for (Edge e : toVisit) {
			if (visitedNodes.contains(e.getDestination())) {
				return true;
			}
			visitedNodes.add(e.getDestination());
			if (this.isCyclic(e.getDestination(), visitedNodes)) return true;
			visitedNodes.remove(e.getDestination());
		}
		
		return false;
	} 
	
	public void setRoot(Long id) {
		for (Node n : this.nodes) {
			if (n.getId().equals(id)) {
				this.root = n;
				return;
			}
		}
	}
	
	/**
	 * Return new graph with edges from both this and other graph.
	 * Each edge has status updated.
	 * 
	 * @param other
	 * @return
	 */
	public Graph getSubgraphDifference(Graph other) {
		Graph retVal = new Graph();
		ArrayList<Edge> sameNodeEdges = new ArrayList<Edge>();
		
		for (int i = 0; i < other.nodes.size()-1; i++) {
			for (int j = i+1; j < other.nodes.size(); j++) {
				for (Edge e : this.edges) {
					if (e.isBetweenBiderction(other.nodes.get(i), other.nodes.get(i)) && !sameNodeEdges.contains(e)) {
						sameNodeEdges.add(e);
					}
				}
			}
		}
		
		retVal.setNodes(this.nodes);
		
		for (Edge e : other.getEdges()) {
			if (sameNodeEdges.contains(e)) { 
				e.setStatus(EdgeStatusEnum.SAME_EDGE);
			}
			else {
				e.setStatus(EdgeStatusEnum.NEW_EDGE);
			}
		}
		
		retVal.setEdges((ArrayList<Edge>)other.getEdges());
		
		for (Edge e : sameNodeEdges) {
			if (!other.edges.contains(e)) {
				e.setStatus(EdgeStatusEnum.REMOVED_EDGE);
				retVal.getEdges().add(e);
			}
		}
		
		for (Edge e : this.edges) {
			if (!retVal.getEdges().contains(e)) {
				e.setStatus(EdgeStatusEnum.NONE);
				retVal.getEdges().add(e);
			}
		}
		
		return retVal;
	}
	
	
	/**
	 * Sets new values and returns new graph with values to be deleted.
	 * 
	 * @param root - new root value
	 * @param nodes - new nodes
	 * @param edges - new edges
	 * @return - graph with nodes and edges but not root node
	 */
	public Graph setNewData(Node root,ArrayList<Node> nodes, ArrayList<Edge> edges) {
		Graph retVal = new Graph(null, this.nodes, this.edges);
		this.root = root;
		this.nodes = nodes;
		this.edges = edges;
		return retVal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
}
