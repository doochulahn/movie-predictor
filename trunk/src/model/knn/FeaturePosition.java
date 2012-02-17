package model.knn;

public enum FeaturePosition {
	TOP250MOVIE(0),
	RTID(1),
	TOP100ACTORSNUMBER(2),
	ID(3);
	
	private int position;
	
	private FeaturePosition(int position){
		this.position=position;
	}
	
	public int getPosition(){
		return this.position;
	}

}
