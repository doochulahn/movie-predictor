package tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Dataset;
import parserAndWriter.PredictedRatingsParser;
import parserAndWriter.UserRatedMoviesParser;

public class Tester {
	private Dataset testDataset;
	private Dataset predictedDataset;
	
	
	
	public Tester(Dataset realPredictions, Dataset calculatedPredictions) {
		this.testDataset = realPredictions;
		this.predictedDataset = calculatedPredictions;
	}

	/**
	 * This method gets data from "output.dat" file, 
	 * and compare its values of rating column with the real values of rating column getted from "user_ratedmovies.dat"
	 * 
	 * @return Return double, the Mean Average Error
	 */
	public double calculateMAE(){
		Map<Integer, Map<Integer,Double>> userId2movieid2realPredictions=this.getRealPredictions();
		Map<Integer, Map<Integer,Double>> userId2movieId2calculatedPredictions=this.getCalculatedPredictions();
		ArrayList<Double> diffValues=new ArrayList<Double>();
		for (int userID: userId2movieId2calculatedPredictions.keySet()){
			Map<Integer,Double> movieID2ratingFromRealPrediction=userId2movieid2realPredictions.get(userID);
			Map<Integer,Double> movieID2ratingFromCalculatePrediction=userId2movieId2calculatedPredictions.get(userID);
			for (int movieID: movieID2ratingFromCalculatePrediction.keySet()){
				double calculatedPred=movieID2ratingFromCalculatePrediction.get(movieID);
				if (movieID2ratingFromRealPrediction.containsKey(movieID)){
					double realPred=movieID2ratingFromRealPrediction.get(movieID);
					double diff=calculatedPred-realPred;
					double module=Math.abs(diff);
					diffValues.add(module);
				}
			}
		}
		double sum=0;
		for (double d: diffValues){
			sum += d;
		}
		return sum/diffValues.size();
	}
	
	public Map<Integer, Map<Integer,Double>> getRealPredictions(){
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		Dataset.visit(this.testDataset, p);
		return p.fromList2map();
	}
	
	public Map<Integer, Map<Integer,Double>> getCalculatedPredictions(){
		PredictedRatingsParser p=new PredictedRatingsParser();
		Dataset.visit(this.predictedDataset, p);
		return p.fromList2map();
	}
	

}
