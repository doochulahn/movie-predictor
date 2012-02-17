package predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Dataset;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.UserRatedMoviesParser;
import persistence.PersistenceException;
import util.DataLoader;

public class AdvancedGenreBasedPredictor implements Predictor{
	private Map<Integer, Map<Integer,Double>> userId2MovieId2rating;
	private Map<Integer,List<String>> movieID2genres;
	private Map<Integer, Double> movieID2rtRating;
	
	public AdvancedGenreBasedPredictor() throws PersistenceException{
		loadDataStructuresInMemory();
	}
	
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
		Dataset.visit(inputSet,p);
		List<UserRatedMovie> inputList=p.getResults();
		for (int i=0; i<inputList.size();i++){
			UserRatedMovie targetMovie=inputList.get(i);
			
			///////
			Prediction pred=this.makeSinglePrediction(targetMovie);
			predictions.add(pred);
			System.out.println("Aggiunta predizione "+i+": "+pred.toString());
		}
		return predictions;
		
	}
	
	public Prediction makeSinglePrediction(UserRatedMovie targetMovie){
		
		int targetMovieID=targetMovie.getMovieID();
		Map<Integer, List<Integer>> ranking2similarMovieIDs=getRankedSimilarMoviesByGenre(targetMovieID, movieID2genres);
		
		int max=Collections.max(ranking2similarMovieIDs.keySet());
		List<Integer> similarMovieIDs=ranking2similarMovieIDs.get(max);
		List<Double> ratings=this.getRatingsOfUser(userId2MovieId2rating, similarMovieIDs, targetMovie.getUserID());
		Prediction pred;
		if (ratings.size()==0){
			// e qui c'è un caso che dovrebbe essere analizzato più approfonditamente
//			pred=new Prediction(targetMovie.getUserID(), targetMovieID, 2.5);// qui potrei restituirgli il voto reale di rt
			pred=new Prediction(targetMovie.getUserID(), targetMovieID, 2.5);
		}else{
			double meanAverage=calculateMeanAverage(ratings);
			pred=new Prediction(targetMovie.getUserID(), targetMovieID, meanAverage);
		}
		return pred;
	}
	
	/**
	 * Calcola se il film sono di buona qualità o meno
	 * @param similarMovieIDs - i film da valutare
	 * @return
	 */
	public boolean haveSeenGoodMovies(List<Integer> similarMovieIDs){
		Double[] ratings=new Double[similarMovieIDs.size()];
		int goodMoviesNum=0;
		for (int i=0;i<ratings.length;i++){
			if (similarMovieIDs.get(i)>5){
				goodMoviesNum++;
			}
		}
		if (goodMoviesNum> 3.*similarMovieIDs.size()/4. ){
			return true;
		}else{
			return false;
		}
	}
	public boolean haveSeenBadMovies(List<Integer> similarMovieIDs){
		Double[] ratings=new Double[similarMovieIDs.size()];
		int badMoviesNum=0;
		for (int i=0;i<ratings.length;i++){
			if (similarMovieIDs.get(i)<5){
				badMoviesNum++;
			}
		}
		if (badMoviesNum> 3.*similarMovieIDs.size()/4. ){
			return true;
		}else{
			return false;
		}
	}
	
	public void loadDataStructuresInMemory() throws PersistenceException{
		System.out.println("Loading predictor...");
		DataLoader dl=new DataLoader();
		this.userId2MovieId2rating=dl.loadSeenAndRatedMoviesByUser();
		this.movieID2genres=dl.loadMovieID2genres();
		this.movieID2rtRating=dl.loadMovieID2rtRating();
//		quali strutture dati mi servono?
		System.out.println("Predictor is ready!");
	}
	
	/***
	 * This method return a list of user rating for a set of movie
	 * @param userId2MovieId2rating - map that contains movies and relative user rating
	 * @param movieIDs - the list that contains movie ids which knowing ratings
	 * @param targetUserID - the user whole knowing rating
	 * @return
	 */
	private List<Double> getRatingsOfUser(Map<Integer,Map<Integer, Double>> userId2MovieId2rating, List<Integer> movieIDs, int targetUserID){
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
	private Map<Integer, List<Integer>> getRankedSimilarMoviesByGenre(int targetID, Map<Integer,List<String>> movieID2genres){
		List<String> genresOfTargetMovie=movieID2genres.get(targetID);
		Map<Integer, List<Integer>> ranking2similarMovieIDs=new HashMap<Integer, List<Integer>>();
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
