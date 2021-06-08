package it.polito.tdp.yelp.model;

public class testModel {

	public static void main(String[] args) {
		
		Model md = new Model();
		md.creaGrafo("Tempe", 2013);
		System.out.println(md.getNV());
		System.out.println(md.getNE());
		System.out.println(md.findBest().getBusinessName());

	}

}
