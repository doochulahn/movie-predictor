package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Dataset;
import model.Genre;
import parserAndWriter.GenreParser;
import parserAndWriter.UserRatedMoviesParser;
import persistence.PersistenceException;

public class DataLoader {
	
	public Map<Integer, Map<Integer,Double>> loadSeenMoviesByUser(){
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		Dataset.visit(new Dataset("dataset"+"/user_ratedmovies.dat",1),p);
		return p.fromList2map();
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
	
	public Map<Integer, Double> loadMovieID2rtRating() throws PersistenceException{
		DataLoaderHelper dlh=new DataLoaderHelper();
		return dlh.retrieveRatingsByMovieVectors();
	}

}
