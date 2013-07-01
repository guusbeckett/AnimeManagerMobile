package animemanagermobile.reupload.nl.animefragmens;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.R;
import animemanagermobile.reupload.nl.animefragmens.listadapters.EpisodeListAdapter;
import animemanagermobile.reupload.nl.data.DataManage;

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
					AlertDialog.Builder alert = new AlertDialog.Builder(act);
	                LinearLayout ll = new LinearLayout(getActivity());
	        	    ll.setOrientation(LinearLayout.VERTICAL);
	                alert.setView(ll);
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
	
	public String[][] parseEps(String stream) {
		//Log.d("nee", stream);
		ArrayList<String[]> eps = new ArrayList<String[]>();
		for (String ep : stream.split("<episode id=\"")) {
			if (ep.contains("<epno")) {
				String[] chara = new String[4];
				chara[0] = ep.split("<epno")[1].split(">")[1].split("</epno")[0];
				if (ep.contains("<length>"))
					chara[1] = ep.split("<length>")[1].split("</length>")[0];
				else
					chara[1] = "Lenght is unknown";
				if (ep.contains("<airdate>"))
					chara[2] = ep.split("<airdate>")[1].split("</airdate>")[0];
				else
					chara[2] = "No airdate is known";
				for (String title : ep.split("<title "))
					chara[3]+="\n"+title.split(">")[1].split("<")[0];
				eps.add(chara);
			}
		}
		return eps.toArray(new String[0][]);
	}
}
