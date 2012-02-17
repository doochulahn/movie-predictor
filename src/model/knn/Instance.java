package model.knn;


public class Instance {
	Feature[] features;

	public Instance(){
		this.features=new Feature[4];
	}
	
	public Feature[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature[] features) {
		this.features = features;
	}
	
	public void addFeature(FeaturePosition position, Feature feature){
		this.features[position.getPosition()]= feature;
	}
	
	public Feature get(FeaturePosition p){
		return this.features[p.getPosition()];
	}
	
	

}
