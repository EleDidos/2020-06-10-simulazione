package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<String> listAllGenres(){
		String sql = "SELECT * FROM movies_genres";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("genre"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Solo ATTORI che hanno partecipato
	 * ad almeno un film del genere passato
	 * Li metto in IDMAP
	 * @return
	 */
	public List<Actor> getVertex(String genere, Map <Integer, Actor> idMap){
		String sql = "SELECT DISTINCT * "
				+ "FROM actors as a, roles as r, movies_genres as m "
				+ "WHERE a.id=r.actor_id AND r.movie_id=m.movie_id AND m.genre=?";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(res.getInt("id"), actor);
				result.add(actor);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Partecipazioni congiunte di due attori a stessi film 
	 * del genere passato come parametro
	 * Peso = n° di film insieme
	 */
	public List <Arco> getArchi (String genere, Map <Integer, Actor> idMap){
		String sql = "SELECT r1.actor_id as id1, r2.actor_id as id2, COUNT(DISTINCT r1.movie_id) as weight "
				+ "	 FROM roles r1, roles r2, movies_genres mg "
				+ "	WHERE r1.actor_id > r2.actor_id AND "
				+ "	r1.movie_id = r2.movie_id AND "
				+ "	mg.movie_id = r1.movie_id AND "
				+ "	mg.genre = ? "
				+ "	GROUP BY r1.actor_id, r2.actor_id";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer i1 = res.getInt("id1");
				Integer i2 = res.getInt("id2");
				Actor a1 = idMap.get(i1);
				Actor a2 = idMap.get(i2);
				if(a1==null || a2==null) {
					throw new RuntimeException("Problema con getArchi");
				}
				Arco arco = new Arco (a1,a2,res.getInt("weight"));
				result.add(arco);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}
