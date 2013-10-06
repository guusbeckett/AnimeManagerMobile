/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile;

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
	private String imageLoc;

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

	public String getImageLoc() {
		return imageLoc;
	}

	public void setImageLoc(String imageLoc) {
		this.imageLoc = imageLoc;
	}
}
