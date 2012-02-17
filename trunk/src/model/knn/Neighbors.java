package model.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neighbors {
	private Map<Double,List<Instance>> neighbors;
	
	public Neighbors(){
		this.neighbors=new HashMap<Double, List<Instance>>();
	}
	
	public void addInstance(Instance i, double distance){
		if (this.neighbors.containsKey(distance)){
			this.neighbors.get(distance).add(i);
		}else{
			List<Instance> instances=new ArrayList<Instance>();
			instances.add(i);
			this.neighbors.put(distance, instances);
		}
	}
	
	public List<Instance> getFirstKneighbors(int k){
		List<Instance> result=new ArrayList<Instance>();
		int i=0;
		List<Double> keys=new ArrayList<Double>(this.neighbors.keySet());
		Collections.sort(keys);
		for (Double key: keys){
			List<Instance> neighborsTemp=this.neighbors.get(key);
			int n=neighborsTemp.size();
			if (i<k){
				if (n<=(k-i)){
					result.addAll(neighborsTemp);
					i+=n;
				}else{
					for (int j=0; j<(k-i); j++){
						result.add(neighborsTemp.get(j));
					}
					i+=(k-i);
				}
			}else if (i==k){
				return result;
			}
		}
		return result;
	}
	
	

}
