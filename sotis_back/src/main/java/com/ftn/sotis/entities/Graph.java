package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.springframework.util.CollectionUtils;

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
		if (source == null) return false;
		if (destination == null) return false;
		if (source.equals(destination)) return false;
		
		if (!CollectionUtils.contains(this.nodes.iterator(), source)) return false;
		if (!CollectionUtils.contains(this.nodes.iterator(), destination)) return false;
		
		boolean sameVertex;
		for (Edge edge : edges) {
			sameVertex = edge.contains(source) && edge.contains(destination);
			if (sameVertex) return false;
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
		if (CollectionUtils.contains(this.nodes.iterator(), node)) return false;
		
		this.nodes.add(node);
		return true;
	}
	
	public Node getNextBfs(Long current) {
		// TODO :  Implement actual BFS method
		return null;
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
