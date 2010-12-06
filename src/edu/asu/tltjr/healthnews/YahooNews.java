package edu.asu.tltjr.healthnews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class YahooNews extends Parseable {

	protected YahooNews(List<Story> stories, String feed) {
		super(stories, feed);
	}

	@Override
	List<Story> refreshNews() {
		try {
			source = new Source(new URL(feed));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Element> segments = source.getAllElementsByClass("hd story");
		List<Element> segments2 = source.getAllElementsByClass("bd story");
		segments.addAll(segments2);
		List<Element> a = convertSegmentsListToSource(segments).getAllElements(HTMLElementName.A);
		elementsToListStory(a, "news.yahoo.com", "http://news.yahoo.com");
		return stories;
	}
}
