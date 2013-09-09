AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.mangareader;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MangaReadManage {
	
	private String path;
	private int point;
	
	public MangaReadManage(String string) {
		// TODO Auto-generated constructor stub
		path = string;
		setPoint(1);
	}
	
	public Bitmap loadImageFromExternal(String filename, Activity act) {
	      try {
	          File f = new File(path, filename);
	          Log.d("file ", f.toString());
	          if (!f.exists()) { Log.d("BitMapLoader", "File " + f + " Not Found"); return null; }
	          Bitmap tmp = BitmapFactory.decodeFile(f.toString());
	          return tmp;
	      } catch (Exception e) {
	    	  Log.d("BitMapLoader", "exception found");
	          return null;
	      }
	  }

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public void nextImage() {
		point++;
		
	}

	public void prevImage() {
		if (point > 1)
			point--;
	}

}
