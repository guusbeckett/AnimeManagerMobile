/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.asynctasks;

import nl.reupload.animemanagermobile.mangareader.sources.MangaUpdatesSource;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public class FetchManga extends AsyncTask<Integer, String, Boolean> {

	private Activity act;
	private String title;
	private MangaUpdatesSource source;

	
	public FetchManga(Activity act, String title) {
		this.act = act;
		this.title = title;
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		for (Integer item : params) {
			source = new MangaUpdatesSource(title, act);
			publishProgress(source.fetchFromSource(item));
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
//      setProgressPercent(progress[0]);
		Toast.makeText(act, "link " + progress[0] + " found!", Toast.LENGTH_SHORT).show();
  }

  protected void onPostExecute(Long result) {
//      showDialog("Downloaded " + result + " bytes");
  }

}
