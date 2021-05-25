package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private SimpleWeightedGraph <Actor, DefaultWeightedEdge> graph;
	private Map <Integer, Actor> idMap;
	private Simulatore sim;
	
	public Model() {
		dao = new ImdbDAO ();
		
		idMap = new HashMap <Integer, Actor>();
		
		sim = new Simulatore();
	}
	

	
	public SimpleWeightedGraph<Actor, DefaultWeightedEdge> getGraph() {
		return graph;
	}




	public List <String> getGenres() {
		List <String> genres = dao.listAllGenres();
		return genres;
	}

	
	public void creaGrafo (String genere) {
		graph = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		dao.getVertex(genere, idMap); //riempio IDMAP
		Graphs.addAllVertices(graph, idMap.values());
		
		System.out.println("VERTICI ATTORI\n");
		for(Actor a: graph.vertexSet())
			System.out.println(a.toString()+"\n");
		
		System.out.println("ARCHI \n");
		for(Arco ai: dao.getArchi(genere, idMap)) {
			Graphs.addEdge(graph,ai.getActor1(), ai.getActor2(), ai.getPeso());
			System.out.println(ai+"\n");
		}
	}
	
	public int nVertici() {
		return this.graph.vertexSet().size();
	}
	
	public int nArchi() {
		return this.graph.edgeSet().size();
	}
	
	public List<String> getNomiAttoriVerticiString (){
		List <String> attoriNomi = new ArrayList <String> ();
		for(Actor a: graph.vertexSet())
			attoriNomi.add(a.getFirstName()+" "+a.getLastName());
		return attoriNomi;
	}



	public String getAttoriRaggiungibili(String NC) {
		String attoriRaggiungibili="";
		List <Actor> reachable = new ArrayList <Actor>();
		String e[] = NC.split(" ");
		Actor partenza=null;
		
		for(Actor a: idMap.values())
			if( a.getFirstName().equals(e[0]) && a.getLastName().equals(e[1]) )
				partenza = a;
		
		GraphIterator <Actor,DefaultWeightedEdge> dfv = new DepthFirstIterator <Actor,DefaultWeightedEdge> (graph,partenza);
		while(dfv.hasNext())
			reachable.add(dfv.next());
		
		for(Actor a: reachable)
			attoriRaggiungibili+=a.getFirstName()+" "+a.getLastName()+"\n";
		
		return attoriRaggiungibili;
	}
	
	
	public void simula (Integer n) {
		if(graph != null) {
			sim.init(graph,n);
			sim.run();
		}
	}
	
	public String getDatiSimulazione() {
		String result ="";
		result="L'intervistatore si è preso "+sim.getGGPausa()+" giorni di pausa.\n"
				+ "Gli attori intervistati sono:\n"+sim.getIntervistati();
				
		return result;
	}
	

}
