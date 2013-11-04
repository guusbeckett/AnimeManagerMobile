/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.optionfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class ViewListSettingFragment extends Fragment {

	
	private LinearLayout masll;
	private LinearLayout ll;
	private SharedPreferences settings;
	
	public ViewListSettingFragment() {
		super();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_settings , container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    Spinner styleSpinner = new Spinner(this.getActivity());
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	            R.array.spinner_storage2, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    settings = getActivity().getSharedPreferences("AMMprefs", 0);
	    int hai = settings.getInt("ViewListStyle", 1);
	    
	    styleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putInt("ViewListStyle", arg2);
			    editor.commit();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    styleSpinner.setAdapter(adapter);
	    styleSpinner.setSelection(hai);
	    
	    TextView tv = new TextView(this.getActivity());
	    tv.setText("Select Style for anime/manga List");
	    ll.addView(tv);
	    ll.addView(styleSpinner);
	    Switch toggleSwype = new Switch(this.getActivity());
	    toggleSwype.setText("Enable sliding actions");
	    toggleSwype.setChecked(settings.getBoolean("ListSwype", false));
	    toggleSwype.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("ListSwype", arg1);
			    editor.commit();
				
			}
		});
	    ll.addView(toggleSwype);
	    Switch toggleCardLook = new Switch(this.getActivity());
	    toggleCardLook.setText("Enable Card look");
	    toggleCardLook.setChecked(settings.getBoolean("ListCards", false));
	    toggleCardLook.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("ListCards", arg1);
			    editor.commit();
				
			}
		});
	    ll.addView(toggleCardLook);
	    masll.addView(ll);
        return v;
    }
}
