package edu.asu.tltjr.healthnews;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class StoryView extends Activity {
	WebView mWebView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        setContentView(mWebView);
    }
}
