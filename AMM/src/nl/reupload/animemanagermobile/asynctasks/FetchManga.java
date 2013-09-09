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
