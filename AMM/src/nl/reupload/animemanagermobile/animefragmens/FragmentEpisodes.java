/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.animefragmens;

import java.util.ArrayList;

import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.animefragmens.listadapters.EpisodeListAdapter;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.VideoFetcher;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentEpisodes extends Fragment {

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
        TextView eps = new TextView(this.getActivity());
        if (metadata != null) {
			//String[][] cars = parseEps(metadata[15]); 
			ListView lv = new ListView(this.getActivity());
	        final EpisodeListAdapter adapt = new EpisodeListAdapter(this.getActivity(), parseEps(metadata[15]));
	        lv.setAdapter(adapt);
	        final Activity act = getActivity();
	        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					if (((String[]) adapt.getItem(arg2))[0].startsWith("C")) {
						String extra = "";
						for (String item : ((String[]) adapt.getItem(arg2))[3].split("\n")) {
							Log.d("lel", item);
							if (item!=null) {
								if (item!=""&&item!="null"&&isNotOnlySpaces(item)) {
									extra = item;
									break;
								}
							}
						}
//						QGramsDistance metric = new QGramsDistance();
//				      	float high = 0;
//				      	String win = "";
				      	String compiled = media.getTitle() + " " + extra;
//				      	for (String item : VideoFetcher.getVideoURLFromYoutube(compiled, true)) {
//				      		float sim = metric.getSimilarity(compiled, item.split("\\^")[1]);
//				      		if (sim > high) {
//				      			high = sim;
//				      			win = item.split("\\^")[0];This part is due for removal. this algorithm, should not be used on youtube's (google's) search system
//				      		}
//				      		Log.d("sim", "simmilarity between " + compiled + " and " + item.split("\\^")[1] + " is " + sim);
//				      	}
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(VideoFetcher.getVideoURLFromYoutube(compiled, true)[0]));
						startActivity(browserIntent);
					}
//					AlertDialog.Builder alert = new AlertDialog.Builder(act);
//	                LinearLayout ll = new LinearLayout(getActivity());
//	        	    ll.setOrientation(LinearLayout.VERTICAL);
//	                alert.setView(ll);
//	                TextView title = new TextView(act);
//	                TextView rest = new TextView(act);
//	                rest.setText("Type: " + ((String[])adapt.getItem(arg2))[0] 
//						+ "\nGender: " + ((String[])adapt.getItem(arg2))[2]
//						+ "\nSeiyuu: " + ((String[])adapt.getItem(arg2))[5]);
//	                title.setGravity(Gravity.CENTER);
//	            	title.setText(((String[])adapt.getItem(arg2))[1]);
//	            	title.setTypeface(null, Typeface.BOLD);
//	            	ll.addView(title);
//	            	ll.addView(rest);
//                    alert.show();
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
//			for (String[] charl : cars) {
//				TextView tv = new TextView(this.getActivity());
//				tv.setText("\nEpisode number: " + charl[0]
//						+ ":\nLength: " + charl[1] + " minutes"
//						+ "\nAirdate: " + charl[2]
//						+ "\nTitles: " + charl[3] + "\n");
//				linlay.addView(tv);
//			}
		}
        else {
            eps.setText("No Metadata for " + media.getTitle());
            eps.setTypeface(null, Typeface.ITALIC);
        	eps.setTextSize(8);
        }
        linlay.addView(eps);
        
        return v;
    }
	
	public boolean isNotOnlySpaces(String string) {
		for (char c : string.toCharArray()) {
			if (!(c==32))
				return true;
		}
		return false;
	}
	
	public String[][] parseEps(String stream) {
		//Log.d("nee", stream);
		ArrayList<String[]> eps = new ArrayList<String[]>();
		for (String ep : stream.split("\\| \\[ id=\"")) {
			String[] epContent = new String[4];
			epContent[0] = ep.split("epno=\"")[1].split("\"")[0];
			epContent[1] = ep.split("length=\"")[1].split("\"")[0];
			epContent[2] = ep.split("airdate=\"")[1].split("\"")[0];
			epContent[3] = ep.split("\" \\] ")[1];
			eps.add(epContent);
//			if (ep.contains("<epno")) {
//				String[] chara = new String[4];
//				chara[0] = ep.split("<epno")[1].split(">")[1].split("</epno")[0];
//				if (ep.contains("<length>"))
//					chara[1] = ep.split("<length>")[1].split("</length>")[0];
//				else
//					chara[1] = "Lenght is unknown";
//				if (ep.contains("<airdate>"))
//					chara[2] = ep.split("<airdate>")[1].split("</airdate>")[0];
//				else
//					chara[2] = "No airdate is known";
//				boolean first = true;
//				for (String title : ep.split("<title ")) {
//					if (!title.contains("null")) {
//						if (!title.equals("")) {
//							String titleCandidate = title.split(">")[1].split("<")[0];
//							if (!titleCandidate.equals("\n")) {
//								if (first) {
//									chara[3]=titleCandidate;
//									first = false;
//								}
//								else
//									chara[3]+="\n"+titleCandidate;
//							}
//						}
//					}
//				}
//				eps.add(chara);
//			}
		}
		return eps.toArray(new String[0][]);
	}
}
