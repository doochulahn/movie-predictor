package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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



}
