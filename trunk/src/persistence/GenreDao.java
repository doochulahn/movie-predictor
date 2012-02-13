package persistence;

import java.util.List;




public interface GenreDao{

	

	public Integer[] retrieveGenreVectorByMovieID( int movieID) throws PersistenceException;
	
	public List<Integer[]> retrieveSimilarVectors(Integer[] vector) throws PersistenceException;
	


}