package animemanagermobile.reupload.nl.asynctasks;

import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import animemanagermobile.reupload.nl.mangareader.sources.MangaUpdatesSource;

public class FetchManga extends AsyncTask<Integer, Integer, Boolean> {

	private Activity act;
	private String title;

	
	public FetchManga(Activity act, String title) {
		this.act = act;
		this.title = title;
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		for (Integer item : params)
			new MangaUpdatesSource(title, act).fetchFromSource(item);
		return true;
	}

	protected void onProgressUpdate(Integer... progress) {
//      setProgressPercent(progress[0]);
  }

  protected void onPostExecute(Long result) {
//      showDialog("Downloaded " + result + " bytes");
  }

}
