package model;

public class Prediction {
	private int userId;
	private int movieId;
	private double rating;
	
	
	public Prediction(int userId, int movieId, double rating) {
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}
	
	public Prediction(String record){
		String[] attributes=record.split("\t");
		this.userId=Integer.parseInt(attributes[0]);
		this.movieId=Integer.parseInt(attributes[1]);
		this.rating=Double.parseDouble(attributes[2]);
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public int getMovieId() {
		return movieId;
	}


	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}


	public double getRating() {
		return rating;
	}


	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public String toString(){
		return this.userId+"\t"+this.movieId+"\t"+this.rating;
	}
	
	
	

}
