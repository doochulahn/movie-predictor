package model.knn;

public enum FeaturePosition {
	SIZE(9),
	TOP250MOVIE(0),
	RTID(1),
	TOP100ACTORSNUMBER(2),
	ID(3),
	BOTTOM_ACTORS_COUNT(4),
	TOP_ACTORS_COUNT(5),
	MEDIAN_ACTORS_COUNT(6),
	IS_DIRECTOR_IN_TOP100_IMDB_DIRECTORS(7),
	DIRECTOR_POSITION_IN_TOP100(8);
	
	private int position;
	
	private FeaturePosition(int position){
		this.position=position;
	}
	
	public int getValue(){
		return this.position;
	}

}
