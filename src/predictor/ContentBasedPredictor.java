package predictor;

import java.util.ArrayList;
import java.util.List;

import model.Dataset;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.UserRatedMoviesParser;
import persistence.PersistenceException;

public class ContentBasedPredictor implements Predictor{

	@Override
	public List<Prediction> calculatePrediction(Dataset inputSet) throws PersistenceException {
		List<Prediction> predictions=new ArrayList<Prediction>();
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		Dataset.visit(inputSet, p);
		for (UserRatedMovie targetMovie: p.getResults()){
			int targetMovieID=targetMovie.getMovieID();
		}
		return null;
	}

}
