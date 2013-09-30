/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.optionfragments;

import java.nio.channels.FileLockInterruptionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MetadataDatabase;

public class MetadataFragment extends Fragment {

	
	private View v;
	private LinearLayout masll;
	private LinearLayout ll;

	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings , container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    TextView tv = new TextView(getActivity());
        tv.setText("Please select an option for storage");
        masll.addView(tv);
        Button destroyMeta = new Button(this.getActivity());
        final Activity act = this.getActivity();
        destroyMeta.setText("Delete ALL metadata");
        destroyMeta.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	DataManage.deleteAllMeta(act);
            	
            }
        });
        ll.addView(destroyMeta);
        Switch storageLoc = new Switch(this.getActivity());
        storageLoc.setText("Metadata storage");
        storageLoc.setTextOff("Internal");
        storageLoc.setTextOn("External");
        final SharedPreferences settings = act.getSharedPreferences("AMMprefs", 0);
        storageLoc.setChecked(settings.getBoolean("MetadataExtStorage", true));
        storageLoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("MetadataExtStorage", isChecked);
			    Log.d("heh", MetadataDatabase.getPath(getActivity()));
			    Log.d("heh", "nee");
//			    editor.commit();
			    //TODO move DB
				
				
			}
		});
        ll.addView(storageLoc);
        masll.addView(ll);
        return v;
    }
}
