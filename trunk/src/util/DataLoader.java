package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Dataset;
import model.Genre;
import model.knn.Instance;

import parserAndWriter.GenreParser;
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
	
	public HashMap<Integer, List<String>> loadMovieID2genres(){
        HashMap<Integer, List<String>> movieID2genres=new HashMap<Integer, List<String>>();
        GenreParser p=new GenreParser();
        Dataset.visit(new Dataset("dataset/movie_genres.dat",1),p);
        for (Genre g: p.getResults()){
                if (movieID2genres.containsKey(g.getMovieID())){
                        movieID2genres.get(g.getMovieID()).add(g.getGenre());
                }else{
                        List<String> l=new ArrayList<String>();
                        l.add(g.getGenre());
                        movieID2genres.put(g.getMovieID(), l);
                }
        }
        return movieID2genres;
}
	
	

}
