package parserAndWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.UserRatedMovie;

public class UserRatedMoviesParser implements Parser{
	List<UserRatedMovie> userRatedMovies;
	
	public UserRatedMoviesParser() {
		this.userRatedMovies=new ArrayList<UserRatedMovie>();
	}


	@Override
	public void updateParser(String record) {
		UserRatedMovie urm=new UserRatedMovie(record);
		this.userRatedMovies.add(urm);
		
	}

	@Override
	public List<UserRatedMovie> getResults() {
		return this.userRatedMovies;
	}
	
	public Map<Integer, Map<Integer,Double>> fromList2map(){
		Map<Integer, Map<Integer,Double>> map=new HashMap<Integer, Map<Integer,Double>>();
		for (UserRatedMovie urm: this.userRatedMovies){
			if (map.get(urm.getUserID())!=null){
				Map<Integer,Double> m1=map.get(urm.getUserID());
				if (m1.get(urm.getMovieID())!=null){
					System.out.println("Problema");
					//m1.put(Integer.parseInt(urm.getMovieID()), Double.parseDouble(urm.getRating()) );
				} else{
					m1.put(urm.getMovieID(), urm.getRating() );
				}
			} else{
				HashMap<Integer,Double> m1=new HashMap<Integer, Double>();
				m1.put(urm.getMovieID(), urm.getRating() );
				map.put(urm.getUserID(), m1);
			}
		}
		return map;
	}
	
	public HashMap<Integer,List<Double>> getMovieID2ratings(){
		HashMap<Integer,List<Double>> map=new HashMap<Integer, List<Double>>();
		for (UserRatedMovie urm: this.userRatedMovies){
			if (map.containsKey(urm.getMovieID())){
				map.get(urm.getMovieID()).add(urm.getRating());
			}else{
				List<Double> rs=new ArrayList<Double>();
				rs.add(urm.getRating());
				map.put(urm.getMovieID(), rs);
			}
		}
		return map;
	}

}
