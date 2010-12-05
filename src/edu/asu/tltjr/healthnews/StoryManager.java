package edu.asu.tltjr.healthnews;

import java.util.ArrayList;
import java.util.List;

public class StoryManager {

	List<Story> stories;
	String cnnfeed;
	
	public StoryManager(ArrayList<Story> stories, 
			String cnnfeed) {
		this.stories = stories;
		this.cnnfeed = cnnfeed;
	}

	public void SetStories() {
    	CnnNews cnnNews = new CnnNews(stories);
    	cnnNews.refreshNews(cnnfeed);
	}

}
