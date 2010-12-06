package edu.asu.tltjr.healthnews;

public class Story implements Comparable<Story> {
	String title;
	String site;
	String url;
	float score;
	
	public Story(String title, String site, String url){
		this.title = title;
		this.site = site;
		this.url = url;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	public float getScore() {
		return score;
	}
	
	public String getTitle() {
		return title;
	}

	public String getSite() {
		return site;
	}
	
	public String getUrl() {
		return url;
	}

	@Override
	public int compareTo(Story another) {
		return (int)(((Story)another).getScore() * 100) - (int)(this.getScore() * 100); 
	}
}
