package predictor;

import java.util.List;

import persistence.PersistenceException;

import model.Dataset;
import model.Prediction;

public interface Predictor {
	
	/**
	 * This method calculates predictions for each movie that is in inputset dataset
	 * @param inputSet: Dataset object from which get movies
	 * @return List of Prediction objects
	 * @throws PersistenceException 
	 */
	public List<Prediction> calculatePrediction(Dataset inputSet) throws PersistenceException;

}
