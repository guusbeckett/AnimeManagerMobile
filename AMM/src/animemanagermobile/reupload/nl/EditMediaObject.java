package animemanagermobile.reupload.nl;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import animemanagermobile.reupload.nl.data.DataManage;

@Deprecated
public class EditMediaObject extends Activity {

	 	private LinearLayout masll;
		private LinearLayout ll;
		private EditText et1;
		private EditText et2;
		private TextView et3;


		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fragment_settings);
	        masll = (LinearLayout) findViewById(R.id.setting_scroll_location);
	        ll = new LinearLayout(this);
		    ll.setOrientation(LinearLayout.VERTICAL);
		    
		    TextView tv1 = new TextView(this);
		    tv1.setText("Title:");
		    TextView tv2 = new TextView(this);
		    tv2.setText("Progress:");
		    TextView tv3 = new TextView(this);
		    tv3.setText("Total episodes:");
//		    TextView tv4 = new TextView(this);
//		    tv4.setText("Title:");
		    et1 = new EditText(this);
		    et2 = new EditText(this);
		    et2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
		    et3 = new EditText(this);
		    et3.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
		    
		    ll.addView(tv1);
		    ll.addView(et1);
		    ll.addView(tv2);
		    ll.addView(et2);
		    ll.addView(tv3);
		    ll.addView(et3);
		    
		    masll.addView(ll);
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.edit_media, menu);
	        return true;

	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	            case R.id.media_add:
	                //newGame();
	            	DataManage data = new DataManage();
	            	MediaObject lel = new MediaObject(et1.getText().toString(), Integer.valueOf(et2.getText().toString()), Integer.valueOf(et3.getText().toString()));
//	            	data.addNewSeries(this, lel, this.getIntent().getIntExtra("type", 0), list);
	            	this.finish();
	                return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	
}
