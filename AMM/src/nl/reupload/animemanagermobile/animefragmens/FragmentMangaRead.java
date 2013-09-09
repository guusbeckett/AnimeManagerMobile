AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.animefragmens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.asynctasks.FetchManga;
import nl.reupload.animemanagermobile.asynctasks.FetchMangaSources;
import nl.reupload.animemanagermobile.asynctasks.FetchMangaSources.fetchManga;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.mangareader.MangaListAdapter;
import nl.reupload.animemanagermobile.mangareader.MangaView;

public class FragmentMangaRead extends Fragment implements fetchManga {

	private MediaObject media;
	private ListView lv;
	//private LinearLayout linlay;

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
        //linlay = new LinearLayout(this.getActivity());
        //view.addView(linlay);
		final FragmentActivity activ = this.getActivity();
        //linlay.setOrientation(LinearLayout.VERTICAL);
        
        lv = new ListView(this.getActivity());
        final MangaListAdapter adapt = new MangaListAdapter(getActivity(), media.getTitle(), DataManage.getMangaChapters(media.getTitle()));
        lv.setAdapter(adapt);
        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (adapt.checkExistenceOf(arg2+1)) {
					Intent intent = new Intent(activ, MangaView.class);
					intent.putExtra("title", media.getTitle());
					intent.putExtra("chapter", arg2+1);
					startActivity(intent);
					Log.d("start", media.getTitle());
				}
				else {
					FetchManga mangafetches = new FetchManga(activ, media.getTitle());
					mangafetches.execute(new Integer[]{arg2+1});
//					new MangaUpdatesSource(media.getTitle(), activ).fetchFromSource(arg2+1);
					Toast.makeText(activ, "Sorry, not implemented yet :(", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        view.addView(lv);
        if (media.getId() != 0 && !DataManage.doesExternalFileExist("/mangareleases/"+DataManage.getHash(media.getTitle())+".xml", this.getActivity())) {
        	AsyncTask<String, Integer, Long> fetches = new FetchMangaSources(this.getActivity());
        	((FetchMangaSources) fetches).setFetchListener(this);
        	fetches.execute(new String[]{media.getTitle()});
        }
        return v;
    }

	@Override
	public void isDone() {
		// TODO Auto-generated method stub
		MangaListAdapter adapt = new MangaListAdapter(getActivity(), media.getTitle(), DataManage.getMangaChapters(media.getTitle()));
        lv.setAdapter(adapt);
        lv.invalidate();
	}
}
