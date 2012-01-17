package model;

public class Genre {
	private int movieID;
	private String genre;
	
	
	public Genre(int movieID, String genre) {
		this.movieID = movieID;
		this.genre = genre;
	}
	
	public Genre(String line) {
		String[] attributes=line.split("\t");
		this.movieID=Integer.parseInt(attributes[0]);
		this.genre=attributes[1];
	}
	
	
	public int getMovieID() {
		return movieID;
	}
	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	
	

}
