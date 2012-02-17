package model.knn;



public class Instance {
	Feature[] features;

	public Instance(){
		this.features=new Feature[FeaturePosition.SIZE.getValue()];
	}
	
	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}
	
	public void addFeature(FeaturePosition position, Feature feature){
		this.features[position.getValue()]= feature;
	}
	
	public Feature get(FeaturePosition p){
		return this.features[p.getValue()];
	}

	@Override
	public String toString() {
		String res="";
		for (int i=0; i<this.features.length; i++){
			res+="\n\t"+this.features[i];
		}
		return res;
	}
	
	
	
	

}
