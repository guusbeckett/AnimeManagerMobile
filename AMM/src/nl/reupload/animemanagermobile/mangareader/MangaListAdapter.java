/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.mangareader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nl.reupload.animemanagermobile.data.DataManage;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class MangaListAdapter extends BaseAdapter {
 
    private Activity activity;
    private String title;
    private static LayoutInflater inflater=null;
    private int i;
    private boolean[] existence;
    private int total;
    private Map<String, String> sources;
 
    public MangaListAdapter(Activity a, String title, int total) {
    	sources = new HashMap<String, String>();
        activity = a;
        i=1;
        this.total = total;
        this.title = title;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkExistence();
        checkSourcesFound();
    }
    
	private void checkSourcesFound() {
		if (new File(activity.getExternalFilesDir(null), "/mangareleases/"+DataManage.getHash(title)+".xml").exists()) {
			String raw = DataManage.readFromExternal("/mangareleases/"+DataManage.getHash(title)+".xml", activity);
			for (String item : raw.split("<release>")) {
				if (item.contains("<chapter>")) {
					sources.put(item.split("<chapter>")[1].split("</chapter>")[0], item.split("<releasername>")[1].split("</releasername>")[0]);
					try {
						int ch = Integer.parseInt(item.split("<chapter>")[1].split("</chapter>")[0]);
						if (ch > total)
							total = ch;
					} catch (Exception e) {}
				}
			}
		}
		
	}

	public void checkExistence() {
    	existence = new boolean[total];
    	for (int o=0;o<total;o++) { 
    		File f = new File("/storage/sdcard0/.searchmanga/"+title+"/"+(o+1)+"/");
    		existence[o] = f.exists();
    		Log.d("exist", f.toString()+f.exists());
    	}
    }
 
    @Override
	public int getCount() {
        return total;
    }
 
    @Override
	public Object getItem(int position) {
        return position;
    }
 
    @Override
	public long getItemId(int position) {
        return position;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.manga_item, null);
 
        TextView title = (TextView)vi.findViewById(R.id.character_title); // title
        TextView available = (TextView)vi.findViewById(R.id.character_type); // artist name
        TextView local = (TextView)vi.findViewById(R.id.manga_local); // duration
 
        // Setting all values in listview
        title.setText(this.title + " ch " + (position+1));
        try {
        	available.setText(sources.get(position+1+""));
        } catch (Exception e) {available.setText("no sources");}
        	
        try {
        	local.setText((existence[position])?"available":"not available");
        } catch (Exception e) {local.setText("not available");}
        return vi;
    }

	public boolean checkExistenceOf(int chap) {
		try {
			return existence[chap-1];
		} catch (Exception e) {return false;}
				
	}

}