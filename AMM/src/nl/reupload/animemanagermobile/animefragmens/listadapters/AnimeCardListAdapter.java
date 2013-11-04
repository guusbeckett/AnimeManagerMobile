/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.animefragmens.listadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.asynctasks.LoadImage;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MangaUpdatesClient;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AnimeCardListAdapter extends BaseAdapter {
 
    private String title;
    private static LayoutInflater inflater=null;
	private MediaObject[] list;
	private Activity act;
	private LruCache<String, Bitmap> mMemoryCache;
	private int type;
 
    public AnimeCardListAdapter(Activity act, MediaObject[] list, int type) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appendListWithMetadata(list, act, type);
        this.list = list;
        this.act = act;
        this.type = type;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    
    private void appendListWithMetadata(MediaObject[] list2, Activity act, int type) {
		for (int i=0;i<list2.length;i++) {
			int id = DataManage.getID(list2[i].getTitle(), act, type);
			if (id!=0) {
				String[] metadataParse;
				if (type == 1 || type == 2 || type == 5) {
	        		metadataParse = AniDBWrapper.parseAniDBfile(id, act);
	        		if (metadataParse != null) {
	        			list2[i].setImageLoc(metadataParse[10]);
	        			if (metadataParse[1]!=null)
	        				list2[i].setTotal(Integer.parseInt(metadataParse[1]));
	        		}
	        	}
	        	else {
	        		if (MangaUpdatesClient.doesMangaUpdatesfileExist(id, act)){
		        		metadataParse = MangaUpdatesClient.parseMangaUpdatesfile(id, act);
		        		if (metadataParse != null) {
		        		}
		        	}
	        	}
			}
		}
		
	}

	@Override
	public int getCount() {
        return list.length;
    }
 
    @Override
	public Object getItem(int position) {
        return list[position];
    }
 
    @Override
	public long getItemId(int position) {
        return position;
    }
    
    public Activity getActivity()
    {
    	return act;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.animecard, null);
 
        Log.d("ne", "e");
        MediaObject item = list[position];
        TextView title = (TextView)vi.findViewById(R.id.anime_card_title); // title
        TextView subTitle = (TextView)vi.findViewById(R.id.anime_card_subtitle); // artist name
        
 
        // Setting all values in listview
	    title.setText(item.getTitle());
	    if (type==1||type==3)
	    	subTitle.setText(item.getProgress()+"/"+item.getTotal());
	    else {
	    	if (item.getTotal()>0)
	    		subTitle.setText(item.getTotal() + ((type==1||type==2||type==5)?" Episodes":" Chapters"));
	    }
	    ImageView image = (ImageView)vi.findViewById(R.id.anime_card_image);
	    if (item.getImageLoc()!=null) {
	    	
	    	final String imageKey = String.valueOf(item.getImageLoc());

	        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	        if (bitmap != null) {
	            image.setImageBitmap(bitmap);
	        } else {
	            image.setImageBitmap(null);
		    	new LoadImage(this, image, false).execute(item.getImageLoc());
	        }

	    	
//	    	image.setImageBitmap(DataManage.loadImageFromExternal(item.getImageLoc(), act));
	    } else image.setImageBitmap(null);
        //gender.setText((existence[position])?"available":"not available");
        return vi;
    }
    
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

	public void remove(Object item) {
		ArrayList<MediaObject> newlist = new ArrayList<MediaObject>();
		for (int i=0;i<list.length;i++) {
			newlist.add(list[i]);
		}
		newlist.remove(item);
		list = newlist.toArray(new MediaObject[0]);
		
	}

	public void insert(int oldpos, MediaObject backup) {
		ArrayList<MediaObject> newlist = new ArrayList<MediaObject>();
		for (int i=0;i<list.length;i++) {
			newlist.add(list[i]);
		}
		newlist.add(oldpos, backup);
		list = newlist.toArray(new MediaObject[0]);
		
	}
}