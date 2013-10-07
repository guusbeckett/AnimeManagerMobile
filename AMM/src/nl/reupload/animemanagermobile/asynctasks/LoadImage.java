/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.asynctasks;

import java.lang.ref.WeakReference;

import nl.reupload.animemanagermobile.animefragmens.listadapters.AnimeCardListAdapter;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.dialogs.ShowPickerDialog;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

public class LoadImage extends AsyncTask<String, String, Bitmap> {

	private boolean online;
	private boolean temp;
	private AnimeCardListAdapter cardloader;
	private WeakReference imageViewReference;
	
	public LoadImage(AnimeCardListAdapter act, ImageView view, boolean temp) {
		imageViewReference = new WeakReference(view);
		this.temp = temp;
		this.cardloader = act;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bm = null;
		for (String item : params) {
			if (item != null) {
				if (!DataManage.doesExternalFileExist("/images/" + item, cardloader.getActivity())) {
					AniDBWrapper.fetchImage(item, cardloader.getActivity(), "");
				}
				try {
					bm  = DataManage.loadImageFromExternal(item, cardloader.getActivity());
					cardloader.addBitmapToMemoryCache(String.valueOf(item), bm);	
				} catch (Exception e) {}
			}
		}
		return bm;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		Toast.makeText(cardloader.getActivity(), progress[0], Toast.LENGTH_SHORT).show();
//      setProgressPercent(progress[0]);
  }

  protected void onPostExecute(Bitmap bm) {
//      showDialog("Downloaded " + result + " bytes");
	  if (bm!=null) {
		  try {
		  ImageView view = (ImageView) imageViewReference.get();
		  view.setImageBitmap(bm);
		  view = null;
		  } catch (Exception e) {}
	  }
  }

}
