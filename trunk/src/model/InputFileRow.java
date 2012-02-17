package model;

public class InputFileRow {
	private int userID;
	private int movieID;
	private String timestamp;

	
	
	public InputFileRow(String record){
		String[] attributes=record.split("\t");
		this.userID=Integer.parseInt(attributes[0]);
		this.movieID=Integer.parseInt(attributes[1]);
		this.timestamp=attributes[2];
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
	
	

}
