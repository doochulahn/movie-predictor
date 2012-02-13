package predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import model.Dataset;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.UserRatedMoviesParser;
import persistence.PersistenceException;
import persistence.PersistenceFacade;

public class GenreBasedPredictorv2 implements Predictor{

	/***
	 * This method, for each target movie (about which make a prediction, and getted from input dataset), 
	 * return the prediction calculated by mean average of ratings of same user about similar movies.
	 * The similarity among movies is based on genre; 
	 * so if a generic movie have genres that matching the genres of target movie, these are similar.
	 * @throws PersistenceException 
	 */
	@Override
	public List<Prediction> calculatePrediction(Dataset inputSet) throws PersistenceException {
		List<Prediction> predictions=new ArrayList<Prediction>();
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		PersistenceFacade pf=new PersistenceFacade();
		Dataset.visit(inputSet, p);
		for (UserRatedMovie targetMovie: p.getResults()){
			int targetID=targetMovie.getMovieID();
			int userID=targetMovie.getUserID();
			Integer[] targetVector=pf.getGenreVectorByMovieID(targetID);
			List<Integer[]> similarVectors=pf.getSimilarByGenreVectors(targetVector);
			HashMap<Integer, List<Integer[]>> rank2similarVectors=calculateRankOfSimilarVector(targetVector, similarVectors);
			int max=Collections.max(rank2similarVectors.keySet());
			List<Double> ratings=pf.getRatingsByGenreBasedVectors(rank2similarVectors.get(max));
			Prediction pred;
			if (ratings.size()==0){
				pred=new Prediction(userID, targetID, 2.5); // NB: valore assegnato valido?
			}else{
				pred=new Prediction(userID, targetID, calculateMeanAverage(ratings));
			}
			predictions.add(pred);
			System.out.println("Aggiunta predizione: "+pred.toString());
		}
		
		return null;
		
	}
	
	private static HashMap<Integer, List<Integer[]>> calculateRankOfSimilarVector(Integer[] targetVector, List<Integer[]> similarVectors){
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
