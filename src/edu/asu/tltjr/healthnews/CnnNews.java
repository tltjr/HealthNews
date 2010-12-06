package edu.asu.tltjr.healthnews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class CnnNews {
	List<Story> stories;
	Source source;
	
	public CnnNews(List<Story> stories) {
		this.stories = stories;
		registerTags();
	}
	
	public List<Story> refreshNews(String cnnFeed) {
		try {
			source = new Source(new URL(cnnFeed));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getElementsByClass("cnn_mtt1content");
		getElementsByClass("cnn_bulletbin");
		List<Element> a = source.getAllElements(HTMLElementName.A);
		elementsToListStory(a);
		return stories;
    }
	
	private String getElementUrl(String hrefAttributeValue) {
		if (hrefAttributeValue.indexOf(':')==-1) {
			return "http://www.cnn.com" + hrefAttributeValue;
		}
		return hrefAttributeValue;
	}
	
	private void elementsToListStory(List<Element> aElements) {
		for(Element element : aElements) {
			String url = getElementUrl(element.getStartTag().getAttributeValue("href"));
			String title = element.getContent().toString();
			if(!title.contains("img src")) {
				Story temp = new Story(title, "cnn.com", url);
				stories.add(temp);
			}
		}
	}
	
	private Source convertSegmentsListToSource(List<? extends Segment> segments) {
		String result = null;
		for(Segment segment : segments){
			result += segment.toString();
		}
		return new Source(result);
	}
	
	private void getElementsByClass(String className) {
		List<? extends Segment> segments = source.getAllElementsByClass(className);
		source = convertSegmentsListToSource(segments);
	}
	
	private void registerTags() {
		MicrosoftTagTypes.register();
		MasonTagTypes.register();
	}
}
