package persistence;

import java.util.List;

import persistence.mysql.GenreDaoImpl;
import persistence.mysql.UserMovieRatingsDaoImpl;

public class PersistenceFacade {
	
	public Integer[] getGenreVectorByMovieID(int movieID) throws PersistenceException{
		GenreDao dao=new GenreDaoImpl();
		return dao.retrieveGenreVectorByMovieID(movieID);
	}
	
	public List<Integer[]> getSimilarByGenreVectors(Integer[] vector) throws PersistenceException{
		GenreDao dao=new GenreDaoImpl();
		return dao.retrieveSimilarVectors(vector);
	}
	
	public List<Double> getRatingsByGenreBasedVectors(List<Integer[]> vectors) throws PersistenceException{
		UserMovieRatingsDao dao=new UserMovieRatingsDaoImpl();
		return dao.retrieveRatingsByMovieVectors(vectors);
	}

}
