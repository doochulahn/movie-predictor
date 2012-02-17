package controller;

import java.io.IOException;

import model.Dataset;
import parserAndWriter.OutputWriter;
import persistence.PersistenceException;
import predictor.AdvancedGenreBasedPredictor;
import predictor.KnnPredictor;
import predictor.StupidPredictor;
import tester.Tester;
import util.PerformanceMeter;

public class Main {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, PersistenceException{
		String fileInputName=args[0];
		String fileOutputName=args[1];
		
		PerformanceMeter pm=new PerformanceMeter();
		pm.start();

		
		System.out.println("Starting classification");
		
		OutputWriter outputWriter=new OutputWriter();
		Dataset inputDataset=new Dataset(fileInputName, 1,20000);
		
//		StupidPredictor stupidClassifier=new StupidPredictor();
//		outputWriter.write(stupidClassifier.calculatePrediction(inputDataset), outputDataset);
//		GenreBasedPredictor gbp=new GenreBasedPredictor();
//		outputWriter.write(gbp.calculatePrediction(inputDataset), outputDataset);
//		GenreDatabaseBasedPredictor gbp=new GenreDatabaseBasedPredictor();
//		outputWriter.write(gbp.calculatePrediction(inputDataset), outputDataset);
//		AdvancedGenreBasedPredictor agbp=new AdvancedGenreBasedPredictor();
//		outputWriter.write(agbp.calculatePrediction(inputDataset), fileOutputName);
		KnnPredictor kp=new KnnPredictor();
		outputWriter.write(kp.calculatePrediction(inputDataset), fileOutputName);
		
		System.out.println("End classification");
		pm.stop();
		
	}
	
}
