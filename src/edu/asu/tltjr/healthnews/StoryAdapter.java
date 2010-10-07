package edu.asu.tltjr.healthnews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.asu.tltjr.healthnews.R;

public class StoryAdapter extends ArrayAdapter<Story> {
	int resource;
	
	public StoryAdapter(Context _context, int _resource, List<Story> _items) {
		super(_context, _resource, _items);
		resource = _resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout storiesView;
		Story item = getItem(position);
		String title = item.getTitle();
		String site = item.getSite();
		
		if (convertView == null) {
			storiesView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, storiesView, true);
		} else {
			storiesView = (LinearLayout) convertView;
		}
		
		TextView titleView = (TextView)storiesView.findViewById(R.id.title);
		TextView siteView = (TextView)storiesView.findViewById(R.id.score);
		titleView.setText(title);
		siteView.setText(site);
		return storiesView;
	}
}
