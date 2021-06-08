package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.Collegamento;
import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	YelpDao dao;
	SimpleDirectedWeightedGraph <Business, DefaultWeightedEdge> grafo;
	Map <String, Business> idMap;


	public Model(){
		dao = new YelpDao();
	}


	public void creaGrafo(String citta, int anno) {
		this.grafo = new SimpleDirectedWeightedGraph<>(
				DefaultWeightedEdge.class);
		idMap = new HashMap <String, Business> ();
		//creiamo la id map che usiamo poi
		//per aggiungere i valori al grafo
		for(Business b: dao.getNodi(citta, anno)) {
			idMap.put(b.getBusinessId(), b);
		}
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//il modo più veloce di gestire la creazione delle adiacenze era quella
		//di andare a prendere con un metodo DAO solo le medie delle recensioni
		// e poi fare la differenza quando le aggiungevi
		//infatti così riuscivi a scorrerti il database una volta sola prendendo le mdei di tutti
		//e poi le andavi ad aggiungere quandi creavi le adiacenze.
		//In questo caso invece devi scorrere il database una volta per ogni sua istanza
		//ora aggiungiamo i collegamenti
		
		for(Collegamento c: dao.getCollegamenti(citta, anno)) {
			if(this.grafo.containsVertex(idMap.get(c.getId1())) || 
					this.grafo.containsVertex(idMap.get(c.getId2()))){
				DefaultWeightedEdge b = this.grafo.getEdge(idMap.get(c.getId1()), idMap.get(c.getId2()));
				if(b == null) {

					if(idMap.containsKey(c.getId1())
							&& idMap.containsKey(c.getId2())){
						if(c.getPeso() < 0)
							Graphs.addEdge(grafo, idMap.get(c.getId1()), idMap.get(c.getId2()), c.getPeso()*(-1.0));
						if(c.getPeso() > 0)
							Graphs.addEdge(grafo, idMap.get(c.getId2()), idMap.get(c.getId1()), c.getPeso());
					}
				}
			}
		}
	}
	
	

	public Integer getNV() {
		if(grafo != null)
			return grafo.vertexSet().size();
		return null;
	}
	
	public Integer getNE() {
		if(grafo != null)
			return grafo.edgeSet().size();
		return null;
	}
	
	
	
	public Business findBest() {
		
		double punteggio = 0;

		double bestPunteggio = Integer.MIN_VALUE;
		Business best = null;
		for(Business b: grafo.vertexSet()) {
			
			for(DefaultWeightedEdge e : grafo.incomingEdgesOf(b)) {
				punteggio += grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(b)) {
				punteggio -= grafo.getEdgeWeight(e);
			}
			if(punteggio >= bestPunteggio) {
				bestPunteggio = punteggio;
				best = b;
			}
			System.out.println(punteggio);

			System.out.println(b.getBusinessName());
			System.out.println("FINITO");
			punteggio = 0;
		}
		return best;
	}
	
	//punto due non lo faccimo perché altri li hanno fatto e la ricorsione sembra abbastanza stupida
	
	
}