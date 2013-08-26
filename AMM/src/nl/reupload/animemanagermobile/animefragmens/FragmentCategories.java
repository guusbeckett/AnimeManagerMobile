package nl.reupload.animemanagermobile.animefragmens;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.DataManage;

public class FragmentCategories extends Fragment {

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
        
        TextView cats = new TextView(this.getActivity());
		if (metadata != null) {
			String[][] cars = parseCats(metadata[11]); 
			for (String[] charl : cars) {
				TextView tv = new TextView(this.getActivity());
				tv.setText("\n" + charl[0]
						+ ":\nDescription: " + charl[1] + "\n\n");
				linlay.addView(tv);
			}
		}
        else {
            cats.setText("No Metadata for " + media.getTitle());
            cats.setTypeface(null, Typeface.ITALIC);
        	cats.setTextSize(8);
        	linlay.addView(cats);
        }
        
        
        return v;
    }
	
	public String[][] parseCats(String stream) {
		ArrayList<String[]> cats = new ArrayList<String[]>();
		for (String cat : stream.split("\\| \\[ id=\"")) {
//			cat = "[ id=\"" +cat;
			String[] chara = new String[2];
			chara[0] = cat.split("title=\"")[1].split("\"")[0];
			chara[1] = cat.split("\" \\] ")[1];
			cats.add(chara);
		}
		return cats.toArray(new String[0][]);
	}
}
