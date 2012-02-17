package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.knn.Instance;

import persistence.PersistenceException;

public class DataLoader {
	
	public Map<Integer, Map<Integer,Double>> loadSeenAndRatedMoviesByUser() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		return dlh.retrieveSeenAndRatedMoviesByUser();
	}
	
	public Map<Integer, Integer[]> loadMovieID2genresVector() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		return dlh.retrieveMovieID2genreVector();
	}
	
	public Map<Integer, Double> loadMovieID2rtRating() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		return dlh.retrieveRatingsByMovieVectors();
	}
	
	public Map<Integer,List<Integer[]>> loadMovieID2genreBasedSimilarVector() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		Map<Integer,List<Integer[]>> movieID2genreBasedSimilarVector=new HashMap<Integer, List<Integer[]>>();
		List<Integer[]> allMovieGenreVectors=dlh.retrieveAllMovieVectors();
		for (Integer[] i: allMovieGenreVectors){
			movieID2genreBasedSimilarVector.put(i[0], dlh.retrieveSimilarVectors(i));
		}
		return movieID2genreBasedSimilarVector;
	}
	
	public Map<Integer,Instance> loadAllInstances() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		return dlh.retrieveId2instance();
	}
	
	

}
