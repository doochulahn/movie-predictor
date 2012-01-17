package parserAndWriter;

import java.util.ArrayList;
import java.util.List;

import model.Movie;

public class MoviesParser implements Parser{
	private List<Movie> movies;

	@Override
	public void updateParser(String record) {
		this.movies.add(new Movie(record));
	}

	@Override
	public List<? extends Object> getResults() {
		return this.movies;
	}
	
	public MoviesParser(){
		this.movies=new ArrayList<Movie>();
	}

	

}
