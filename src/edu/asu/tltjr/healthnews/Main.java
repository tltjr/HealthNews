package edu.asu.tltjr.healthnews;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {
	static final String PREFS_NAME = "user";
	
	static final private int MENU_UPDATE = Menu.FIRST;
	static final private int MENU_LOGIN = 2;
	static final private int MENU_LOGOUT = 3;
	static final private int MENU_PREFERENCES = 4;
	
	private static final int LIST_MENU_GROUP = 10;
	private static final int LIST_NEWS_ID = 11;
	private static final int LIST_BEST_ID = 12;
	private static final int LIST_ACTIVE_ID = 13;
	private static final int LIST_NOOB_ID = 14;
	
	static final private int CONTEXT_USER_SUBMISSIONS = 2;
	static final private int CONTEXT_COMMENTS = 3;
	static final private int CONTEXT_USER_LINK = 4;
	static final private int CONTEXT_GOOGLE_MOBILE = 6;
	
	static final private int NOTIFY_DATASET_CHANGED = 1;
	static final private int LOGIN_FAILED = 2;
	static final private int LOGIN_SUCCESSFULL = 3;
	
	static int DEFAULT_ACTION_PREFERENCES = 0;
	
	String loginUrl;
	
	ProgressDialog dialog;
	
	ListView newsListView;
	
	StoryAdapter storyAdapter;
	
	ArrayList<Story> stories = new ArrayList<Story>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

    	newsListView = (ListView)this.findViewById(R.id.hnListView);
    	registerForContextMenu(newsListView);
    	int layoutID = R.layout.simple_list_item_1;
    	storyAdapter = new StoryAdapter(this, layoutID , stories);
    	newsListView.setAdapter(storyAdapter);
    	newsListView.setOnItemClickListener(clickListener);
    	dialog = ProgressDialog.show(Main.this, "", "Loading. Please wait...", true);
    	new Thread(new Runnable(){
    		public void run() {
    			refreshNews();
    			dialog.dismiss();
    			handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
    		}
    	}).start();
    }

    Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		switch(msg.what){
    		case NOTIFY_DATASET_CHANGED:
    			storyAdapter.notifyDataSetChanged();
    			break;
    		case LOGIN_FAILED:
    			Toast.makeText(Main.this, "Login failed :(", Toast.LENGTH_LONG).show();
    			break;
    		case LOGIN_SUCCESSFULL:
    			Toast.makeText(Main.this, "Successful login :)", Toast.LENGTH_LONG).show();
    			dialog = ProgressDialog.show(Main.this, "", "Reloading. Please wait...", true);
    	    	new Thread(new Runnable(){
    	    		public void run() {
    	    			refreshNews();
    	    			dialog.dismiss();
    	    			handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
    	    		}
    	    	}).start();
    			break;
    		default:
    			break;
    		}
    	}
    };
    
    OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> newsAV, View view, int pos, long id) {
			final Story item = (Story) newsAV.getAdapter().getItem(pos);
			if (pos < 30) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				String ListPreference = prefs.getString("PREF_DEFAULT_ACTION", "view-comments");
				if (ListPreference.equalsIgnoreCase("open-in-browser")) {
					Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse((String) item.getUrl()));
					startActivity(viewIntent);
				} 
//				else if (ListPreference.equalsIgnoreCase("view-comments")) {
//					Intent intent = new Intent(Main.this, Comments.class);
//					intent.putExtra("url", item.getCommentsUrl());
//					intent.putExtra("title", item.getTitle());
//					startActivity(intent); } 
				else if (ListPreference.equalsIgnoreCase("mobile-adapted-view")) {
					Intent viewIntent = new Intent("android.intent.action.VIEW",
							Uri.parse((String) "http://www.google.com/gwt/x?u=" + item.getUrl()));
					startActivity(viewIntent);
				}
			} else {
				dialog = ProgressDialog.show(Main.this, "", "Loading. Please wait...", true);
    	    	new Thread(new Runnable(){
    	    		public void run() {
    	    			refreshNews();
    	    			dialog.dismiss();
    	    			handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
    	    		}
    	    	}).start();
			}
		}
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	MenuItem itemRefresh = menu.add(0, MENU_UPDATE, Menu.NONE, R.string.menu_refresh);
    	itemRefresh.setIcon(R.drawable.ic_menu_refresh);
    	itemRefresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {
    		public boolean onMenuItemClick(MenuItem item) {
    			try {
    				dialog = ProgressDialog.show(Main.this, "", "Reloading. Please wait...", true);
    				new Thread(new Runnable(){
    					public void run() {
    						refreshNews();
    						dialog.dismiss();
    						handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
    					}
    				}).start();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			return true;
    		}
    	});
    	
    	SubMenu subMenu = menu.addSubMenu(R.string.menu_lists);
    	subMenu.add(LIST_MENU_GROUP, LIST_NEWS_ID, 0, "news");
    	subMenu.add(LIST_MENU_GROUP, LIST_BEST_ID, 1, "best");
    	subMenu.add(LIST_MENU_GROUP, LIST_ACTIVE_ID, 2, "active");
    	subMenu.add(LIST_MENU_GROUP, LIST_NOOB_ID, 3, "noobstories");
    	subMenu.setIcon(R.drawable.ic_menu_friendslist);
    	subMenu.setGroupCheckable(LIST_MENU_GROUP, true, true);

    	MenuItem itemLogout = menu.add(0, MENU_LOGOUT, Menu.NONE, R.string.menu_logout);
    	itemLogout.setIcon(R.drawable.ic_menu_logout);
    	itemLogout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
    		public boolean onMenuItemClick(MenuItem item) {
    			try {
    				dialog = ProgressDialog.show(Main.this, "", "Reloading. Please wait...", true);
    				new Thread(new Runnable(){
    					public void run() {
    						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    						SharedPreferences.Editor editor = settings.edit();
    						editor.remove("cookie");
    						editor.commit();
    						refreshNews();
    						dialog.dismiss();
    						handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
    					}
    				}).start();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			return true;
    		}
    	});
    	
    	MenuItem itemPreferences = menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
    	itemPreferences.setIcon(android.R.drawable.ic_menu_preferences);
    	itemPreferences.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(Main.this, Preferences.class);
				startActivity(intent);
				
				return true;
			}
		});

    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

    	if (loginUrl.contains("submit")) {
    		menu.findItem(MENU_LOGIN).setVisible(false);
    		menu.findItem(MENU_LOGIN).setEnabled(false);
    		menu.findItem(MENU_LOGOUT).setVisible(true);
    		menu.findItem(MENU_LOGOUT).setEnabled(true);
    	} else {
    		menu.findItem(MENU_LOGIN).setVisible(true);
    		menu.findItem(MENU_LOGIN).setEnabled(true);
    		menu.findItem(MENU_LOGOUT).setVisible(false);
    		menu.findItem(MENU_LOGOUT).setEnabled(false);
    	}
    	
    	return super.onPrepareOptionsMenu(menu); 
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
    	switch (item.getItemId()) {
    	case LIST_ACTIVE_ID:
    	case LIST_BEST_ID:
    	case LIST_NOOB_ID:
    	case LIST_NEWS_ID:
    		try {
				dialog = ProgressDialog.show(Main.this, "", "Reloading. Please wait...", true);
				new Thread(new Runnable(){
					public void run() {
						refreshNews();
						dialog.dismiss();
						handler.sendEmptyMessage(NOTIFY_DATASET_CHANGED);
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return true;
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
    	if (info.position < 30) {
    		final Story newsContexted = (Story) newsListView.getAdapter().getItem(info.position);

    		menu.setHeaderTitle(newsContexted.getTitle());

    		MenuItem originalLink = menu.add(0, CONTEXT_USER_LINK, 0, newsContexted.getUrl()); 
    		originalLink.setOnMenuItemClickListener(new OnMenuItemClickListener() {		
    			public boolean onMenuItemClick(MenuItem item) {
    				Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse((String) item.getTitle()));
    				startActivity(viewIntent);
    				return true;
    			}
    		});

    		MenuItem googleMobileLink = menu.add(0, CONTEXT_GOOGLE_MOBILE, 0, R.string.context_google_mobile);
    		googleMobileLink.setOnMenuItemClickListener(new OnMenuItemClickListener() {		
    			public boolean onMenuItemClick(MenuItem item) {
    				Intent viewIntent = new Intent("android.intent.action.VIEW",
    						Uri.parse((String) "http://www.google.com/gwt/x?u=" + newsContexted.getUrl()));
    				startActivity(viewIntent);
    				return true;
    			}
    		});

    	}
    }
    
    private void refreshNews() {
    	CnnNews cnnNews = new CnnNews(stories, getSharedPreferences(PREFS_NAME, 0));
    	String cnnFeed = getString(R.string.cnnfeed);
    	cnnNews.refreshNews(cnnFeed);
    }
    
}