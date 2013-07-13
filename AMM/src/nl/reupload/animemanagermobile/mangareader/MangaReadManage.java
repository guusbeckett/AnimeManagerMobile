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
