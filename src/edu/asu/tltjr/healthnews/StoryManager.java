package edu.asu.tltjr.healthnews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class StoryManager {

	private List<Story> stories;
	private File file;
	
	public StoryManager(ArrayList<Story> stories, File file) {
		this.stories = stories;
		this.file = file;
	}

	public void SetStories() {
		List<Parseable> allSites = new ArrayList<Parseable>();
		allSites.add(new CnnNews(stories, "http://www.cnn.com/HEALTH/"));
		//allSites.add(new NewYorkTimes(stories));
		for(Parseable site : allSites) {
			stories = site.refreshNews();
		}
    	sort();
	}

	private void sort() {
		scoreStories();
		Collections.sort(stories);
	}

	private void scoreStories() {
		for(Story story : stories) {
			scoreStory(story);
		}
	}

	private void scoreStory(Story story) {
		try {
			IndexSearcher searcher = new IndexSearcher(FSDirectory.open(file));
			QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));
			Query query = parser.parse(story.title);
			TopDocs hits = searcher.search(query, 10);
			story.setScore(sumScores(hits.scoreDocs));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private float sumScores(ScoreDoc[] docs) {
		float result = (float) 0.0;
		for(ScoreDoc doc : docs) {
			result += doc.score;
		}
		return result;
	}

}
