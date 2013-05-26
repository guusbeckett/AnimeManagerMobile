package animemanagermobile.reupload.nl.animefragmens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.R;
import animemanagermobile.reupload.nl.animefragmens.listadapters.CharacterListAdapter;
import animemanagermobile.reupload.nl.data.AniDBWrapper;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.data.MangaUpdatesClient;
import animemanagermobile.reupload.nl.mangareader.MangaListAdapter;
import animemanagermobile.reupload.nl.mangareader.MangaView;

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
		                	AniDBWrapper.fetchImage(((String[])adapt.getItem(arg2))[4], act, "character");
		                	bm = DataManage.loadImageFromExternal("character/"+((String[])adapt.getItem(arg2))[4], act);
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
