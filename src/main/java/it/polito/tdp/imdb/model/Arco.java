package it.polito.tdp.imdb.model;

/**
 * Partecipazione di due attori agli stessi film
 * Il peso= n° di film insieme
 * @author elena
 *
 */

public class Arco {
	
	private Integer actorID1;
	private Integer actorID2;
	private Integer peso;
	public Arco(Integer actorID1, Integer actorID2,  Integer peso) {
		super();
		this.actorID1 = actorID1;
		this.actorID2 = actorID2;
		
		this.peso = peso;
	}
	public Integer getActorID1() {
		return actorID1;
	}
	public void setActorID1(Integer actorID1) {
		this.actorID1 = actorID1;
	}
	public Integer getActorID2() {
		return actorID2;
	}
	public void setActorID2(Integer actorID2) {
		this.actorID2 = actorID2;
	}
	
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	

}
