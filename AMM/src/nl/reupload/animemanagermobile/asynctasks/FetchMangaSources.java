/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.asynctasks;

import java.net.URL;

import nl.reupload.animemanagermobile.mangareader.sources.MangaUpdatesSource;
import android.app.Activity;
import android.os.AsyncTask;

public class FetchMangaSources extends AsyncTask<String, Integer, Long> {
    private Activity act;
    private fetchManga fetches; 

	protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
//            totalSize += Downloader.downloadFile(urls[i]);
//            publishProgress((int) ((i / (float) count) * 100));
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
       
    }
    
    public FetchMangaSources(Activity act) {
    	this.act = act;
    }

    @Override
	protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    @Override
	protected void onPostExecute(Long result) {
//        showDialog("Downloaded " + result + " bytes");
    	fetches.isDone();
    }
    
    public void setFetchListener(fetchManga manga) {
    	fetches = manga;
    }

	@Override
	protected Long doInBackground(String... params) {
		for (String item : params)
			new MangaUpdatesSource(item, act);
		return null;
	}
	
	public interface fetchManga {
		public void isDone();
	}
}
