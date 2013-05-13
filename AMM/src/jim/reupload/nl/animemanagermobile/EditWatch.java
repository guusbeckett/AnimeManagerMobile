package jim.reupload.nl.animemanagermobile;


import jim.reupload.nl.animemanagermobile.DataManage.skydrive;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditWatch extends Activity implements OnItemClickListener, skydrive {

	private DataManage data;
	private AnimeObject[] lel;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_anime);
        data = new DataManage();
        if (data.isAsync(this)) {
        	data.iniateFS(this);
        }
        else {
        	lel = data.getWatchingAnime(this);
        }
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.anime_relative);
        if (lel == null) {
        	Log.d("SEVERE", "getAnime returned NULL");
        	TextView tv = new TextView(this);
        	tv.setText("No anime found");
        }
        else {
        	Log.d("SUCCESS", "anime succesfully read");
        	ArrayAdapter adapter = new ArrayAdapter<String>(this, 
                    android.R.layout.simple_list_item_1, MediaObject.convertMediaObjectArrayToStringArray(lel));
        	ListView listView = new ListView(this);
        	listView.setAdapter(adapter);
        	rl.addView(listView);
        	//listView.setOnItemSelectedListener(this);
        	listView.setOnItemClickListener(this);
        }
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;

    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainMenu.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


	/*@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		Intent intent = new Intent(getBaseContext(), MediaPage.class);
		Bundle b = new Bundle();
        b.putSerializable("media", lel[pos]);
        intent.putExtras(b);
		startActivity(intent);
		
	}*/
	
	/*protected void onListItemClick(ListView l, View v, int position, long id) {
	    // position is the position in the adapter and id is what adapter.getItemId() returns.
	    // use one of them to get the group id from the data.
		
	}*/
/*
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), MediaPage.class);
		intent.putExtra("point", arg2);
		startActivity(intent);
	}

	@Override
	public void initdone() {
		
		//data.getWatchingAnime(this);
		data.findWatchingFile(this);
		
	}

	@Override
	public void fileReady() {
		
		//lel = data.getWatchingAnime(this);
	}

	@Override
	public void filefound(String fileid) {
		Log.d("ohowo", fileid);
		
	}
	
}
