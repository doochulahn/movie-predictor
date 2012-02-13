package predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import model.Dataset;
import model.Genre;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.GenreParser;
import parserAndWriter.UserRatedMoviesParser;

public class GenreBasedPredictor implements Predictor{

	/***
	 * This method, for each target movie (about which make a prediction, and getted from input dataset), 
	 * return the prediction calculated by mean average of ratings of same user about similar movies.
	 * The similarity among movies is based on genre; 
	 * so if a generic movie have genres that matching the genres of target movie, these are similar.
	 */
	@Override
	public List<Prediction> calculatePrediction(Dataset inputSet) {
		List<Prediction> predictions=new ArrayList<Prediction>();
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		Dataset.visit(inputSet, p);// ancihè andare nel dataset posso prende da db
		HashMap<Integer,List<String>> movieID2genres=this.getMovieID2genres();
		HashMap<Integer, HashMap<Integer,Double>> userId2MovieId2rating=p.fromList2map();
		for (UserRatedMovie targetMovie: p.getResults()){
			int targetID=targetMovie.getMovieID();
			HashMap<Integer, List<Integer>> ranking2similarMovieIDs=this.getRankedSimilarMoviesByGenre(targetID, movieID2genres);
			
			int max=Collections.max(ranking2similarMovieIDs.keySet());
			
			List<Integer> similarMovieIDs=ranking2similarMovieIDs.get(max);
			
			List<Double> ratings=this.getRatingsOfUser(userId2MovieId2rating, similarMovieIDs, targetMovie.getUserID());
			Prediction pred;
			if (ratings.size()==0){
				// e qui c'è un caso che dovrebbe essere analizzato più approfonditamente
				pred=new Prediction(targetMovie.getUserID(), targetID, 2.5);
			}else{
				pred=new Prediction(targetMovie.getUserID(), targetID, calculateMeanAverage(ratings));
			}
			predictions.add(pred);
			System.out.println("Aggiunta predizione: "+pred.toString());
		}
		return predictions;
		
	}
	
	/***
	 * This method return a list of user rating for a set of movie
	 * @param userId2MovieId2rating - map that contains movies and relative user rating
	 * @param movieIDs - the list that contains movie ids which knowing ratings
	 * @param targetUserID - the user whole knowing rating
	 * @return
	 */
	private List<Double> getRatingsOfUser(HashMap<Integer,HashMap<Integer, Double>> userId2MovieId2rating, List<Integer> movieIDs, int targetUserID){
		List<Double> ratings=new ArrayList<Double>();
		for (int movieId: movieIDs){
			if (userId2MovieId2rating.containsKey(targetUserID) && userId2MovieId2rating.get(targetUserID).containsKey(movieId)){
				ratings.add(userId2MovieId2rating.get(targetUserID).get(movieId));
			}
		}
		return ratings;
	}
	/**
	 * This method calculate similar movies to target movie
	 * @param targetID
	 * @param movieID2genres
	 */
	private HashMap<Integer, List<Integer>> getRankedSimilarMoviesByGenre(int targetID, HashMap<Integer,List<String>> movieID2genres){
		List<String> genresOfTargetMovie=movieID2genres.get(targetID);
		HashMap<Integer, List<Integer>> ranking2similarMovieIDs=new HashMap<Integer, List<Integer>>();
		Set<Integer> genericMovieID=movieID2genres.keySet();
		for (int i: genericMovieID){
			if (i!=targetID){
				int ranking=countMatchingStrings(genresOfTargetMovie, movieID2genres.get(i));
				// qui dovrei fare un controllo nel caso in cui il ranking sia zero, se lo fosse è inutile che lo aggiungo alla mappa
				if (ranking2similarMovieIDs.containsKey(ranking)){
					ranking2similarMovieIDs.get(ranking).add(i);
				}else{
					List<Integer> l=new ArrayList<Integer>();
					l.add(i);
					ranking2similarMovieIDs.put(ranking, l);
				}
			}
		}
		return ranking2similarMovieIDs;
	}
	private HashMap<Integer, List<String>> getMovieID2genres(){
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
	
	private static double calculateMeanAverage(List<Double> l){
		double sum=0;
		for (double d: l){
			sum+=d;
		}
		return sum/l.size();
	}
	
	private static int countMatchingStrings(List<String> gs1, List<String> gs2){
		int i=0;
		for (String g1: gs1){
			if (gs2.contains(g1)){
				i++;
			}
		}
		return i;
	}
	

}
