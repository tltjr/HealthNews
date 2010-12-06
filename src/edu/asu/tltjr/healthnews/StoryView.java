package edu.asu.tltjr.healthnews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.htmlparser.jericho.Source;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class StoryView extends Activity {
	WebView mWebView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        Indexer indexer = new Indexer(getApplicationContext().getFilesDir());
        String url = extras.getString("url");
        Source source = null;
		try {
			source = new Source(new URL(url));
	        indexer.index(source.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        setContentView(mWebView);
    }
}
