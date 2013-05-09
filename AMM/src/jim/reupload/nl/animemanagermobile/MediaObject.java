package jim.reupload.nl.animemanagermobile;

import java.io.Serializable;

public class MediaObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6107033642861376397L;
	private String title;
	private int progress;
	private int total;

	public MediaObject(String title, int progress, int total) {
		this.title = title;
		this.progress = progress;
		this.total = total;
	}

	public String getTitle() {
		return title;
	}

	public static String[] convertMediaObjectArrayToStringArray(
			MediaObject[] items) {
		String[] newItems = new String[items.length];
		int i = 0;
		for (MediaObject object : items) {
			newItems[i] = object.getTitle();
			i++;
		}
		return newItems;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getProgress() {
		return progress;
	}

	public int getTotal() {
		return total;
	}
	
	public void setProgress(int i) {
		progress = i;
	}
	
	public void setTotal(int i) {
		total = i;
	}
}
