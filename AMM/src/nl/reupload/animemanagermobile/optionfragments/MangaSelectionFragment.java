/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.optionfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MangaSelectionFragment extends Fragment {

	
	private LinearLayout masll;
	private LinearLayout ll;
	private static final int PICKFILE_RESULT_CODE = 1;
	
	public MangaSelectionFragment() {
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
	    Button destroyMeta = new Button(this.getActivity());
		destroyMeta.setText("Find folder");
        destroyMeta.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("resource/folder");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
       
            	
            }
        });
        ll.addView(destroyMeta);
        return v;
    }
}
