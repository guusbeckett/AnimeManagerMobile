package animemanagermobile.reupload.nl.asynctasks;

import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import animemanagermobile.reupload.nl.mangareader.sources.MangaUpdatesSource;

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

    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

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
