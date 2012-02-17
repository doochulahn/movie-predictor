package predictor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import model.Dataset;
import model.InputFileRow;
import model.Prediction;
import model.knn.FeaturePosition;
import model.knn.Instance;
import model.knn.Neighbors;
import parserAndWriter.InputFileParser;
import persistence.PersistenceException;
import util.DataLoader;
import util.PerformanceMeter;


/***
 * This class implements the k-nn algorithm. 
 * It has method to get all instance in memory, get the neighbors of each instance.
 * The distance of each movie is calculated with Euclidian distance.
 * @author bitliner
 *
 */
public class KnnPredictor implements Predictor{
	private Map<Integer,Instance> id2instance;
	private final static int K=1000;

	public KnnPredictor() throws PersistenceException{
		this.loadDataStructuresInMemory();
	}

	
	/****
	 * This method has as input a dataset, that contains the data of movies of which making preictions,
	 * and returns a list of predictions.
	 */
	@Override
	public List<Prediction> calculatePrediction(Dataset inputSet)throws PersistenceException {
		List<Prediction> predictions=new ArrayList<Prediction>();
		InputFileParser p=new InputFileParser();
		Dataset.visit(inputSet,p);
		List<InputFileRow> inputList=p.getResults();

		for (int i=0; i<inputList.size();i++){
			InputFileRow row=inputList.get(i);
			int targetMovieID=row.getMovieID();
			Instance targetInstance=this.id2instance.get(targetMovieID);
			Neighbors neighbors=calculateNeighbors(targetInstance);
			double predictedRating=calculateAverageRating(neighbors.getFirstKneighbors(K));
			predictedRating=roundToFirstPlaceAfterComma(predictedRating);
			
			Prediction pred=new Prediction(row.getUserID(), targetMovieID, predictedRating/2.);
			
			predictions.add(pred);
			System.out.println("Aggiunta predizione "+i+": "+pred.toString());
		}
		return predictions;
	}

	/***
	 * It calculates the average between ratings of input instances 
	 * @param firstKneighbors
	 * @return
	 */
	private double calculateAverageRating(List<Instance> firstKneighbors) {
		double tmpAverage=0.;
		for (Instance i: firstKneighbors){
			tmpAverage+=(Double) i.get(FeaturePosition.RTID).getValue();
		}
		return tmpAverage/firstKneighbors.size();
	}

	/***
	 * It calculates the neighboring instances ot target instance
	 * @param targetInstance - the instance of which calculating the neighbors
	 * @return - a object Neighbors that represents all neighboring instances
	 */
	private Neighbors calculateNeighbors(Instance targetInstance) {
		Neighbors neighbors=new Neighbors();
		Collection<Instance> allInstances=this.id2instance.values();
		for (Instance i: allInstances){
			neighbors.addInstance(i, calculateEuclidianDistance(targetInstance, i));
		}		
		return neighbors;
	}

	/***
	 * This method calculate the euclidian distance between two instances
	 * @param targetInstance - the target instance
	 * @param i - a generic instance of neighbors of target instance
	 * @return - double that represents the distance
	 */
	private double calculateEuclidianDistance(Instance targetInstance, Instance i) {
		double distance=0.;
		double tmp1;
		tmp1=(Double) targetInstance.get(FeaturePosition.RTID).getDistanceThan( i.get(FeaturePosition.RTID) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.TOP100ACTORSNUMBER).getDistanceThan( i.get(FeaturePosition.TOP100ACTORSNUMBER) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.TOP250MOVIE).getDistanceThan( i.get(FeaturePosition.TOP250MOVIE) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.TOP_ACTORS_COUNT).getDistanceThan( i.get(FeaturePosition.TOP_ACTORS_COUNT) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.MEDIAN_ACTORS_COUNT).getDistanceThan( i.get(FeaturePosition.MEDIAN_ACTORS_COUNT) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.BOTTOM_ACTORS_COUNT).getDistanceThan( i.get(FeaturePosition.BOTTOM_ACTORS_COUNT) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.IS_DIRECTOR_IN_TOP100_IMDB_DIRECTORS).getDistanceThan( i.get(FeaturePosition.IS_DIRECTOR_IN_TOP100_IMDB_DIRECTORS) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.DIRECTOR_POSITION_IN_TOP100).getDistanceThan( i.get(FeaturePosition.DIRECTOR_POSITION_IN_TOP100) );
		distance+=Math.pow(tmp1, 2);
		return Math.sqrt(distance);	
	}

	/****
	 * This method load in memory an HashMap that contains all instances of movies
	 * @throws PersistenceException
	 */
	private void loadDataStructuresInMemory() throws PersistenceException{
		PerformanceMeter pm=new PerformanceMeter();
		pm.start();
		System.out.println("Loading predictor...");
		//
		DataLoader dl=new DataLoader();
		this.id2instance=dl.loadAllInstances();
		System.out.println("Predictor is ready!");
		pm.stop();
	}

	/****
	 * This method convert the format of input rating in a target format: 
	 * - only one place after comma (e.g. 3.4 becomes 3.5, 3.8 becomes 4.0)
	 * - rating in range [0,5] 
	 * @param predictedRating - the rating with innappropriate format
	 * @return - the rating with right format
	 */
	private static double roundToFirstPlaceAfterComma(double predictedRating){
		int decimalPlace=0;
		double power_of_ten = 1;
		double fudge_factor = 0.05;
		while (decimalPlace-- > 0) {
			power_of_ten *= 10.0d;
			fudge_factor /= 10.0d;
		}
		return Math.round((predictedRating + fudge_factor)* power_of_ten)  / power_of_ten;
	}

}
