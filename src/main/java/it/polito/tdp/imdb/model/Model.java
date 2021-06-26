package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private SimpleWeightedGraph< Actor , DefaultWeightedEdge>graph;
	private Map <Integer, Actor  > idMap;
	private ImdbDAO dao;
	private Simulatore sim;
	
	public Model() {
		idMap= new HashMap <Integer,Actor  >();
		dao=new ImdbDAO();
	}
	
	public void creaGrafo(String genere) {
		graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.loadAllVertici(idMap, genere);
		Graphs.addAllVertices(graph, idMap.values());
		
		for(Arco a : dao.listArchi(idMap,genere))
			Graphs.addEdge(graph,a.getA1(),a.getA2(),a.getPeso());
		
		
	}
	
	public Integer getNVertici() {
		return graph.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return graph.edgeSet().size();
	}
	
	public List <Actor> getVertici(){
		List <Actor> vertici = new ArrayList <Actor>();
		for(Actor a     : graph.vertexSet())
			vertici.add(a  );
		Collections.sort(vertici);
		return vertici;
	}

	public SimpleWeightedGraph< Actor , DefaultWeightedEdge> getGraph() {
		return graph;
	}

	
	public List <String> getGeneri() {
		
		return dao.listAllGenres();
	}

	
	public List<Actor> getVicini(Actor scelto) {
		if(!graph.vertexSet().contains(scelto))
			throw new RuntimeException("L'attore scelto non fa parte del grafo");
		List <Actor> vicini = new ArrayList <Actor>();
		
		//BreadthFirstIterator
		GraphIterator<Actor,DefaultWeightedEdge> bfi = new BreadthFirstIterator <Actor,DefaultWeightedEdge> (graph,scelto);
		while(bfi.hasNext()) 
				vicini.add(bfi.next());
		
		return vicini;
	}
	
	public void simula(Integer n) {
		sim= new Simulatore (graph,n);
		sim.run();
		
	}
	
	public List<Actor> getIntervistati() {
		return sim.getIntervistati();
	}


	public int getPause() {
		return sim.getPause();
	}
	
	

}
