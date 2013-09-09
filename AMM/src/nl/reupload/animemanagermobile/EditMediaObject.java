/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.DataManage;

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
