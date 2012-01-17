package predictor;

import java.util.ArrayList;
import java.util.List;

import model.Dataset;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.Parser;
import parserAndWriter.UserRatedMoviesParser;

public class StupidPredictor implements Predictor {
	
	/**
	 * This method return the predictions calculated from dataset passed as parameter
	 * @return
	 */
	public List<Prediction> calculatePrediction(Dataset trainingSet){
		//, 339974);
		Parser p=new UserRatedMoviesParser();
		Dataset.visit(trainingSet, p);
		ArrayList<Prediction> records=new ArrayList<Prediction>();
		for (Object urm :p.getResults()){
			UserRatedMovie urm2= (UserRatedMovie) urm;
			Prediction pred=new Prediction(urm2.getUserID(),urm2.getMovieID(),2.5);
			records.add(pred);
		}
		return records;
	}


	
	
	
	

}
