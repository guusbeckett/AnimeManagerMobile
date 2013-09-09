/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.animefragmens;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.AMMDatabase;
import nl.reupload.animemanagermobile.data.DataManage;

public class FragmentRelease extends Fragment {

	private MediaObject media;
	private LinearLayout linlay;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.anime_fragment_page , container, false);
		media = (MediaObject) DataManage.getCached();
		String[] metadata = null;
		if (DataManage.isCached2())
			metadata = (String[]) DataManage.getCached2();
		ScrollView view = (ScrollView) v.findViewById(R.id.anime_frag);
        linlay = new LinearLayout(this.getActivity());
        view.addView(linlay);
        linlay.setOrientation(LinearLayout.VERTICAL);
        
        /*TextView desc = new TextView(this.getActivity());
        if (metadata != null)
        	desc.setText(metadata[8]);
        else {
            desc.setText("No Metadata for " + media.getTitle());
            desc.setTypeface(null, Typeface.ITALIC);
        	desc.setTextSize(8);
        }*/
        SQLiteOpenHelper ammData = new AMMDatabase(this.getActivity());
		final SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		final Cursor c = ammDatabase.query("Registered", new String[]{"Tracking", "Subber", "Keyword"}, "Name="+ DatabaseUtils.sqlEscapeString(media.getTitle())  +" AND Type='1'", null, null, null, null);
		Cursor c2 = ammDatabase.query("Subteams", new String[]{"Name"}, null, null, null, null, null);
        
    	//ammDatabase.insert("Registered", null, cv);
        final String[] spins = new String[c2.getCount()];
        int i = 0;
        Log.d("ne", "crash");
        while (c2.moveToNext()) {
        	spins[i] = c2.getString(c2.getColumnIndex("Name"));
        	i++;
        }
        final CheckBox cb = new CheckBox(this.getActivity());
        c.moveToFirst();
        Log.d("ne", "crash");
        if (c.getCount() >= 1)
        	cb.setChecked((c.getString(c.getColumnIndex("Tracking")).equals("1")?true:false));
        else
        	cb.setChecked(false);
        cb.setText("Track");
        
        linlay.addView(cb);
        final Spinner spin = new Spinner(this.getActivity());
        //ArrayAdapter<String> adapter = android.widget.ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, spins);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	    spin.setAdapter(adapter);
        //spin
	    
	    cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				ContentValues cv = new ContentValues();
		    	cv.put("Name", media.getTitle());
		    	cv.put("Type", 1);
		    	cv.put("ID", media.getId());
		    	cv.put("Tracking", arg1);
		    	cv.put("Subber", spins[spin.getSelectedItemPosition()]);
		    	cv.put("Keyword", "");
		    	ammDatabase.delete("Registered", "Name="+ DatabaseUtils.sqlEscapeString(media.getTitle()) + " AND Type='1'", null);
				ammDatabase.insert("Registered", null, cv);
			}
		});
	    
	    spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (cb.isChecked()) {
					ContentValues cv = new ContentValues();
			    	cv.put("Name", media.getTitle());
			    	cv.put("Type", 1);
			    	cv.put("ID", media.getId());
			    	cv.put("Tracking", true);
			    	cv.put("Subber", spins[arg2]);
			    	cv.put("Keyword", c.getString(c.getColumnIndex("Keyword")));
			    	ammDatabase.delete("Registered", "Name="+ DatabaseUtils.sqlEscapeString(media.getTitle())  +  " AND Type='1'", null);
					ammDatabase.insert("Registered", null, cv);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    final EditText et = new EditText(this.getActivity());
	    et.setHint(media.getTitle());
	    if (c.getCount() > 0) {
	    	int select = 0;
	    	for (String item : spins) {
	    		if (item.equals(c.getString(c.getColumnIndex("Subber"))))
	    			break;
	    		select++;
	    	}
	    	if (select < spins.length)
	    		spin.setSelection(select);
	    	et.setText(c.getString(c.getColumnIndex("Keyword")));
	    }
	    Button but = new Button(this.getActivity());
	    but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ContentValues cv = new ContentValues();
		    	cv.put("Name", media.getTitle());
		    	cv.put("Type", 1);
		    	cv.put("ID", media.getId());
		    	cv.put("Tracking", c.getInt(c.getColumnIndex("Tracking")));
		    	cv.put("Subber", c.getString(c.getColumnIndex("Subber")));
		    	cv.put("Keyword", et.getText().toString());
		    	ammDatabase.delete("Registered", "Name="+ DatabaseUtils.sqlEscapeString(media.getTitle())  + " AND Type='1'", null);
				ammDatabase.insert("Registered", null, cv);
				
			}
		});
	    linlay.addView(et);
	    linlay.addView(but);
	    linlay.addView(spin);
        
        return v;
    }
}
