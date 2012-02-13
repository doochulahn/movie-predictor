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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + movieID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (movieID != other.movieID)
			return false;
		return true;
	}
	
	
	
	

}
