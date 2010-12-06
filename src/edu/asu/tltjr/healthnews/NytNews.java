package edu.asu.tltjr.healthnews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class NytNews extends Parseable {

	protected NytNews(List<Story> stories, String feed) {
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
		getElementsByClass("wideA");
		getElementsByClass("ledeStory");
		List<Element> a = source.getAllElements(HTMLElementName.A);
		elementsToListStory(a, "nytimes.com", "http://www.nytimes.com");
		return stories;
	}

}
