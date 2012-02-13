package persistence;

import java.util.List;




public interface UserMovieRatingsDao{

	

	public List<Double> retrieveRatingsByMovieVectors(List<Integer[]> vectors) throws PersistenceException;
	
	
	


}