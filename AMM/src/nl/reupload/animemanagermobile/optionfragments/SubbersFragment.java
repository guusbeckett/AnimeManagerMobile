/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.optionfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.AMMDatabase;

public class SubbersFragment extends Fragment {

	
	private LinearLayout masll;
	private LinearLayout ll;
	private ArrayAdapter<String> adapter;
	private SQLiteDatabase ammDatabase;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		SQLiteOpenHelper ammData = new AMMDatabase(this.getActivity());
		ammDatabase =  ammData.getWritableDatabase();
		View v = inflater.inflate(R.layout.fragment_settings , container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    Button destroyMeta = new Button(this.getActivity());
        final Activity act = this.getActivity();
        destroyMeta.setText("Add a fansubteam");
        destroyMeta.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(act);
            	alert.setTitle("Add a fansubber: ");

                LinearLayout ll = new LinearLayout(getActivity());
        	    ll.setOrientation(LinearLayout.VERTICAL);
        	    final EditText et1 = new EditText(getActivity());
        	    et1.setHint("Fansubteam name");
        	    final EditText et2 = new EditText(getActivity());
        	    et2.setHint("Fansubteam RSS feed URL");
        	    ll.addView(et1);
        	    ll.addView(et2);
                alert.setView(ll);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                    	ContentValues cv = new ContentValues();
                    	cv.put("Name", et1.getText().toString());
                    	cv.put("RSS_URL", et2.getText().toString());
                    	cv.put("LastPost", "0");
                      ammDatabase.insert("Subteams", null, cv);
                      
                      }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel.
                      }
                    });
                    alert.show();
            }
        });
        ll.addView(destroyMeta);
	    Cursor c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL"}, null, null, null, null, null);
	    Log.d("lle", "idongetit");
	    if (c.getCount() >= 1) {
	    	Log.d("c", c.getCount()+"");
		    String[] titles = new String[c.getCount()];
		    //c.moveToFirst();
//		    for (int i=0;i<c.getColumnCount();i++) {
//		    	c.moveToNext();
//		    	titles[i] = c.getString(1);
//		    }
		    int i =0;
		    while (c.moveToNext())
		    {
		    	titles[i] = c.getString(c.getColumnIndex("Name"));
		    	i++;
		    }
		    for (String item : titles) {
		    	Log.d("dsa", item);
		    }
		    Log.d("no", "it does");
		    	
		    adapter = new ArrayAdapter<String>(this.getActivity(), 
	                android.R.layout.simple_list_item_1, titles);
	    	ListView listView = new ListView(this.getActivity());
	    	listView.setAdapter(adapter);
	    	ll.addView(listView);
	    	//listView.setOnItemClickListener((OnItemClickListener) this);
	    }
	    else {
	    	TextView tv = new TextView(this.getActivity());
	    	tv.setText("Nothing here, why don't you try adding some?");
	    }
    	//listView.setOnItemSelectedListener(this);
    	
	    
	    masll.addView(ll);
        return v;
    }
	
	public void drawList () {
		Cursor c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL"}, null, null, null, null, null);
		    String[] titles = new String[c.getCount()];
		    //c.moveToFirst();
//		    for (int i=0;i<c.getColumnCount();i++) {
//		    	c.moveToNext();
//		    	titles[i] = c.getString(1);
//		    }
		    int i =0;
		    while (c.moveToNext())
		    {
		    	titles[i] = c.getString(c.getColumnIndex("Name"));
		    	i++;
		    }
		    for (int o=0; o<titles.length;o++) {
		    	if (titles[o] == null)
		    		titles[o] = "null";
		    }
		    adapter = new ArrayAdapter<String>(this.getActivity(), 
	                android.R.layout.simple_list_item_1, titles);
	    	ListView listView = new ListView(this.getActivity());
	    	listView.setAdapter(adapter);
	    	//listView.setOnItemClickListener((OnItemClickListener) this);
		adapter = new ArrayAdapter<String>(this.getActivity(), 
                android.R.layout.simple_list_item_1, titles);
	}
}
