package it.polito.tdp.yelp.model;

public class testModel {

	public static void main(String[] args) {
		
		Model md = new Model();
		md.creaGrafo();
		System.out.println(md.getNV());
		System.out.println(md.getNE());

		
	}

}
