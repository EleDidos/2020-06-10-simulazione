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
	
	
	public List<String> listAllGenres(){
		String sql = "SELECT DISTINCT genre FROM movies_genres";
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
	
	
	public void loadAllVertici(Map <Integer, Actor> idMap, String genere){
		
		
		String sql = "SELECT DISTINCT a.id, a.first_name, a.last_name, a.gender "
				+ "FROM actors as a, roles as r, movies_genres as mg "
				+ "WHERE a.id=r.actor_id and r.movie_id=mg.movie_id and mg.genre=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, genere);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("a.id"))) {
					
					idMap.put(res.getInt("a.id"),new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender")));
					
				}//if
				
			}//while
			
			conn.close();
			return  ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}


public List <Arco> listArchi(Map <Integer,  Actor > idMap, String genere){
	String sql = "SELECT DISTINCT a1.id as id1, a2.id as id2, COUNT(DISTINCT mg1.movie_id) as peso "
			+ "FROM actors as a1, roles as r1, movies_genres as mg1, actors as a2, roles as r2, movies_genres as mg2 "
			+ "WHERE a1.id=r1.actor_id and r1.movie_id=mg1.movie_id and mg1.genre=? and a2.id=r2.actor_id and r2.movie_id=mg2.movie_id and mg1.genre=mg2.genre and a1.id>a2.id and mg1.movie_id=mg2.movie_id "
			+ "GROUP BY a1.id, a2.id";

	Connection conn = DBConnect.getConnection();
	List <Arco> archi = new ArrayList <Arco>();

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, genere);
		ResultSet res = st.executeQuery();
		while (res.next()) {

			Actor a1 = idMap.get(res.getInt("id1"));
			Actor a2 = idMap.get(res.getInt("id2"));
			
			if(a1!=null & a2!=null & res.getInt("peso")>0) {
				Arco a = new Arco(a1,a2,res.getInt("peso"));
				archi.add(a);
				
			}
		

		}
		conn.close();
		return archi;
		
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
}

	
}
