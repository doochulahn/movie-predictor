package parserAndWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Genre;

public class GenreParser implements Parser{
	List<Genre> genres;

	public GenreParser(){
		this.genres=new ArrayList<Genre>();
	}
	
	@Override
	public void updateParser(String record) {
		this.genres.add(new Genre(record));
	}

	@Override
	public List<Genre> getResults() {
		return this.genres;
	}
	
	public HashMap<Integer,List<String>> fromList2map(){
		HashMap<Integer,List<String>> map=new HashMap<Integer, List<String>>();
		for (Genre g: this.genres){
			if (map.containsKey(g.getMovieID())){
				map.get(g.getMovieID()).add(g.getGenre());
			}else{
				List<String> gs=new ArrayList<String>();
				gs.add(g.getGenre());
				map.put(g.getMovieID(), gs);
			}
		}
		return map;
	}

}
