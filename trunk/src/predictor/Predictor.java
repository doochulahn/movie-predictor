package predictor;

import java.util.List;

import model.Dataset;
import model.Prediction;

public interface Predictor {
	
	/**
	 * This method calculates predictions for each movie that is in inputset dataset
	 * @param inputSet: Dataset object from which get movies
	 * @return List of Prediction objects
	 */
	public List<Prediction> calculatePrediction(Dataset inputSet);

}
