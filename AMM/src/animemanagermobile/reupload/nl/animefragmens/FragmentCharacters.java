package animemanagermobile.reupload.nl.animefragmens;

import java.util.ArrayList;

import jim.reupload.nl.animemanagermobile.R;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.data.DataManage;

public class FragmentCharacters extends Fragment {

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
        
        
		
		if (metadata != null) {
			String[][] chara;
			chara = parseCharacters(metadata[14]); 
			for (String[] charl : chara) {
				TextView tv = new TextView(this.getActivity());
				tv.setText("\n\nName: " + charl[1]
						+ "\nType: " + charl[0] 
						+ "\nGender: " + charl[2]
						+ "\nCharacter type: " + charl[3] 
						+ "\nPicture: " + charl[4]
						+ "\nSeiyuu: " + charl[5]);
				linlay.addView(tv);
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
		for (String character : stream.split("<character id=\"")) {
			if (character.contains("<name>")) {
				String[] chara = new String[6];
				chara[0] = character.split("type=\"")[1].split("\"")[0];
				chara[1] = character.split("<name>")[1].split("</name>")[0];
				chara[2] = character.split("<gender>")[1].split("</gender>")[0];
				chara[3] = character.split("<charactertype")[1].split(">")[1].split("</charactertype")[0];
				if (character.contains("<picture>"))
					chara[4] = character.split("<picture>")[1].split("</picture>")[0];
				else
					chara[4] = "not existent";
				if (character.contains("<seiyuu"))
					chara[5] = character.split("<seiyuu")[1].split(">")[1].split("</seiyuu")[0];
				else
					chara[5] = "not existent";
				chars.add(chara);
			}
		}
		return chars.toArray(new String[0][]);
	}
}
