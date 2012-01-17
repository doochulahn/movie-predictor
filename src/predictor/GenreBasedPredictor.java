package predictor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Dataset;
import model.Genre;
import model.Prediction;
import model.UserRatedMovie;
import parserAndWriter.GenreParser;
import parserAndWriter.UserRatedMoviesParser;

public class GenreBasedPredictor implements Predictor{

	/***
	 * This method, for each target movie getted from input set, return the prediction based on genre.
	 * Particulary, for each target movie are getted other film that have high based genre similarity,
	 * then it calculate mean average of their rating, and this will be the predicted rating.
	 */
	@Override
	public List<Prediction> calculatePrediction(Dataset inputSet) {
//		List<Prediction> records=new ArrayList<Prediction>();
//		Map<Integer,Map<Integer, List<Integer>>> targetMovie2ranking2moviesList=new HashMap<Integer, Map<Integer,List<Integer>>>();
//		UserRatedMoviesParser p=new UserRatedMoviesParser();
//		Dataset.visit(inputSet, p);
//		GenreParser p2=new GenreParser();
//		Dataset.visit(new Dataset("dataset.test/movie_genres.dat",1), p2);
//		HashMap<Integer,List<String>> movieID2genres=p2.fromList2map();
//		for (Object urm :p.getResults()){
//			UserRatedMovie targetMovie= (UserRatedMovie) urm;
//			if (!targetMovie2ranking2moviesList.containsKey(targetMovie.getMovieID())){
//				Map<Integer,List<Integer>> ranking2moviesList=getBasedGenreRanking2movieList(movieID2genres, targetMovie);
//				targetMovie2ranking2moviesList.put(targetMovie.getMovieID(), ranking2moviesList);
//				int max=Collections.max(ranking2moviesList.keySet());
//				
//				for (int movieID:ranking2moviesList.get(max)){
//					List<Double> l=new ArrayList<Double>();
//					HashMap<Integer, List<Double>> m2=p.getMovieID2ratings();
//					List<Double> l2=m2.get(movieID);
//					if (l2!=null){
//						l.addAll(l2);// qui getmovieid dovrebbe essere per riferimento, non metodo
//						double predictValue=this.calculateMeanAverage(l);
//						Prediction pred=new Prediction(targetMovie.getUserID(), targetMovie.getMovieID(), predictValue);
//						records.add(pred);
//					}
//				}
//			}
//		}
//		return records;
		List<Prediction> predictions=new ArrayList<Prediction>();
		UserRatedMoviesParser p=new UserRatedMoviesParser();
		Dataset.visit(inputSet, p);
		HashMap<Integer,List<String>> movieID2genres=this.getMovieID2genres();
		for (UserRatedMovie targetMovie: p.getResults()){
			int targetID=targetMovie.getMovieID();
			List<String> targetMovieGenres=movieID2genres.get(targetID);
			HashMap<Integer, List<Integer>> ranking2similarMovieID=new HashMap<Integer, List<Integer>>();
			for (int i: movieID2genres.keySet()){
				if (i!=targetID){
					int ranking=this.countMatchingStrings(targetMovieGenres, movieID2genres.get(i));
					if (ranking2similarMovieID.containsKey(ranking)){
						ranking2similarMovieID.get(ranking).add(i);
					}else{
						List<Integer> l=new ArrayList<Integer>();
						l.add(i);
						ranking2similarMovieID.put(ranking, l);
					}
				}
			}
			// ora dovrei calcolare la media
			int max=Collections.max(ranking2similarMovieID.keySet());
			HashMap<Integer, HashMap<Integer,Double>> userId2MovieId2rating=p.fromList2map();
			List<Double> ratings=new ArrayList<Double>();
			for (int movieId: ranking2similarMovieID.get(max)){
				ratings.add(userId2MovieId2rating.get(targetMovie.getUserID()).get(movieId));
			}
			Prediction pred=new Prediction(targetMovie.getUserID(), targetID, calculateMeanAverage(ratings));
			predictions.add(pred);
		}
		return predictions;
		
	}
	
	/**
	 * This method select all movies that has almost one genre mathing the target mobie genres, 
	 * and put there in a map sorted by ranking
	 * @param movieID2genres: list of genre of a film
	 * @param targetMovie: target movie
	 * @return map: ranking as keys, movie list as values
	 */
	private static Map<Integer,List<Integer>> getBasedGenreRanking2movieList(HashMap<Integer,List<String>> movieID2genres, UserRatedMovie targetMovie){
		Map<Integer,List<Integer>> ranking2moviesList=new HashMap<Integer,List<Integer>>();
		for (int mid: movieID2genres.keySet()){
			if (mid != targetMovie.getMovieID()){
				int i=0;
				List<String> gs=movieID2genres.get(targetMovie.getMovieID());
				if (gs!=null){
					for (String g: gs){
						if (gs.contains(g)){
							i++;
						}
					}
				}
				if (ranking2moviesList.containsKey(i)){
					ranking2moviesList.get(i).add(mid);
				}else{
					List<Integer> l=new ArrayList<Integer>();
					l.add(mid);
					ranking2moviesList.put(mid, l);
				}
				i=0;
			}
		}
		return ranking2moviesList;
	}
	
	private HashMap<Integer, List<String>> getMovieID2genres(){
		HashMap<Integer, List<String>> movieID2genres=new HashMap<Integer, List<String>>();
		GenreParser p=new GenreParser();
		Dataset.visit(new Dataset("dataset/genre_movies.dat",1),p);
		for (Genre g: p.getResults()){
			if (movieID2genres.containsKey(g.getMovieID())){
				movieID2genres.get(g.getMovieID()).add(g.getGenre());
			}else{
				List<String> l=new ArrayList<String>();
				l.add(g.getGenre());
				movieID2genres.put(g.getMovieID(), l);
			}
		}
		return movieID2genres;
	}
	
	private static double calculateMeanAverage(List<Double> l){
		double sum=0;
		for (double d: l){
			sum+=d;
		}
		return sum/l.size();
	}
	
	private static int countMatchingStrings(List<String> gs1, List<String> gs2){
		int i=0;
		for (String g1: gs1){
			if (gs2.contains(g1)){
				i++;
			}
		}
		return i;
	}
	
	// per ogni movieID, ho la mappa id2generi
	// per ogni target movie, conto quanti generi del target movie sono compresi nel movie generico
	// aggiorno la mappa di conseguenza

}
