package animemanagermobile.reupload.nl;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animemanagermobile.reupload.nl.data.DataManage;

public class ViewList extends Activity implements OnItemClickListener {

	private DataManage data;
	private MediaObject[] lel = null;
	private int typeList;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_anime);
        typeList = this.getIntent().getIntExtra("type", 0);
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
        	Log.d("what", "lel");
        	if (lel.length > 0) {
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
		final Activity act = this;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainMenu.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
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
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	MediaObject lel = new MediaObject(et1.getText().toString(), 0,0);
    	            	data.addNewSeries(act, lel, typeList);
    	            	act.recreate();
                      }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel.
                      }
                    });
                    alert.show();
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