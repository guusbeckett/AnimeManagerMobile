package animemanagermobile.reupload.nl;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import animemanagermobile.reupload.nl.animefragmens.listadapters.SearchResultAdapter;
import animemanagermobile.reupload.nl.data.AniDBWrapper;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.data.MangaUpdatesClient;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchAMM extends Activity implements OnItemClickListener {
	
	private String query;
	private SearchResultAdapter adapter;
	private int service;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_edit_anime);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      service = intent.getIntExtra("service", 0);
	      query = intent.getStringExtra(SearchManager.QUERY);
	      Toast.makeText(this, query, Toast.LENGTH_LONG).show();
	      ArrayList<MediaObject> listOfAll = new ArrayList<MediaObject>();
	      if (service == 0) {
		      DataManage data = new DataManage();
		      for (int i=1; i<=4; i++) {
		    	  for (MediaObject item : data.getMediaList(this, i)) {
		    		  if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
		    			  item.setType(i);
		    			  listOfAll.add(item);
		    		  }
		    	  }
		      }
		      listOfAll.add(new MediaObject("Search aniDB", -1));
		      listOfAll.add(new MediaObject("Search MangaUpdates", -2));
	      }
	      else if (service == 1) {
	    	  for (String item : AniDBWrapper.getMostLikelyID(query, true)) {
	    		  try {
		    		  String title = item.split("\\^")[0];
		    		  int id = Integer.parseInt(item.split("\\^")[1]);
		    		  if (!title.equals(""))
		    			  listOfAll.add(new MediaObject(title, id));
	    		  } catch (Exception e) {  }
	    	  }
	      }
	      else if (service == 2) {
	    	  for (String item : MangaUpdatesClient.getMostLikelyID(query, true)) {
	    		  try {
	    			  String title = item.split("\\^")[0];
	    			  int id = Integer.parseInt(item.split("\\^")[1]);
	    			  if (!title.equals(""))
	    				  listOfAll.add(new MediaObject(title, id));
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.options_menu_main_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
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
