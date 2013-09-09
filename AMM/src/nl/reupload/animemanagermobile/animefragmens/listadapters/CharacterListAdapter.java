AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.animefragmens.listadapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
 
public class CharacterListAdapter extends BaseAdapter {
 
    private String title;
    private static LayoutInflater inflater=null;
    private int i;
	private String[][] items;
 
    public CharacterListAdapter(Activity act, String[][] items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        i = 0;
    }
    
    @Override
	public int getCount() {
        return items.length;
    }
 
    @Override
	public Object getItem(int position) {
        return items[position];
    }
 
    @Override
	public long getItemId(int position) {
        return position;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.character_item, null);
 
        Log.d("ne", "e");
        String[] item = items[position];
        TextView title = (TextView)vi.findViewById(R.id.character_title); // title
        TextView type = (TextView)vi.findViewById(R.id.character_type); // artist name
        TextView gender = (TextView)vi.findViewById(R.id.GenderText); // duration
        TextView seiyuu = (TextView)vi.findViewById(R.id.SeiyuuText); // duration
 
        i++;
        // Setting all values in listview
        title.setText(item[1]);
        gender.setText(item[2]);
        type.setText(item[0]);
        seiyuu.setText(item[5]);
        //gender.setText((existence[position])?"available":"not available");
        return vi;
    }
}