package nl.reupload.animemanagermobile.mangareader.sources;

import android.app.Activity;
import android.util.Log;

public abstract class SourceClient {

	protected String title;
	private Activity act;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public SourceClient(String title, Activity act) {
		this.title = title;
		this.setAct(act);
	}
	public abstract boolean isAvailable();
	public abstract boolean[] availableFor();
	public abstract void makeXML(String[][] xml);
	public Activity getAct() {
		return act;
	}
	public void setAct(Activity act) {
		this.act = act;
	}
	public String fetchFromSource(Integer chap) {
		Log.d("url", chap+"");
		String url = getScannerByChapter(chap);
		Log.d("url", url);
		if (url.contains("mangaupdates"))
			url = getActualURL(url);
		Log.d("url", url);
		getDownloadPageFromScannerSite(url);
		url = getFileURLFromDownloadPage("",chap+"");
//		Toast.makeText(act, "link " + url + " found!", Toast.LENGTH_SHORT).show();
		Log.d("url", url);
		return url;
	}
	
	public abstract String getScannerByChapter(Integer chap);
	public abstract String getActualURL(String url);
	public abstract String getDownloadPageFromScannerSite(String url);
	public abstract String getFileURLFromDownloadPage(String url, String chap);
}