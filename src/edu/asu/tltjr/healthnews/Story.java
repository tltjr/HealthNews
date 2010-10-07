package edu.asu.tltjr.healthnews;

public class Story {
	String title;
	String site;
	String url;
	
	public Story(String title, String site, String url){
		this.title = title;
		this.site = site;
		this.url = url;
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
}
