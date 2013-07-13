package nl.reupload.animemanagermobile.animefragmens;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.MediaPage;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.DataManage;

public class FragmentRelatedSeries extends Fragment {

	private MediaObject media;
	private LinearLayout linlay;
	private boolean tempMode;

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
        	final int origin = Integer.parseInt(metadata[16]);
        	tempMode = metadata[17].equals("1");
        	final int type = Integer.parseInt(metadata[16]);
			ListView lv = new ListView(this.getActivity());
//	        final ListAdapter adapt = new List(this.getActivity(), parseEps(metadata[15]));
			final String[] titles = getTitleArray(metadata[4]);
	        final ArrayAdapter adapt = new ArrayAdapter<String>(this.getActivity(), 
                    android.R.layout.simple_list_item_1, getTitleArray(metadata[4]));
	        lv.setAdapter(adapt);
	        final Activity act = getActivity();
	        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int list = DataManage.getListWhereIDisRegistered(Integer.parseInt(titles[arg2].split("\\^")[1]), act);
					if (list == 0) {
						Intent openTemp = new Intent(getActivity(), MediaPage.class);
						openTemp.putExtra("tempMode", true);
						openTemp.putExtra("mediaID", Integer.parseInt(titles[arg2].split("\\^")[1]));
						openTemp.putExtra("type", ((type==1||type==2||type==5)?2:4));
						openTemp.putExtra("title", titles[arg2].split("\\^")[0]);
						if (!tempMode)
							openTemp.putExtra("origin", origin);
						else
							openTemp.putExtra("origin", act.getIntent().getIntExtra("origin", 0));
						startActivity(openTemp);
					}
					else {
						Intent openExisting = new Intent(getActivity(), MediaPage.class);
						openExisting.putExtra("standalone", true);
						openExisting.putExtra("list", list);
						openExisting.putExtra("mediaID", Integer.parseInt(titles[arg2].split("\\^")[1]));
						startActivity(openExisting);
					}
					
				}
			});
//	        lv.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
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
//					// TODO Auto-generated method stub
//				}
//			});
	        Log.d("ne", "e");
	        view.addView(lv);
		}
        else {
            eps.setText("No Metadata for " + media.getTitle());
            eps.setTypeface(null, Typeface.ITALIC);
        	eps.setTextSize(8);
        }
        linlay.addView(eps);
        
        return v;
    }
	
	private String[] getTitleArray(String stream) {
		ArrayList<String> titles = new ArrayList<String>();
		for (String show : stream.split("<anime id=\"")) {
			Log.d("lel", show);
			//String newshow = show.split(">")[1].split("</anime")[0].replace(">", "")+"^"+show.split("\"")[0];
			if (show.contains("anime")) {
				show = show.split(">")[1].split("</anime")[0]+"^"+show.split("\"")[0];
				if (!show.split("\\^")[0].equals(""))
					titles.add(show);
	//			titles.add(show.split(">")[1].split("</anime")[0]+"^"+show.split("\"")[0]);
				Log.d("lel", show);
			}
		}
		return titles.toArray(new String[0]);
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
