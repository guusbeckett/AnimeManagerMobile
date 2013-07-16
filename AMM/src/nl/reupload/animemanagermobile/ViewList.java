package nl.reupload.animemanagermobile;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.asynctasks.MetaDataFetcher;
import nl.reupload.animemanagermobile.data.AMMDatabase;
import nl.reupload.animemanagermobile.data.DataManage;

import org.apache.commons.lang3.StringEscapeUtils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ViewList extends Activity implements OnItemClickListener {

	private DataManage data;
	private MediaObject[] lel = null;
	private int typeList;
	private boolean sort;
	private boolean rssMode;
	private boolean feedMode;
	private String[] list;
	private String feed;
	private ArrayList<String> guids;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_anime);
        typeList = this.getIntent().getIntExtra("type", 0);
        rssMode = this.getIntent().getBooleanExtra("rssMode", false);
        data = new DataManage();
        if (!rssMode)
        	lel = data.getMediaList(this, typeList);
        else
        	feedMode = this.getIntent().getBooleanExtra("feedMode", false);
        if (feedMode) {
        	feed = this.getIntent().getStringExtra("feed");
        	actionBar.setTitle(feed);
        }
        sort = true;
        
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.anime_relative);
        if (lel == null && !rssMode) {
        	Log.d("SEVERE", "getAnime returned NULL");
        	TextView tv = new TextView(this);
        	tv.setText("No items found");
        }
        else if (!rssMode) {
        	Log.d("what", "lel");
        	if (lel.length > 0) {
        		if (sort) {
                	ArrayList<MediaObject> list = new ArrayList<MediaObject>();
                	for (MediaObject item : lel)
                		list.add(item);
                	Collections.sort(list, new MediaObjectTitleComperator());
                	lel = list.toArray(new MediaObject[0]);
                }
        		DataManage.setList(lel);
	        	Log.d("SUCCESS", "media succesfully read");
	        	ArrayAdapter adapter = new ArrayAdapter<String>(this, 
	                    android.R.layout.simple_list_item_1, MediaObject.convertMediaObjectArrayToStringArray(lel));
	        	ListView listView = new ListView(this);
	        	listView.setAdapter(adapter);
	        	rl.addView(listView);
	        	//listView.setOnItemSelectedListener(this);
	        	listView.setOnItemClickListener(this);
        	}
        	else {
	        	Log.d("SEVERE", "getAnime returned empty list");
	        	TextView tv = new TextView(this);
	        	tv.setText("No items found");
        	}
        }
        else {
        	if (!feedMode) {
        		list = getFeeds();
	        	ArrayAdapter adapter = new ArrayAdapter<String>(this, 
	                    android.R.layout.simple_list_item_1, list);
	        	ListView listView = new ListView(this);
	        	listView.setAdapter(adapter);
	        	rl.addView(listView);
	        	listView.setOnItemClickListener(this);
        	}
        	else {
        		list = getFeedHeaders();
        		if (list != null) {
		        	ArrayAdapter adapter = new ArrayAdapter<String>(this, 
		                    android.R.layout.simple_list_item_1, list);
		        	ListView listView = new ListView(this);
		        	listView.setAdapter(adapter);
		        	rl.addView(listView);
		        	listView.setOnItemClickListener(this);
        		}
        	}
        }
        
    }
	
	private String[] getFeedHeaders() {
		ArrayList<String> list = new ArrayList<String>();
		guids = new ArrayList<String>();
		SQLiteOpenHelper ammData = new AMMDatabase(this);
		SQLiteDatabase ammDatabase = ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Feeds", new String[]{"Title", "guid"}, "feedname='" + feed + "'", null, null, null, null);
		while (c.moveToNext()) {
			if (c.isAfterLast())
				break;
			else  {
				list.add(StringEscapeUtils.unescapeHtml4(c.getString(c.getColumnIndex("Title"))));
				guids.add(c.getString(c.getColumnIndex("Title")));
			}
		}
		ammDatabase.close();
		return list.toArray(new String[0]);
	}

	private String[] getFeeds() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Important items");
		SQLiteOpenHelper ammData = new AMMDatabase(this);
		SQLiteDatabase ammDatabase = ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Subteams", new String[]{"Name"}, null, null, null, null, null);
		while (c.moveToNext()) {
			if (c.isAfterLast())
				break;
			else {
				Cursor c2 = ammDatabase.query("Feeds", new String[]{"Read"}, "Read='0'", null, null, null, null);
				list.add(c.getString(c.getColumnIndex("Name")) + ((c2.getCount()>0)?" (" + c2.getCount() + ")":""));
			}
		}
		ammDatabase.close();
		return list.toArray(new String[0]);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		if (!rssMode) {
			getMenuInflater().inflate(R.menu.edit_menu, menu);
			menu.add(0, 5, 0, "RGet missing metadata");
		}
		else
			getMenuInflater().inflate(R.menu.edit_menu_noadd, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Activity act = this;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	        	if (!rssMode) {
		            Intent intent = new Intent(this, MainMenu.class);
		            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(intent);
	        	}
	        	else finish();
	            return true;
	        case R.id.menu_add:
//	        	Intent intent2 = new Intent(this, EditMediaObject.class);
//	        	intent2.putExtra("type", typeList);
//	        	startActivity(intent2);
	        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
            	alert.setTitle("Add a series: ");

                LinearLayout ll = new LinearLayout(this);
        	    ll.setOrientation(LinearLayout.VERTICAL);
        	    final EditText et1 = new EditText(this);
        	    et1.setHint("Series title");
        	    ll.addView(et1);
                alert.setView(ll);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                    	MediaObject item = new MediaObject(et1.getText().toString(), 0,0);
    	            	data.addNewSeries(act, item, typeList, lel);
    	            	act.recreate();
                      }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel.
                      }
                    });
                    alert.show();
                    return true;
	        case (5):
	        	for (MediaObject object : lel) { //I hope this doesn't get me banned
	        		if (!DataManage.isRegistered(object.getTitle(), typeList, this)) {
	        			MetaDataFetcher fetch = new MetaDataFetcher(this, null, object, typeList);
	        			fetch.noClose();
	        			fetch.execute(0);
	        		}
	        	}
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (!rssMode) {
			Intent intent = new Intent(getBaseContext(), MediaPage.class);
			intent.putExtra("type", this.getIntent().getIntExtra("type", 0));
			intent.putExtra("point", arg2);
			startActivity(intent);
		}
		else if (!feedMode) {
			Intent intent = new Intent(getBaseContext(), ViewList.class);
			intent.putExtra("rssMode", true);
			intent.putExtra("feedMode", true);
			if (list[arg2].contains(" ("))
				intent.putExtra("feed", list[arg2].split(" \\(")[0]);
			else
				intent.putExtra("feed", list[arg2]);
			startActivity(intent);
		}
		else {
			Intent intent = new Intent(getBaseContext(), RssItem.class);
			intent.putExtra("guid", guids.get(arg2));
			intent.putExtra("feed", feed);
			startActivity(intent);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onResume() {
		super.onResume();
		if (DataManage.getRefresh()) {
			DataManage.setRefresh(false);
			this.recreate();
		}
	}

}

class MediaObjectTitleComperator implements Comparator<MediaObject>
{
	@Override
	public int compare(MediaObject lhs, MediaObject rhs) {
		return (lhs.getTitle().compareToIgnoreCase(rhs.getTitle()));
//		return 0;
	}
}