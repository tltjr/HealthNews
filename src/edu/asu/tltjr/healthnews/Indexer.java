package edu.asu.tltjr.healthnews;

import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import android.content.Context;

public class Indexer {

	private Context applicationContext;
	private IndexWriter indexWriter;
	
	public Indexer(Context applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void index(String url) {
		try {
			Directory dir = FSDirectory.open(applicationContext.getFilesDir());
			indexWriter = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30), 
											true, IndexWriter.MaxFieldLength.UNLIMITED);
			Document doc = new Document();
			doc.add(new Field("contents", url, Field.Store.YES, Field.Index.ANALYZED)); 
			indexWriter.addDocument(doc);
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
