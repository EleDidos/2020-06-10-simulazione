package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private SimpleWeightedGraph <Actor, DefaultWeightedEdge> graph;
	private Map <Integer, Actor> idMap;
	
	public Model() {
		dao = new ImdbDAO ();
		
		idMap = new HashMap <Integer, Actor>();
	}

	
	public List <String> getGenres() {
		List <String> genres = dao.listAllGenres();
		return genres;
	}

	
	public void creaGrafo (String genere) {
		graph = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(graph, idMap.values());
	
	}
	

}
