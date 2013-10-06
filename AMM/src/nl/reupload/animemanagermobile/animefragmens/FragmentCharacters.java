/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.animefragmens;

import java.util.ArrayList;

import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.animefragmens.listadapters.CharacterListAdapter;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentCharacters extends Fragment {

	private MediaObject media;
	private LinearLayout linlay;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.activity_edit_anime , container, false);
		media = (MediaObject) DataManage.getCached();
		String[] metadata = null;
		if (DataManage.isCached2())
			metadata = (String[]) DataManage.getCached2();
		RelativeLayout view = (RelativeLayout) v.findViewById(R.id.anime_relative);
        linlay = new LinearLayout(this.getActivity());
        view.addView(linlay);
        linlay.setOrientation(LinearLayout.VERTICAL);
        
        
		if (metadata != null) {
			if (metadata[14] != null) {
	//			String[][] chara = parseCharacters(metadata[14]);
				ListView lv = new ListView(this.getActivity());
		        final CharacterListAdapter adapt = new CharacterListAdapter(this.getActivity(), parseCharacters(metadata[14]));
		        lv.setAdapter(adapt);
		        final Activity act = getActivity();
		        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		        lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						AlertDialog.Builder alert = new AlertDialog.Builder(act);
		                LinearLayout ll = new LinearLayout(getActivity());
		        	    ll.setOrientation(LinearLayout.VERTICAL);
		                alert.setView(ll);
		                Bitmap bm = null;
		                if (!((String[])adapt.getItem(arg2))[4].equals("")) {
			            	if (DataManage.doesExternalFileExist("/images/character/" + ((String[])adapt.getItem(arg2))[4], act)) {
			            		bm = DataManage.loadImageFromExternal("character/" + ((String[])adapt.getItem(arg2))[4], act);
			            		
			                }
			            	else {
			            		if (DataManage.isNetworkAvailable(act)) {
				                	AniDBWrapper.fetchImage(((String[])adapt.getItem(arg2))[4], act, "character");
				                	bm = DataManage.loadImageFromExternal("character/"+((String[])adapt.getItem(arg2))[4], act);
			            		}
			                	//Log.d("hai", "FnF");
			            	}
		        		}
		                TextView title = new TextView(act);
		                TextView rest = new TextView(act);
		                rest.setText("Type: " + ((String[])adapt.getItem(arg2))[0] 
							+ "\nGender: " + ((String[])adapt.getItem(arg2))[2]
							+ "\nSeiyuu: " + ((String[])adapt.getItem(arg2))[5]);
		                title.setGravity(Gravity.CENTER);
		            	title.setText(((String[])adapt.getItem(arg2))[1]);
		            	title.setTypeface(null, Typeface.BOLD);
		            	ll.addView(title);
		            	ll.addView(rest);
		            	ImageView img = new ImageView(act);
		            	img.setMinimumWidth(linlay.getWidth());
		            	img.setImageBitmap(bm);
		            	
		            	ll.addView(img);
	                    alert.show();
						// TODO Auto-generated method stub
	//					Intent intent = new Intent(activ, MangaView.class);
	//					intent.putExtra("title", media.getTitle());
	//					intent.putExtra("chapter", arg2+1);
	//					startActivity(intent);
	//					Log.d("start", media.getTitle());
						
					}
				});
		        Log.d("ne", "e");
		        view.addView(lv);
		        Log.d("ne", "e");
	//				TextView tv = new TextView(this.getActivity());
	//				tv.setText("\n\nName: " + charl[1]
	//						+ "\nType: " + charl[0] 
	//						+ "\nGender: " + charl[2]
	//						+ "\nCharacter type: " + charl[3] 
	//						+ "\nPicture: " + charl[4]
	//						+ "\nSeiyuu: " + charl[5]);
	//				linlay.addView(tv);
			}
		}
		
		
        
        else {
        	TextView chars = new TextView(this.getActivity());
            chars.setText("No Metadata for " + media.getTitle());
            chars.setTypeface(null, Typeface.ITALIC);
        	chars.setTextSize(8);
        	linlay.addView(chars);
        }
        
        
        return v;
    }
	
	public String[][] parseCharacters(String stream) {
		ArrayList<String[]> chars = new ArrayList<String[]>();
		for (String character : stream.split("\\| \\[ id=\"")) {
			String[] chara = new String[6];
			chara[0] = character.split("type=\"")[1].split("\"")[0];
			chara[1] = character.split("name=\"")[1].split("\"")[0];
			chara[2] = character.split("gender=\"")[1].split("\"")[0];
			chara[3] = character.split("charactertype=\"")[1].split("\"")[0];
			chara[4] = character.split("picture=\"")[1].split("\"")[0];
			chara[5] = character.split("seiyuu=\"")[1].split("\"")[0];
			chars.add(chara);
//			if (character.contains("<name>")) {
//				String[] chara = new String[6];
//				chara[0] = character.split("type=\"")[1].split("\"")[0];
//				chara[1] = character.split("<name>")[1].split("</name>")[0];
//				chara[2] = character.split("<gender>")[1].split("</gender>")[0];
//				chara[3] = character.split("<charactertype")[1].split(">")[1].split("</charactertype")[0];
//				if (character.contains("<picture>"))
//					chara[4] = character.split("<picture>")[1].split("</picture>")[0];
//				else
//					chara[4] = "";
//				if (character.contains("<seiyuu"))
//					chara[5] = character.split("<seiyuu")[1].split(">")[1].split("</seiyuu")[0];
//				else
//					chara[5] = "not existent";
//				chars.add(chara);
//			}
		}
		return chars.toArray(new String[0][]);
	}
}
