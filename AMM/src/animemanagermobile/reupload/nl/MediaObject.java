package animemanagermobile.reupload.nl;

import java.io.Serializable;

import android.util.Log;

public class MediaObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6107033642861376397L;
	private String title;
	public void setTitle(String title) {
		this.title = title;
	}

	private int progress;
	private int total;
	private int id;
	private int type;

	public MediaObject(String title, int progress, int total) {
		this.title = title;
		this.progress = progress;
		this.total = total;
		this.id = 0;
	}

	public MediaObject(String string) {
		this.title = string;
		this.progress = 0;
		this.total = 0;
		this.id = 0;
	}
	
	public MediaObject(int type, String string, int ID) {
		this.title = string;
		this.progress = 0;
		this.total = 0;
		this.id = ID;
		this.type = type;
	}
	
	public MediaObject(String string, int ID) {
		this.title = string;
		this.progress = 0;
		this.total = 0;
		this.id = ID;
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
		Log.d("prog", progress+"");
		return progress;
	}

	public int getTotal() {
		return total;
	}
	
	public void setProgress(int i) {
		Log.d("prog", i+"");
		
		progress = i;
		Log.d("prog", progress+"");
	}
	
	public void setTotal(int i) {
		total = i;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getWriteable(boolean done, boolean manga) {
		// TODO Auto-generated method stub
		return getTitle() + ((done)?"":((manga)?" ch ":" ep ") + getProgress() + "...");
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
