package edu.asu.tltjr.healthnews;

import java.util.List;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public abstract class Parseable {
	
	protected List<Story> stories;
	protected Source source;
	protected String feed;
	
	protected Parseable(List<Story> stories, String feed) {
		this.stories = stories;
		this.feed = feed;
		registerTags();
	}
	
	abstract List<Story> refreshNews();
	
	protected void elementsToListStory(List<Element> aElements, String site, String siteUrl) {
		for(Element element : aElements) {
			String url = getElementUrl(siteUrl, element.getStartTag().getAttributeValue("href"));
			String title = element.getContent().toString();
			if(!title.contains("img src")) {
				Story temp = new Story(title, site, url);
				stories.add(temp);
			}
		}
	}
	
	protected void getElementsByClass(String className) {
		List<? extends Segment> segments = source.getAllElementsByClass(className);
		source = convertSegmentsListToSource(segments);
	}
	
	private Source convertSegmentsListToSource(List<? extends Segment> segments) {
		String result = null;
		for(Segment segment : segments){
			result += segment.toString();
		}
		return new Source(result);
	}
	
	private String getElementUrl(String siteUrl, String hrefAttributeValue) {
		if (hrefAttributeValue.indexOf(':')==-1) {
			return siteUrl + hrefAttributeValue;
		}
		return hrefAttributeValue;
	}
	
	private void registerTags() {
		MicrosoftTagTypes.register();
		MasonTagTypes.register();
	}
}
