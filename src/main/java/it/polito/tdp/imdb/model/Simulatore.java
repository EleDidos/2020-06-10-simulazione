package it.polito.tdp.imdb.model;

import java.util.ArrayList;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {
	
	
	private int n;
	private int gg; //gg consecutivi di interviste
	private int ggPausa; //gg pausa
	private ArrayList <Actor> actors = new ArrayList <Actor>();
	private ArrayList <Actor> intervistati = new ArrayList <Actor>();
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> graph;
	
	public void init(SimpleWeightedGraph<Actor, DefaultWeightedEdge> graph, int n) {
		this.n=n;
		gg=0;
		ggPausa=0;
		this.graph=graph;
		for(Actor a: graph.vertexSet())
			actors.add(a);
	}
	
	Actor scelto;
	double probability;
	int random;
	
	public void run() {
		//primo personaggio scelto casualmente
		random = (int)Math.random()*actors.size();
		Actor primo = actors.get(random);
		intervistati.add(primo);
		actors.remove(primo);
		
		//dal 2° gg in poi
		while(gg<n) {
			probability = Math.random();
			
			//60% casuale
			if(probability<=0.6) {
				this.casuale();
			}//0.6 
			else { //0.4
				scelto = this.actorGradoMax(intervistati.get (intervistati.size()-1) );
				
				if(scelto==null) { //grado dell'ultimo=0 --> casuale
					this.casuale();
				}else {
					intervistati.add(scelto);
					actors.remove(scelto);
					gg++;
				}
			}//0.4
			
			//2 persone stesso sesso negli ultimi due gg
			if(intervistati.get(gg-1).getGender().equals(intervistati.get(gg-2).getGender())) {
				probability= Math.random();
				//pausa
				if(probability<=0.9) {
					gg++; //per un gg non intervista
					ggPausa++;
					//gg dopo sceglie per forza casualmente
					//poi riprende normale trafila di scelta
					this.casuale();
				} 
				else { //casuale
					this.casuale();
				}
					
			}//genere uguale
				
			
		}//while
		
	}//run
	
	
	public void casuale () {
		random = (int)Math.random()*actors.size();
		scelto = actors.get(random);
		intervistati.add(scelto);
		actors.remove(scelto);
		gg++;
	}
	
	
	public Actor actorGradoMax(Actor ultimo) {
		Double max=0.0; //film insieme
		Actor best=null;
		for(DefaultWeightedEdge e: graph.edgesOf(ultimo)) {
			if(graph.getEdgeWeight(e)>max) {
				max=graph.getEdgeWeight(e);
				best=graph.getEdgeTarget(e);
			}
		}
		if(best==null) //grado di ultimo=0
			return null;
		return best;
	}
	
	
	public int getGGPausa() {
		return ggPausa;
	}
	
	public String getIntervistati() {
		String intervistatiString ="";
		for(Actor a: intervistati)
			intervistatiString+=a.getFirstName()+" "+a.getLastName()+"\n";
		return intervistatiString;
	}

}
