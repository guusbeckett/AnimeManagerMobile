AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.optionfragments;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.AMMDatabase;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.dialogs.ShowPickerDialog;

public class DevOptFragment extends Fragment {

	
	private View v;
	private LinearLayout masll;
	private LinearLayout ll;
	private AMMDatabase ammData;
	private SQLiteDatabase ammDatabase;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings , container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    ammData = new AMMDatabase(this.getActivity());
        Button clearFeeds = new Button(this.getActivity());
        Button clearFeed = new Button(this.getActivity());
        Button clearSubs = new Button(this.getActivity());
        Button clearSub = new Button(this.getActivity());
        Button clearFeedBookmarks = new Button(this.getActivity());
        Button clearFeedBookmark = new Button(this.getActivity());
        final Activity act = this.getActivity();
        clearFeeds.setText("Clear all feeds");
        clearFeeds.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	openDB();
            	ammDatabase.delete("Feeds", null, null);
            	closeDB();
            }
        });
        clearFeed.setText("Clear a feed");
        clearFeed.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	openDB();
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            	Cursor c = ammDatabase.query("Subteams", new String[]{"Name"}, null, null, null, null, null);
            	final String[] items = new String[c.getCount()];
            	int i = 0;
            	while (c.moveToNext()) {
            		if (c.isAfterLast())
            			break;
            		items[i] = c.getString(c.getColumnIndex("Name"));
            		i++;
            	}
            	alert.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openDB();
						ammDatabase.delete("Feeds", "feedname='" + items[which] + "'", null);
						closeDB();
					}
				});
            	alert.show();
            	closeDB();
        	}
        });
        clearSubs.setText("Delete all subteams");
        clearSubs.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	openDB();
            	ammDatabase.delete("Subteams", null, null);
            	closeDB();
        	}
        });
        clearSub.setText("Delete a subteam");
        clearSub.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	openDB();
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            	Cursor c = ammDatabase.query("Subteams", new String[]{"Name"}, null, null, null, null, null);
            	final String[] items = new String[c.getCount()];
            	int i = 0;
            	while (c.moveToNext()) {
            		if (c.isAfterLast())
            			break;
            		items[i] = c.getString(c.getColumnIndex("Name"));
            		i++;
            	}
            	alert.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openDB();
						ammDatabase.delete("Subteams", "Name='" + items[which] + "'", null);
						ammDatabase.delete("Feeds", "feedname='" + items[which] + "'", null);
						closeDB();
					}
				});
            	alert.show();
            	closeDB();
        	}
        });
        clearFeedBookmarks.setText("Clear all feed bookmarks");
        clearFeedBookmarks.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	openDB();
            	Cursor c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL"}, null, null, null, null, null);
            	while (c.moveToNext()) {
            		if (c.isAfterLast())
            			break;
            		ContentValues cv = new ContentValues();
            		cv.put("Name", c.getString(c.getColumnIndex("Name")));
            		cv.put("RSS_URL", c.getString(c.getColumnIndex("RSS_URL")));
            		cv.put("LastPost", "");
            		ammDatabase.delete("Subteams", "Name='" +  c.getString(c.getColumnIndex("Name"))+ "'", null);
            		ammDatabase.insert("Subteams", null, cv);
            	}
            	closeDB();
        	}
        });
        clearFeedBookmark.setText("Clear a feed's bookmark");
        clearFeedBookmark.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	openDB();
            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            	Cursor c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL"}, null, null, null, null, null);
            	final String[] items = new String[c.getCount()];
            	final String[] rss = new String[c.getCount()];
            	int i = 0;
            	while (c.moveToNext()) {
            		if (c.isAfterLast())
            			break;
            		items[i] = c.getString(c.getColumnIndex("Name"));
            		rss[i] = c.getString(c.getColumnIndex("RSS_URL"));
            		i++;
            	}
            	alert.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openDB();
						ContentValues cv = new ContentValues();
	            		cv.put("Name", items[which]);
	            		cv.put("RSS_URL", rss[which]);
	            		cv.put("LastPost", "");
	            		ammDatabase.delete("Subteams", "Name='" + items[which] + "'", null);
	            		ammDatabase.insert("Subteams", null, cv);
						closeDB();
					}
				});
            	alert.show();
            	closeDB();
        	}
        });
        ll.addView(clearFeeds);
        ll.addView(clearFeed);
        ll.addView(clearSubs);
        ll.addView(clearSub);
        ll.addView(clearFeedBookmarks);
        ll.addView(clearFeedBookmark);
        //END OF SUBBERS
        Button rename = new Button(this.getActivity());
        rename.setText("rename folder");
        rename.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File f = new File(act.getFilesDir().getParentFile().getParentFile(), "animemanager.reupload.nl");
				act.getFilesDir().getParentFile().delete();
				f.renameTo(act.getFilesDir().getParentFile());
			}
		});
		ll.addView(rename);
        masll.addView(ll);
        return v;
    }
	
	private void openDB() {
		ammDatabase =  ammData.getWritableDatabase();
	}
	
	private void closeDB() {
		ammDatabase.close();
	}
	
//	private String[] makeArrayNotHaveNull(String[] items) {
//		String[] newList = new String[items.length];
//		int i = 0;
//		for (String item : items) {
//			if (item != null)
//				newList[i] = item;
//			else
//				newList[i] = "";
//			i++;
//		}
//		return newList;
//	}
}
