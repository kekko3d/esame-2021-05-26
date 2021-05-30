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


	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(
				DefaultWeightedEdge.class);
		idMap = new HashMap <String, Business> ();
		//creiamo la id map che usiamo poi
		//per aggiungere i valori al grafo
		for(Business b: dao.getNodi("Phoenix", 2013)) {
			idMap.put(b.getBusinessId(), b);
		}
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//ora aggiungiamo i collegamenti
		for(Collegamento c: dao.getCollegamenti("Phoenix", 2013)) {
			if(idMap.containsKey(c.getId1()) && idMap.containsKey(c.getId2())){
				Graphs.addEdge(grafo, idMap.get(c.getId1()), idMap.get(c.getId2()), c.getPeso());
			}
		}
		
		
	}

	public Integer getNV() {
		if(grafo != null)
			return grafo.vertexSet().size();
		return null;
	}
	
	public Integer getNE() {
		return grafo.edgeSet().size();
	}
	
}