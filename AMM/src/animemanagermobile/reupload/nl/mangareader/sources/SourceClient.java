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
	public abstract void fetchFromSource();
	public Activity getAct() {
		return act;
	}
	public void setAct(Activity act) {
		this.act = act;
	}
}