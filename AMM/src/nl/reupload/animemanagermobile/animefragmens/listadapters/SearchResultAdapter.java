/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.animefragmens.listadapters;

import nl.reupload.animemanagermobile.MediaObject;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class SearchResultAdapter extends BaseAdapter {
 
    private static LayoutInflater inflater=null;
	private MediaObject[] items;
 
    public SearchResultAdapter(Activity act, MediaObject[] items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
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
            vi = inflater.inflate(R.layout.search_result, null);
 
        MediaObject item = items[position];
        TextView title = (TextView)vi.findViewById(R.id.search_title); // title
        TextView type = (TextView)vi.findViewById(R.id.search_type); // type
        TextView status = (TextView)vi.findViewById(R.id.search_status); // status
 
        // Setting all values in listview
        title.setText(item.getTitle());
        status.setText((item.getType()==1)?"Watching":
        			  ((item.getType()==2)?"Seen":
        			  ((item.getType()==5)?"Anime Backlog":
        			  ((item.getType()==3)?"Reading":
        			  ((item.getType()==4)?"Read":
        			  ((item.getType()==6)?"Manga Backlog":
        			 ((item.getType()==-1)?"Online Anime":	  
        			 ((item.getType()==-2)?"Online Manga":	  
        							 	   item.getType()+" was not recognized"))))))));
        type.setText("");
        return vi;
    }
}