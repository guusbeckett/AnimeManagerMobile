package animemanagermobile.reupload.nl.mangareader.sources;

import android.app.Activity;

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
	public void fetchFromSource(Integer chap) {
		String url = getScannerByChapter(chap);
		if (url.contains("mangaupdates"))
			url = getActualURL(url);
		url = getDownloadPageFromScannerSite(url);
		url = getFileURLFromDownloadPage(url);
	}
	
	public abstract String getScannerByChapter(Integer chap);
	public abstract String getActualURL(String url);
	public abstract String getDownloadPageFromScannerSite(String url);
	public abstract String getFileURLFromDownloadPage(String url);
}