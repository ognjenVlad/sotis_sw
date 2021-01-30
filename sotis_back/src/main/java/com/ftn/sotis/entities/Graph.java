package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.ftn.sotis.enums.EdgeStatusEnum;

@Entity
public class Graph {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany
	private List<Node> nodes;
	
	@OneToOne
	private Node root;
	
	@ManyToMany
	private List<Edge> edges;
	
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
		if (this.nodes.contains(node)) return false;
		
		this.nodes.add(node);
		return true;
	}
	
	private void sortNodesBfsOrder() {
		this.bfsNodesSorted.add(this.root);
		this.getBfsGeneration(root);
		
		Iterator<Node> iter = this.bfsNodesSorted.iterator();
		iter.next();
		while (iter.hasNext()) {
			getBfsGeneration(iter.next());
		}
	}
	
	private void getBfsGeneration(Node node) {
		// TODO :  Implement actual BFS method
		
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
	
	public ArrayList<Node> getNextBfs(Long current) {
		this.sortNodesBfsOrder();
		return this.bfsNodesSorted;
	}
	
	/**
	 * Checks if graph is directed and not cyclic.
	 * 
	 * @return
	 */
	public boolean isValid() {
		Set<Edge> visited = new HashSet<Edge>();
		ArrayList<Edge> toVisit = new ArrayList<Edge>();
		Edge temp;
		
		for (Edge e : edges) {
			if (e.getSource().equals(this.root)) {
				toVisit.add(e);
			}
		}
		
		for(int i = 0; i < toVisit.size(); i++) {
			temp = toVisit.get(i);
			
			if (visited.contains(temp)) {
				for (Edge edge : visited) {
					System.out.println(edge.getSource().getId() + " -- " + edge.getDestination().getId());
				}
				System.out.println("Temp:" + temp.getSource().getId() + " -- " + temp.getDestination().getId());
				return false;
			}
			else {
				visited.add(temp);
			}
			for (Edge e : edges) {
				if (e.getSource().equals(temp.getDestination())) {
					toVisit.add(e);
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * Return new graph with edges from both this and other graph.
	 * Each edge has status updated.
	 * 
	 * @param other
	 * @return
	 */
	public Graph getGraphDifference(Graph other) {
		Graph retVal = new Graph();
		retVal.setNodes((ArrayList<Node>)this.nodes);
		
		for (Edge e : other.getEdges()) {
			if (this.edges.contains(e)) { 
				e.setStatus(EdgeStatusEnum.SAME_EDGE);
			}
			else {
				e.setStatus(EdgeStatusEnum.NEW_EDGE);
			}
		}
		
		retVal.setEdges((ArrayList<Edge>)other.getEdges());
		
		for (Edge e : this.getEdges()) {
			if (!other.edges.contains(e)) {
				e.setStatus(EdgeStatusEnum.REMOVED_EDGE);
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

	public void setNodes(ArrayList<Node> nodes) {
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
