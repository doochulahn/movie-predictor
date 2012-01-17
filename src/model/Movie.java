package model;

public class Movie {
	private String id;
	private String title;
	private String imdbID;
	private String spanishTitle;
	private String imdbPictureURL;
	private String year;
	private String rtID;
	private String rtAllCriticsRating;
	private String rtAllCriticsNumReviews;
	private String rtAllCriticsNumFresh;
	private String rtAllCriticsNumRotten;
	private String rtAllCriticsScore;
	private String rtTopCriticsRating;
	private String rtTopCriticsNumReviews;
	private String rtTopCriticsNumFresh;
	private String rtTopCriticsNumRotten;
	private String rtTopCriticsScore;
	private String rtAudienceRating;
	private String rtAudienceNumRatings;
	private String rtAudienceScore;
	private String rtPictureUrl;
	
	public Movie(String record){
		String[] attributes=record.split("\t");
		this.id=attributes[0];
		this.title=attributes[1];
		this.imdbID=attributes[2];
		this.spanishTitle=attributes[3];
		this.imdbPictureURL=attributes[4];
		this.year=attributes[5];
		this.rtID=attributes[6];
		this.rtAllCriticsRating=attributes[7];
		this.rtAllCriticsNumReviews=attributes[8];
		this.rtAllCriticsNumFresh=attributes[9];
		this.rtAllCriticsNumRotten=attributes[10];
		this.rtTopCriticsScore=attributes[11];
		this.rtAudienceRating=attributes[12];
		this.rtAudienceNumRatings=attributes[13];
		this.rtAudienceScore=attributes[14];
		this.rtPictureUrl=attributes[15];
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImdbID() {
		return imdbID;
	}
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	public String getSpanishTitle() {
		return spanishTitle;
	}
	public void setSpanishTitle(String spanishTitle) {
		this.spanishTitle = spanishTitle;
	}
	public String getImdbPictureURL() {
		return imdbPictureURL;
	}
	public void setImdbPictureURL(String imdbPictureURL) {
		this.imdbPictureURL = imdbPictureURL;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getRtID() {
		return rtID;
	}
	public void setRtID(String rtID) {
		this.rtID = rtID;
	}
	public String getRtAllCriticsRating() {
		return rtAllCriticsRating;
	}
	public void setRtAllCriticsRating(String rtAllCriticsRating) {
		this.rtAllCriticsRating = rtAllCriticsRating;
	}
	public String getRtAllCriticsNumReviews() {
		return rtAllCriticsNumReviews;
	}
	public void setRtAllCriticsNumReviews(String rtAllCriticsNumReviews) {
		this.rtAllCriticsNumReviews = rtAllCriticsNumReviews;
	}
	public String getRtAllCriticsNumFresh() {
		return rtAllCriticsNumFresh;
	}
	public void setRtAllCriticsNumFresh(String rtAllCriticsNumFresh) {
		this.rtAllCriticsNumFresh = rtAllCriticsNumFresh;
	}
	public String getRtAllCriticsNumRotten() {
		return rtAllCriticsNumRotten;
	}
	public void setRtAllCriticsNumRotten(String rtAllCriticsNumRotten) {
		this.rtAllCriticsNumRotten = rtAllCriticsNumRotten;
	}
	public String getRtAllCriticsScore() {
		return rtAllCriticsScore;
	}
	public void setRtAllCriticsScore(String rtAllCriticsScore) {
		this.rtAllCriticsScore = rtAllCriticsScore;
	}
	public String getRtTopCriticsRating() {
		return rtTopCriticsRating;
	}
	public void setRtTopCriticsRating(String rtTopCriticsRating) {
		this.rtTopCriticsRating = rtTopCriticsRating;
	}
	public String getRtTopCriticsNumReviews() {
		return rtTopCriticsNumReviews;
	}
	public void setRtTopCriticsNumReviews(String rtTopCriticsNumReviews) {
		this.rtTopCriticsNumReviews = rtTopCriticsNumReviews;
	}
	public String getRtTopCriticsNumFresh() {
		return rtTopCriticsNumFresh;
	}
	public void setRtTopCriticsNumFresh(String rtTopCriticsNumFresh) {
		this.rtTopCriticsNumFresh = rtTopCriticsNumFresh;
	}
	public String getRtTopCriticsNumRotten() {
		return rtTopCriticsNumRotten;
	}
	public void setRtTopCriticsNumRotten(String rtTopCriticsNumRotten) {
		this.rtTopCriticsNumRotten = rtTopCriticsNumRotten;
	}
	public String getRtTopCriticsScore() {
		return rtTopCriticsScore;
	}
	public void setRtTopCriticsScore(String rtTopCriticsScore) {
		this.rtTopCriticsScore = rtTopCriticsScore;
	}
	public String getRtAudienceRating() {
		return rtAudienceRating;
	}
	public void setRtAudienceRating(String rtAudienceRating) {
		this.rtAudienceRating = rtAudienceRating;
	}
	public String getRtAudienceNumRatings() {
		return rtAudienceNumRatings;
	}
	public void setRtAudienceNumRatings(String rtAudienceNumRatings) {
		this.rtAudienceNumRatings = rtAudienceNumRatings;
	}
	public String getRtAudienceScore() {
		return rtAudienceScore;
	}
	public void setRtAudienceScore(String rtAudienceScore) {
		this.rtAudienceScore = rtAudienceScore;
	}
	public String getRtPictureUrl() {
		return rtPictureUrl;
	}
	public void setRtPictureUrl(String rtPictureUrl) {
		this.rtPictureUrl = rtPictureUrl;
	}
	
	
	
	

}
