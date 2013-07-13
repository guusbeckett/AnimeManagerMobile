package nl.reupload.animemanagermobile;

import java.util.ArrayList;
import java.util.Locale;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.animefragmens.listadapters.SearchResultAdapter;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MangaUpdatesClient;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchAMM extends Activity implements OnItemClickListener {
	
	private String query;
	private SearchResultAdapter adapter;
	private int service;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_edit_anime);

	    ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      service = intent.getIntExtra("service", 0);
	      query = intent.getStringExtra(SearchManager.QUERY);
	      Toast.makeText(this, query, Toast.LENGTH_LONG).show();
	      ArrayList<MediaObject> listOfAll = new ArrayList<MediaObject>();
	      if (service == 0) {
		      DataManage data = new DataManage();
		      for (int i=1; i<=6; i++) {
		    	  for (MediaObject item : data.getMediaList(this, i)) {
		    		  if (item.getTitle().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US))) {
		    			  item.setType(i);
		    			  listOfAll.add(item);
		    		  }
		    	  }
		      }
		      listOfAll.add(new MediaObject(-1 ,"Search aniDB", -1));
		      listOfAll.add(new MediaObject(-2, "Search MangaUpdates", -2));
	      }
	      else if (service == 1) {
	    	  for (String item : AniDBWrapper.getMostLikelyID(query, true)) {
	    		  try {
		    		  String title = item.split("\\^")[0];
		    		  int id = Integer.parseInt(item.split("\\^")[1]);
		    		  if (!title.equals(""))
		    			  listOfAll.add(new MediaObject(-1, title, id));
	    		  } catch (Exception e) {  }
	    	  }
	      }
	      else if (service == 2) {
	    	  for (String item : MangaUpdatesClient.getMostLikelyID(query, true)) {
	    		  try {
	    			  String title = item.split("\\^")[0];
	    			  int id = Integer.parseInt(item.split("\\^")[1]);
	    			  if (!title.equals(""))
	    				  listOfAll.add(new MediaObject(-2, title, id));
	    		  } catch (Exception e) {  }
	    	  }
	      }
	      RelativeLayout rl = (RelativeLayout) findViewById(R.id.anime_relative);
	      if (listOfAll.size() == 0) {
	    	  TextView tv = new TextView(this);
	    	  tv.setText("No items found");
	    	  rl.addView(tv);
	      }
	      else {
	    	  adapter = new SearchResultAdapter(this, listOfAll.toArray(new MediaObject[0]));
	    	  ListView listView = new ListView(this);
	    	  listView.setAdapter(adapter);
	    	  rl.addView(listView);
	    	  listView.setOnItemClickListener(this);
	      }
	    }
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	        	finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_menu_noadd, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MediaObject item = (MediaObject) adapter.getItem(arg2);
		if (item.getId()>0) {
			Intent openTemp = new Intent(this, MediaPage.class);
			openTemp.putExtra("tempMode", true);
			openTemp.putExtra("mediaID", item.getId());
			openTemp.putExtra("type", ((service==1)?2:4));
			openTemp.putExtra("title", item.getTitle());
//			if (!tempMode)
//				openTemp.putExtra("origin", origin);
//			else
//				openTemp.putExtra("origin", act.getIntent().getIntExtra("origin", 0));
			startActivity(openTemp);
		}
		else if (item.getId()==-1) {
			Intent aniDBsearch = new Intent(this, SearchAMM.class);
			aniDBsearch.putExtra(SearchManager.QUERY, query);
			aniDBsearch.putExtra("service", 1);
			aniDBsearch.setAction(Intent.ACTION_SEARCH);
			startActivity(aniDBsearch);
		}
		else if (item.getId()==-2) {
			Intent mangaUpdatessearch = new Intent(this, SearchAMM.class);
			mangaUpdatessearch.putExtra(SearchManager.QUERY, query);
			mangaUpdatessearch.putExtra("service", 2);
			mangaUpdatessearch.setAction(Intent.ACTION_SEARCH);
			startActivity(mangaUpdatessearch);
		}
		else {
			Intent openExisting = new Intent(this, MediaPage.class);
			openExisting.putExtra("standalone", true);
			openExisting.putExtra("list", item.getType());
			openExisting.putExtra("title", item.getTitle());
			startActivity(openExisting);
		}
	}
}
