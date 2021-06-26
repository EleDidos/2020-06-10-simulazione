package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {
	
private SimpleWeightedGraph<Actor, DefaultWeightedEdge>graph;
	
	private List <Actor>intervistati;
	private List <Actor>daIntervistare;
	private int pause=0;
	private int n;//giorni di interviste
	private int gg=0; //gg di interviste già fatti
	
	
	public Simulatore( SimpleWeightedGraph<Actor, DefaultWeightedEdge>graph, Integer n) {
		this.n=n;
		this.graph=graph;
		
		intervistati = new ArrayList<Actor>();
		daIntervistare = new ArrayList<Actor>();
		for(Actor a: graph.vertexSet())
			daIntervistare.add(a);
		
		Actor a = this.scegliCasualmente();
		intervistati.add(a);
		gg++;
		
	}
	
	public void run() {
		//se gg==5 vuol dire che ha già intervistato per 5 gg
		//se gg==4 --> deve iniziare 5° gg
		while(gg<n) {
			double probability= Math.random();
			
			if(probability<=0.6) {
				Actor a = this.scegliCasualmente();
				intervistati.add(a);
				gg++;
			} else {
				Actor a = this.consiglio();
				intervistati.add(a);
				gg++;
			} //40%
			
			if(gg<n) {
				Actor ultimo=intervistati.get(intervistati.size()-1);
				Actor penultimo=intervistati.get(intervistati.size()-2);
				if(ultimo.getGender().equals(penultimo.getGender())) {
					double p=Math.random();
					if(p<=0.9) {//pausa
						pause++;
						gg++; //salto un giorno
						//gg dopo riparto casualmente
						if(gg<n) {
							Actor a = this.scegliCasualmente();
							intervistati.add(a);
							gg++;
						}
					}
				}
			}
				
		}//while
	}
		
	
	private Actor scegliCasualmente() {
		int prob = (int) (Math.random()*(daIntervistare.size()));
		Actor scelto = daIntervistare.remove(prob);
		return scelto;
		
	}
	
	/**
	 * attore sotto consiglio dell'ultimo intervistato
	 * @param e
	 */
	public Actor  consiglio() {
		Actor ultimo = intervistati.get(intervistati.size()-1);
		//se non ha vicini --> casuale
		if(Graphs.neighborListOf(graph, ultimo)==null)
			return this.scegliCasualmente();
		//se ha vicini
		Actor intervistato = this.getVicinoGradoMAX(ultimo);
		//lo tolgo da "daIntervistare"
		for(int i=0;i<daIntervistare.size();i++){
			if(daIntervistare.get(i).equals(intervistato))
				daIntervistare.remove(i);
		}
		
		return intervistato;
		
	}
	
	/**
	 * potrebbero esserci più vicini di grado max
	 * in quel caso scelgo a caso tra loro
	 * @param ultimo
	 * @return
	 */
	private Actor getVicinoGradoMAX(Actor ultimo) {
		double pesoMax=0.0;
		List <Actor> max = new ArrayList <Actor>();
		//trovo grado max
		for(DefaultWeightedEdge e: graph.incomingEdgesOf(ultimo))
			if(graph.getEdgeWeight(e)>pesoMax)
				pesoMax=graph.getEdgeWeight(e);
		//trovo tutti gli attori con quel peso
		System.out.println("\n\nIncoming = "+graph.incomingEdgesOf(ultimo).size()+"\nOutgoing = "+graph.outgoingEdgesOf(ultimo).size());
		for(DefaultWeightedEdge e: graph.incomingEdgesOf(ultimo))
			if(graph.getEdgeWeight(e)==pesoMax) {
				Actor vicino = Graphs.getOppositeVertex(graph, e, ultimo);
				max.add(vicino);
			}
		System.out.println("\nN° di vicini max "+max.size());
		//ne scelgo uno a caso tra i max
		int p = (int) (Math.random()*(max.size()));
		System.out.println("\n p= "+p);
		return max.get(p);
				
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	

	public int getPause() {
		return pause;
	}

	
	
	
	
	
	

}
