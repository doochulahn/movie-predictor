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

public class KnnPredictor implements Predictor{
	private Map<Integer,Instance> id2instance;
	private final static int K=40;
	
	public KnnPredictor() throws PersistenceException{
		this.loadDataStructuresInMemory();
	}

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
			Neighbors neighbors=calculateNeighbour(targetInstance);
			double predictedRating=calculateAverageRating(neighbors.getFirstKneighbors(K));
			Prediction pred=new Prediction(row.getUserID(), targetMovieID, predictedRating/2);
			predictions.add(pred);
			System.out.println("Aggiunta predizione "+i+": "+pred.toString());
		}
		return predictions;
	}
	
	private double calculateAverageRating(List<Instance> firstKneighbors) {
		double tmpAverage=0.;
		for (Instance i: firstKneighbors){
			tmpAverage+=(Double) i.get(FeaturePosition.RTID).getValue();
		}
		return tmpAverage/firstKneighbors.size();
	}

	private Neighbors calculateNeighbour(Instance targetInstance) {
		Neighbors neighbors=new Neighbors();
		Collection<Instance> allInstances=this.id2instance.values();
		for (Instance i: allInstances){
			neighbors.addInstance(i, calculateEuclidianDistance(targetInstance, i));
		}		
		return neighbors;
	}

	private double calculateEuclidianDistance(Instance targetInstance, Instance i) {
		double distance=0.;
		double tmp1;
		tmp1=(Double) targetInstance.get(FeaturePosition.RTID).getDistanceThan( i.get(FeaturePosition.RTID) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.TOP100ACTORSNUMBER).getDistanceThan( i.get(FeaturePosition.TOP100ACTORSNUMBER) );
		distance+=Math.pow(tmp1, 2);
		tmp1=(Integer) targetInstance.get(FeaturePosition.TOP250MOVIE).getDistanceThan( i.get(FeaturePosition.TOP250MOVIE) );
		distance+=Math.pow(tmp1, 2);
		return Math.sqrt(distance);	
	}

	public void loadDataStructuresInMemory() throws PersistenceException{
		PerformanceMeter pm=new PerformanceMeter();
		pm.start();
		System.out.println("Loading predictor...");
		//
		DataLoader dl=new DataLoader();
		this.id2instance=dl.loadAllInstances();
		System.out.println("Predictor is ready!");
		pm.stop();
	}

}
