package nl.reupload.animemanagermobile;

import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.AMMDatabase;

import org.apache.commons.lang3.StringEscapeUtils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RssItem extends Activity {
	
	private String guid;
	private String feed;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_anime);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.anime_relative);
        feed = getIntent().getStringExtra("feed");
        guid = getIntent().getStringExtra("guid");
        WebView tv = new WebView(this);
//        tv.setText(getRSSItem());
        tv.loadData(getRSSItem(false), "text/html", null);
        rl.addView(tv);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_menu_noadd, menu);
		return true;
    }

	private String getRSSItem(boolean unescape) {
		String item = null;
		SQLiteOpenHelper ammData = new AMMDatabase(this);
		SQLiteDatabase ammDatabase = ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Feeds", new String[]{"Title", "Description", "Link", "Author", "Content", "Read", "_id", "Category", "Comments", "Enclosure", "guid" , "pubDate", "Source", "Important", "feedname"}, "feedname='" + feed + "' AND Title='" + guid + "'", null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			if (c.getInt(c.getColumnIndex("Read"))==0) {
				ContentValues cv = new ContentValues();
				cv.put("Title", c.getString(c.getColumnIndex("Title")));
				cv.put("Description", c.getString(c.getColumnIndex("Description")));
				cv.put("Link", c.getString(c.getColumnIndex("Link")));
				cv.put("Author", c.getString(c.getColumnIndex("Author")));
				cv.put("Read", true);
				cv.put("Category", c.getString(c.getColumnIndex("Category")));
				cv.put("Comments", c.getString(c.getColumnIndex("Comments")));
				cv.put("Enclosure", c.getString(c.getColumnIndex("Enclosure")));
				cv.put("guid", c.getString(c.getColumnIndex("guid")));
				cv.put("pubDate", c.getString(c.getColumnIndex("pubDate")));
				cv.put("Source", c.getString(c.getColumnIndex("Source")));
				cv.put("Important", c.getInt(c.getColumnIndex("Important")));
				cv.put("feedname", c.getInt(c.getColumnIndex("feedname")));
				cv.put("_id", c.getString(c.getColumnIndex("_id")));
				ammDatabase.replace("Feeds", null, cv);
			}
			String content = c.getString(c.getColumnIndex("Content"));
			if (content != "")
				if (unescape)
					item = StringEscapeUtils.unescapeHtml4(content);
				else
					item = content;
		} else {
			item = "";
			Log.d("Rss item", "0 items match");
		}
		ammDatabase.close();
		return item;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		final Activity act = this;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	        	finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
