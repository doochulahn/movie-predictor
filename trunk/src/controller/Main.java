package controller;

import java.io.IOException;

import model.Dataset;
import parserAndWriter.OutputWriter;
import predictor.StupidPredictor;
import tester.Tester;
import util.PerformanceMeter;

public class Main {
	final String DATASET_FOLDER="dataset.test";
	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		Main m=new Main();
		PerformanceMeter pm=new PerformanceMeter();
		pm.start();

		
		System.out.println("Starting classification");
		
		OutputWriter outputWriter=new OutputWriter();
		Dataset inputDataset=new Dataset(m.DATASET_FOLDER+"/user_ratedmovies.dat", 1);
		Dataset outputDataset=new Dataset(m.DATASET_FOLDER+"/output.dat");
		
		StupidPredictor stupidClassifier=new StupidPredictor();
		outputWriter.write(stupidClassifier.calculatePrediction(inputDataset), outputDataset);
//		GenreBasedPredictor gbp=new GenreBasedPredictor();
//		outputWriter.write(gbp.calculatePrediction(inputDataset), outputDataset);
		
		System.out.println("End classification");
		pm.stop();
		
		System.out.println("Starting test");
		
		Dataset testSet=new Dataset(m.DATASET_FOLDER+"/user_ratedmovies.dat", 1);
		Dataset predictedSet=new Dataset(m.DATASET_FOLDER+"/output.dat", 1);
		Tester t=new Tester(testSet, predictedSet);
		
		double error=t.calculateMAE();
		
		System.out.println("L'errore medio è di: "+error);
		
		System.out.println("End test");
		
		pm.stop();
		
	}
	
}
