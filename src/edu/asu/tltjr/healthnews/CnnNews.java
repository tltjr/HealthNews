package edu.asu.tltjr.healthnews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class CnnNews extends Parseable {
	
	protected CnnNews(List<Story> stories, String feed) {
		super(stories, feed);
	}

	@Override
	public List<Story> refreshNews() {
		try {
			source = new Source(new URL(feed));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getElementsByClass("cnn_mtt1content");
		getElementsByClass("cnn_bulletbin");
		List<Element> a = source.getAllElements(HTMLElementName.A);
		elementsToListStory(a, "cnn.com", "http://www.cnn.com");
		return stories;
    }
	
}
