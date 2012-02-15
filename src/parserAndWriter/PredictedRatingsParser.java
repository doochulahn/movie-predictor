package parserAndWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Prediction;

public class PredictedRatingsParser implements Parser{
	private List<Prediction> predictions;
	
	public PredictedRatingsParser(){
		this.predictions=new ArrayList<Prediction>();
	}
	
	public Map<Integer, Map<Integer,Double>> fromList2map(){
		Map<Integer, Map<Integer,Double>> map=new HashMap<Integer, Map<Integer,Double>>();
		for (Prediction p: this.predictions){
			if (map.get(p.getUserId())!=null){
				Map<Integer,Double> m1=map.get(p.getUserId());
				if (m1.get(p.getMovieId())!=null){
					System.out.println("Problema");
					//m1.put(Integer.parseInt(urm.getMovieID()), Double.parseDouble(urm.getRating()) );
				} else{
					m1.put(p.getMovieId(), p.getRating() );
				}
			} else{
				HashMap<Integer,Double> m1=new HashMap<Integer, Double>();
				m1.put(p.getMovieId(), p.getRating() );
				map.put(p.getUserId(), m1);
			}
		}
		return map;
	}

	@Override
	public void updateParser(String record) {
		this.predictions.add(new Prediction(record));
		
	}

	@Override
	public List<? extends Object> getResults() {
		return this.predictions;
	}

}
