package model;

public class UserRatedMovie {
	private int userID;
	private int movieID;
	private double rating;
	private String timestamp;

	
	
	public UserRatedMovie(String record){
		String[] attributes=record.split("\t");
		this.userID=Integer.parseInt(attributes[0]);
		this.movieID=Integer.parseInt(attributes[1]);
		this.rating=Double.parseDouble(attributes[2]);
		this.timestamp=attributes[3];
	}
	

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
	
	

}
