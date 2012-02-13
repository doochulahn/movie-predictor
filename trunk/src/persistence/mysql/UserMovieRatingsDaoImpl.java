package persistence.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistence.DataSource;
import persistence.PersistenceException;
import persistence.UserMovieRatingsDao;


public class UserMovieRatingsDaoImpl implements UserMovieRatingsDao{
	private DataSource dataSource;

	public UserMovieRatingsDaoImpl(){
		this.dataSource=new DataSource();
	}

	// il nome va modificato in modo che si faccia riferimento a vettori basati sul genere
	@Override
	public List<Double> retrieveRatingsByMovieVectors(List<Integer[]> vectors) throws PersistenceException {
		List<Double> ratings=new ArrayList<Double>();
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement=null;
		String query="select rating from user_ratedmovies where "+createWhereConds(vectors);
		try{
			statement=connection.prepareStatement(query);
			ResultSet res=statement.executeQuery();
			while(res.next() ){
				ratings.add(res.getDouble("rating"));
			}
		}
		catch (SQLException e){
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
		return ratings;
	}

	private String createWhereConds(List<Integer[]> vectors){// non viene affrontato il problema della sicurezza<<
		String queryWhereConds="";
		queryWhereConds+="(movieid="+vectors.get(0)[0]+") ";
		for (int i=1;i<vectors.size(); i++){
			queryWhereConds+="or (movieid="+vectors.get(i)[0]+")";
		}
		return queryWhereConds;
	}








}