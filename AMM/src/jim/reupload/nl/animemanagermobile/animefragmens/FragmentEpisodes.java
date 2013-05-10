package jim.reupload.nl.animemanagermobile.animefragmens;

import java.util.ArrayList;

import jim.reupload.nl.animemanagermobile.DataManage;
import jim.reupload.nl.animemanagermobile.MediaObject;
import jim.reupload.nl.animemanagermobile.R;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentEpisodes extends Fragment {

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
        
        TextView eps = new TextView(this.getActivity());
        if (metadata != null) {
			String[][] cars = parseEps(metadata[15]); 
			for (String[] charl : cars) {
				TextView tv = new TextView(this.getActivity());
				tv.setText("\nEpisode number: " + charl[0]
						+ ":\nLength: " + charl[1] + " minutes"
						+ "\nAirdate: " + charl[2]
						+ "\nTitles: " + charl[3] + "\n");
				linlay.addView(tv);
			}
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
		ArrayList<String[]> eps = new ArrayList<String[]>();
		for (String ep : stream.split("<episode id=\"")) {
			if (ep.contains("<name>")) {
				String[] chara = new String[4];
				Log.d("nee", ep);
				Log.d("wow", "1");
				chara[0] = ep.split("<epno")[1].split(">")[1].split("</epno>")[0];
				chara[1] = ep.split("<length>")[1].split("</length>")[0];
				chara[2] = ep.split("<airdate>")[1].split("</airdate>")[0];
				for (String title : ep.split("<title "))
					chara[3]+="\n"+title.split(">")[1].split("<")[0];
				eps.add(chara);
			}
		}
		return eps.toArray(new String[0][]);
	}
}