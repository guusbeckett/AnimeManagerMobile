package animemanagermobile.reupload.nl;


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
import animemanagermobile.reupload.nl.data.DataManage;

public class ViewList extends Activity implements OnItemClickListener {

	private DataManage data;
	private MediaObject[] lel = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_anime);
        int typeList = this.getIntent().getIntExtra("type", 0);
        data = new DataManage();
        switch (typeList) {
        	case (1):
        		lel = data.getWatchingAnime(this);
        		break;
        	case (2):
        		lel = data.getSeenAnime(this);
        		break;
        	case (3):
        		lel = data.getReadingManga(this);
        		break;
        	case (4):
        		lel = data.getReadManga(this);
        		break;
        }
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.anime_relative);
        if (lel == null) {
        	Log.d("SEVERE", "getAnime returned NULL");
        	TextView tv = new TextView(this);
        	tv.setText("No items found");
        }
        else {
        	if (lel.length > 1) {
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
	        case R.id.menu_add:
	        	Intent intent2 = new Intent(this, EditMediaObject.class);
	        	startActivity(intent2);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), MediaPage.class);
		intent.putExtra("type", this.getIntent().getIntExtra("type", 0));
		intent.putExtra("point", arg2);
		startActivity(intent);
	}

}