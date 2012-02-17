package predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Dataset;
import model.InputFileRow;
import model.Prediction;
import parserAndWriter.InputFileParser;
import persistence.PersistenceException;
import util.DataLoader;
import util.PerformanceMeter;

public class AdvancedGenreBasedPredictor implements Predictor{
	private Map<Integer, Map<Integer,Double>> userId2MovieId2rating;
	private Map<Integer, Double> movieID2rtRating;
	private Map<Integer,Integer[]> moviedId2genreVector;
	private Map<Integer,List<Integer[]>> movieID2genreBasedSimilarVector;
	
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
		
		InputFileParser p=new InputFileParser();
		Dataset.visit(inputSet,p);
		List<InputFileRow> inputList=p.getResults();
		for (int i=0; i<inputList.size();i++){
			InputFileRow inputFileRow=inputList.get(i);
			int targetMovieID=inputFileRow.getMovieID();
			Integer[] targetVector=this.moviedId2genreVector.get(targetMovieID);
			List<Integer[]> similarVectors=this.movieID2genreBasedSimilarVector.get(targetMovieID);
			Map<Integer, List<Integer[]>> ranking2similarVectors=calculateRank2SimilarVector(targetVector, similarVectors);
			
			int max=Collections.max(ranking2similarVectors.keySet());
			List<Integer[]> similarMoviesVectors=ranking2similarVectors.get(max);
			List<Double> ratings=getRatingsOfUserForGenreBasedSimilarMovies(similarMoviesVectors, inputFileRow.getUserID());
			Prediction pred;
			if (ratings.size()==0){
				// e qui c'è un caso che dovrebbe essere analizzato più approfonditamente
//				pred=new Prediction(targetMovie.getUserID(), targetMovieID, 2.5);// qui potrei restituirgli il voto reale di rt
				pred=new Prediction(inputFileRow.getUserID(), targetMovieID, this.movieID2rtRating.get(targetMovieID));
			}else{
				double meanAverage=calculateMeanAverage(ratings);
				double rtRating=this.movieID2rtRating.get(targetMovieID)/2.;
				if (meanAverage>=2.5){
					if (haveSeenGoodMovies(similarMoviesVectors)){
						//restituisco il voto reale, perchè non stravede per il genere e quindi potrebbe non piacergli, diamogli i voti che 
						// gli hanno dato gli altri (rt)
						// non siamo sicuri che stravede per il genere, e quindi meglio andare cauti, e dargli il voto che spetta al film
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, rtRating);
					}else if (haveSeenBadMovies(similarMoviesVectors)){
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, 5);
					}else{
						// stravede per il genere (quindi voto alto più una parte derivante dalla qualità del film)
//						restituisco la media tra il rating dato dagli altri utenti e la media che lui da al genere
						double rat=rtRating+meanAverage/2;
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, rat);
					}
				} else{// anzichè haveSeendGoodMovie meglio haveSeendBadMovies (con i 3/4 di film brutti)
					if (haveSeenGoodMovies(similarMoviesVectors)){// ha dato un voto basso ma i film sono tutti belli
						//gli fa proprio schifo il genere, e quindi meglio fare una media tra il voto che lui da al genere e il voto reale del film (metti che è un filmone, potrebbe piacergli)
						// in modo che se il valore reale è alto, allora è un buon film che potrebbe cmq piacergli
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, meanAverage);
					}else if (haveSeenBadMovies(similarMoviesVectors)){
						// non gli piace il genere, meglio restituire o il voto che da al genere, oppure una media tra il suo voto e
						// quello che altri utenti danno al genere
						// non gli piace ma perchè si è visto
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, rtRating);
					}else{
						pred=new Prediction(inputFileRow.getUserID(), targetMovieID, rtRating);
					}
				}
			}
			predictions.add(pred);
			System.out.println("Aggiunta predizione "+i+": "+pred.toString());
		}
		return predictions;
		
	}
	
	/**
	 * Calcola se il film sono di buona qualità o meno
	 * @param similarMovieVectors - i film da valutare
	 * @return
	 */
	public boolean haveSeenGoodMovies(List<Integer[]> similarMovieVectors){
		Double[] ratings=new Double[similarMovieVectors.size()];
		int goodMoviesNum=0;
		for (int i=0;i<ratings.length;i++){
			if (this.movieID2rtRating.get( similarMovieVectors.get(i)[0] )>5){
				goodMoviesNum++;
			}
		}
		if (goodMoviesNum> 3.*similarMovieVectors.size()/4. ){
			return true;
		}else{
			return false;
		}
	}
	public boolean haveSeenBadMovies(List<Integer[]> similarMovieVectors){
		Double[] ratings=new Double[similarMovieVectors.size()];
		int badMoviesNum=0;
		for (int i=0;i<ratings.length;i++){
			if (this.movieID2rtRating.get( similarMovieVectors.get(i)[0] ) <5){
				badMoviesNum++;
			}
		}
		if (badMoviesNum> 3.*similarMovieVectors.size()/4. ){
			return true;
		}else{
			return false;
		}
	}
	
	public void loadDataStructuresInMemory() throws PersistenceException{
		PerformanceMeter pm=new PerformanceMeter();
		pm.start();
		System.out.println("Loading predictor...");
		DataLoader dl=new DataLoader();
		this.userId2MovieId2rating=dl.loadSeenAndRatedMoviesByUser();
		this.movieID2genreBasedSimilarVector=dl.loadMovieID2genreBasedSimilarVector();
		this.movieID2rtRating=dl.loadMovieID2rtRating();
//		this.moviedId2genreVector=dl.loadMovieID2genresVector();
		System.out.println("Predictor is ready!");
		pm.stop();
	}
	
	/***
	 * This method return a list of user rating for a set of movie
	 * @param userId2MovieId2rating - map that contains movies and relative user rating
	 * @param movieIDs - the list that contains movie ids which knowing ratings
	 * @param targetUserID - the user whole knowing rating
	 * @return
	 */
	private List<Double> getRatingsOfUserForGenreBasedSimilarMovies(List<Integer[]> similarMoviesVectors, int targetUserID){
		List<Double> ratings=new ArrayList<Double>();
		for (Integer[] v: similarMoviesVectors){
			ratings.add( this.userId2MovieId2rating.get(targetUserID).get(v[0]) );
		}
		return ratings;
	}

	private static HashMap<Integer, List<Integer[]>> calculateRank2SimilarVector(Integer[] targetVector, List<Integer[]> similarVectors){
		HashMap<Integer, List<Integer[]>> rank2similarMovieVectors=new HashMap<Integer, List<Integer[]>>();
		for (Integer[] similarVector: similarVectors){
			int rank=0;
			for (int i=0; i<targetVector.length;i++){
				if (targetVector[i]==1 && similarVector[i]==1){
					rank++;
				}
			}
			if (rank2similarMovieVectors.containsKey(rank)){
				rank2similarMovieVectors.get(rank).add(similarVector);
			}else{
				List<Integer[]> l=new ArrayList<Integer[]>();
				l.add(similarVector);
				rank2similarMovieVectors.put(rank, l);
			}
		}
		return rank2similarMovieVectors;
	}
	
	
	private static double calculateMeanAverage(List<Double> l){
		double sum=0;
		for (double d: l){
			sum+=d;
		}
		return sum/l.size();
	}
	
	
	

}
