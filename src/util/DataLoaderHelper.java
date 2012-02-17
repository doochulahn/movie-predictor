package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.knn.DoubleFeature;
import model.knn.FeaturePosition;
import model.knn.Instance;
import model.knn.IntFeature;
import persistence.DataSource;
import persistence.PersistenceException;

public class DataLoaderHelper {
	private DataSource dataSource;

	public DataLoaderHelper(){
		this.dataSource=new DataSource();
	}


	public Map<Integer, Double> retrieveRatingsByMovieVectors() throws PersistenceException {
		Map<Integer, Double> movieID2rtRating=new HashMap<Integer, Double>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		String query="select id, idrtAllCriticsRating from movies";
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while( res.next() ){
				movieID2rtRating.put(res.getInt("id"), res.getDouble("idrtAllCriticsRating"));
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				e.printStackTrace();
				throw new PersistenceException(e.getMessage());
			}
		}
		return movieID2rtRating;
	}
	
	public Map<Integer, Map<Integer,Double>> retrieveSeenAndRatedMoviesByUser() throws PersistenceException{
		Map<Integer, Map<Integer,Double>> userId2MovieId2rating=new HashMap<Integer, Map<Integer,Double>>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		String query="select userid,movieid,rating from user_ratedmovies";
		try{
			
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while( res.next() ){
				int userID=res.getInt("userid");
				int movieID=res.getInt("movieid");
				double rating=res.getDouble("rating");
				
				if (userId2MovieId2rating.get(userID)!=null){
					Map<Integer,Double> movieID2ratingByUser=userId2MovieId2rating.get(userID);
					if (movieID2ratingByUser.get(movieID)!=null){
						System.out.println("Problema");// se è stato inserito il film allora anhce il rating
					} else{
						movieID2ratingByUser.put(movieID, rating );
					}
				} else{
					HashMap<Integer,Double> movieID2ratingByUser=new HashMap<Integer, Double>();
					movieID2ratingByUser.put(movieID, rating );
					userId2MovieId2rating.put(userID, movieID2ratingByUser);
				}
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				e.printStackTrace();
				throw new PersistenceException(e.getMessage());
			}
		}
		return userId2MovieId2rating;
	}
	
	public Integer[] retrieveGenreVectorByMovieID( int movieID) throws PersistenceException{
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		Integer[] vector=new Integer[21];
		String query="select * from genres where movieid=?";
		try{
			statement=connection.prepareStatement(query);
			statement.setInt(1, movieID);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				for (int i=0; i<vector.length;i++){
					vector[i]=res.getInt(i+1);
				}
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return vector;
	}
	
	public List<Integer[]> retrieveSimilarVectors(Integer[] vector)throws PersistenceException {
		List<Integer[]> vectors=new ArrayList<Integer[]>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		
		String query="select * from genres where "+getWhereConds(vector);
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				Integer[] v=new Integer[21];
				for (int j=0; j<21; j++){
					v[j]=res.getInt(j+1);
				}
				// qui inserisco il calcolo del rank di similarità
				vectors.add(v);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return vectors;
	}
	
	
	public List<Integer[]> retrieveAllMovieVectors() throws PersistenceException{
		List<Integer[]> vectors=new ArrayList<Integer[]>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		
		String query="select * from genres";
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				Integer[] v=new Integer[21];
				for (int j=0; j<21; j++){
					v[j]=res.getInt(j+1);
				}
				vectors.add(v);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return vectors;
	}
	
	private static String getWhereConds(Integer[] vector){
		String[] int2genre=new String[21];
		int2genre[0]="movieid";
		int2genre[1]="action";
		int2genre[2]="adventure";
		int2genre[3]="animation";
		int2genre[4]="children";
		int2genre[5]="comedy";
		int2genre[6]="documentary";
		int2genre[7]="drama";
		int2genre[8]="fantasy";
		int2genre[9]="filmnoir";
		int2genre[10]="horror";
		int2genre[11]="imax";
		int2genre[12]="musical";
		int2genre[13]="mystery";
		int2genre[14]="romance";
		int2genre[15]="scifi";
		int2genre[16]="short";
		int2genre[17]="thriller";
		int2genre[18]="war";
		int2genre[19]="western";
		int2genre[20]="crime";
		
		String queryWhereConds="";
		queryWhereConds+=int2genre[1]+"=1 ";
		for (int i=2;i<vector.length; i++){
			if (vector[i]==1){
				queryWhereConds+="or "+int2genre[i]+"=1 ";
			}
		}
		return queryWhereConds;
	}
	
	public Map<Integer,Integer[]> retrieveMovieID2genreVector()throws PersistenceException {
		Map<Integer,Integer[]> movieID2genreVector=new HashMap<Integer, Integer[]>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		
		String query="select * from genres";
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				Integer[] v=new Integer[21];
				for (int j=0; j<21; j++){
					v[j]=res.getInt(j+1);
				}
				movieID2genreVector.put(v[0], v);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}
		return movieID2genreVector;
	}
	
	public Map<Integer,Instance> retrieveId2instance() throws PersistenceException {
		Map<Integer,Instance> id2instance=new HashMap<Integer,Instance>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		String query="select id, idrtAllCriticsRating,numberOfTop100Actors,isTop250movie from movies";
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while( res.next() ){
				Instance instance=new Instance();
				instance.addFeature(FeaturePosition.ID, new IntFeature(res.getInt("id")));
				instance.addFeature(FeaturePosition.RTID, new DoubleFeature(res.getDouble("idrtAllCriticsRating")));
				instance.addFeature(FeaturePosition.TOP100ACTORSNUMBER, new IntFeature(res.getInt("numberOfTop100Actors")));
				instance.addFeature(FeaturePosition.TOP250MOVIE, new IntFeature(res.getInt("isTop250movie")));
				id2instance.put((Integer) instance.get(FeaturePosition.ID).getValue(),instance);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new PersistenceException(e.getMessage());
		} finally{
			try{
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e){
				e.printStackTrace();
				throw new PersistenceException(e.getMessage());
			}
		}
		return id2instance;
	}
	
	



}
