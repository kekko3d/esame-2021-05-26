package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Business> getNodi(String citta, int year){
		String sql = "SELECT DISTINCT(b.`business_id`), b.`full_address`, b.`active`, b.`categories`, b.`city`, b.`review_count`,   b.`business_name`, b.`neighborhoods`, b.`latitude`, b.`longitude`, b.`state`, b.`stars` "
				+ "FROM Reviews r, Business b "
				+ "WHERE b.`city` = ? "
				+ "AND YEAR(r.`review_date`) = YEAR(?) "
				+ "AND b.`business_id` = r.`business_id` ";
		
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			LocalDate d =  LocalDate.of(year,1,2);
			
			st.setString(1, citta);
			st.setDate(2, Date.valueOf(d));
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Collegamento> getCollegamenti(String citta ,int year ){
		
		String sql = "SELECT r1.`business_id` as id1, r2.`business_id` as id2, (AVG(r1.`stars`) - AVG(r2.`stars`)) as peso "
				+ "FROM Reviews r1, Reviews r2, Business b1, Business b2 "
				+ "WHERE b1.`business_id` = r1.`business_id` AND b2.`business_id` = r2.`business_id` "
				+ "AND r1.`business_id` > r2.`business_id`"
				+ "AND b1.`city` = ? AND b2.`city` = b1.`city` "
				+ "AND YEAR(r2.`review_date`) = YEAR(?) "
				+ "AND YEAR(r1.`review_date`) = YEAR(r2.`review_date`) "
				+ "GROUP BY id1, id2 "
				+ "HAVING peso != 0 ";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			LocalDate d =  LocalDate.of(year,1,2);
			
			st.setString(1, citta);
			st.setDate(2, Date.valueOf(d));

			ResultSet res = st.executeQuery();
			while (res.next()) {

				Collegamento edge = new Collegamento(res.getString("business_id"), 
						res.getString("business_id"),
						res.getDouble("peso"));
				result.add(edge);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
