package jim.reupload.nl.animemanagermobile.animefragmens;

import java.util.ArrayList;

import jim.reupload.nl.animemanagermobile.MediaObject;
import jim.reupload.nl.animemanagermobile.R;
import jim.reupload.nl.animemanagermobile.data.DataManage;
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

public class FragmentTags extends Fragment {

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
        TextView tags = new TextView(this.getActivity());
        if (metadata != null) {
			String[][] cars = parseTags(metadata[13]); 
			for (String[] charl : cars) {
				TextView tv = new TextView(this.getActivity());
				tv.setText("\n" + charl[0]
						+ ":\nCount: " + charl[1]
						+ "\nDescription: " + charl[2] + "\n\n");
				linlay.addView(tv);
			}
		}
        else {
            tags.setText("No Metadata for " + media.getTitle());
            tags.setTypeface(null, Typeface.ITALIC);
        	tags.setTextSize(8);
        }
        linlay.addView(tags);
        return v;
    }
	
	public String[][] parseTags(String stream) {
		ArrayList<String[]> tags = new ArrayList<String[]>();
		for (String tag : stream.split("<tag id=\"")) {
			if (tag.contains("<name>")) {
				String[] chara = new String[3];
				chara[0] = tag.split("<name>")[1].split("</name>")[0];
				chara[1] = tag.split("<count>")[1].split("</count>")[0];
				if (tag.contains("<description>"))
					chara[2] = tag.split("<description>")[1].split("</description>")[0];
				else
					chara[2] = "None available";
				tags.add(chara);
			}
		}
		return tags.toArray(new String[0][]);
	}
}
