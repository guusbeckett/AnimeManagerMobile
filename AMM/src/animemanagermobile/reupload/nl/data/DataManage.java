package animemanagermobile.reupload.nl.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.storages.DropboxFS;
import animemanagermobile.reupload.nl.storages.FileSystem;
import animemanagermobile.reupload.nl.storages.SkyDriveFS;

public class DataManage {

	
	private FileSystem fs;
	private static Object cached;
	private static boolean fslive;
	private static Object cached2;
	private static Object cached3;
	private static Object cached4;
	private static Object session;
	private static MediaObject[] list;
	private boolean writePos;
	private static String watchingfileLocation;
	private static String seenfileLocation;
	
	public static final int NoFS = 0;
	public static final int DropboxFS = 1;
	public static final int SkyDriveFS = 2;
	public static final int LocalFS = 5;
	
	public DataManage() {
		fslive = false;
	}
	
	
	public void iniateFS(Activity activ)
	{
		SharedPreferences settings = activ.getSharedPreferences("AMMprefs", 0);
        int storageMethod = settings.getInt("storageMethod", 0);
        switch (storageMethod) {
        	case (NoFS):
        		break;
        	case (DropboxFS):
        		fs = new DropboxFS(activ);
				fslive = true;
        		break;
        	case (SkyDriveFS):
        		fs = new SkyDriveFS(activ);
        		((SkyDriveFS) fs).trulyInit();
        		fslive = true;
        		break;
        	case (LocalFS):
        		fs = new animemanagermobile.reupload.nl.storages.LocalFS(activ);
				fslive = true;
        		break;
        }
        
	}
	
	public MediaObject[] getWatchingAnime(Activity activ) {
		if (!fslive) {
			Log.d("what", fslive+"");
			iniateFS(activ);
		}
		Log.d("what", "lel");
		ArrayList<MediaObject> henk = formatArray(fs.readStringFromFile("watching.txt"),1);
		if (henk != null) {
			list = henk.toArray(new MediaObject[0]);
			return list;
		}
		else
			return null;
	}

	public ArrayList<MediaObject> formatArray(String lel, int i) {
		ArrayList<MediaObject> list = null;
		if (lel != null) {
			if (lel.contains("Seen:") || lel.contains("Watching:")) {
				Log.d("lel", "I decided it contains this");
				Log.d("lel", lel);
				list = new ArrayList<MediaObject>();
				switch (i) {
					case (1):
						for (String nya : lel.split("Reading:")[0].split("Watching:")[1].split("\n")) {
							Log.d("what", nya);
							if (!nya.isEmpty())
							{
								String prog = nya.split(" ep ")[1].split("\\.")[0];
								int progs =Integer.parseInt(prog);
								if (prog.length() == 1) {
									list.add(new MediaObject(nya.split(" ep ")[0], progs));
								}
								else {
									list.add(new MediaObject(nya.split(" ep ")[0], 0));
								}
							}
								
						}
					Log.d("what", "bye");
						break;
					case (2):
						for (String nya : lel.split("Read:")[0].split("Seen:")[1].split("\n")) {
							if (!nya.isEmpty())
							{
								String prog = nya;
								if (prog.length() == 1) {
									list.add(new MediaObject(prog));
								}
								else {
									list.add(new MediaObject(prog));
								}
							}
								
						}
						break;
					case (3):
						for (String nya : lel.split("Reading:")[1].split("\n")) {
							if (!nya.isEmpty())
							{
								String prog = nya.split(" ch ")[1].split("\\.")[0];
								if (prog.length() == 1) {
									list.add(new MediaObject(nya.split(" ch ")[0], Integer.parseInt(prog)));
								}
								else {
									list.add(new MediaObject(nya.split(" ch ")[0], 0));
								}
							}
								
						}
						break;
					case (4):
						for (String nya : lel.split("Read:")[1].split("\n")) {
							if (!nya.isEmpty())
							{
								String prog = nya;
								
								if (prog.length() == 1) {
									list.add(new MediaObject(prog));
								}
								else {
									list.add(new MediaObject(prog));
								}
							}
								
						}
						break;
				}
			}
		}
		Log.d("what", "ending");
		return list;
	}
	
	public void setWatchingAnime(Activity activ, MediaObject[] animu) {
		if (!fslive)
			iniateFS(activ);
	}
	
	public MediaObject getAnimeDetails(Activity act, int point) {
		writePos = true;
		list = getWatchingAnime(act);
		return list[point];
		
	}
	
	public void writeSeriesDetails(Activity act, MediaObject item, int point, int type) {
		list[point] = item;
		try {
			writeAlltoFile(act, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO handle exceptions
	}
	
	public void addNewSeries(Activity act, MediaObject item, int j) {
//		switch (j) {
//			case (1):
//				
//		}
		if (list == null)
			list = new MediaObject[0];
		MediaObject[] list2 = new MediaObject[list.length+1];
		int i = 0;
		for (MediaObject object : list) {
			list2[i] = object;
			i++;
		}
		list2[list.length] = (MediaObject) item;
		list = list2;
		try {
			writeAlltoFile(act, j);
		} catch (IOException e) {
			// TODO handle exceptions
			e.printStackTrace();
		}
	}

	private void writeAlltoFile(Activity act, int type) throws IOException {
		if (!fslive)
			iniateFS(act);
		String fname = null;
		String split1 = null;
		String split2 = null;
		boolean manga=(type==3||type==4);
		boolean done = false;
		if (type == 1 || type == 3) {
			fname = "watching.txt";
			split1 = "Reading:";
			split2 = "Watching:";
		}
		else if (type == 2 || type == 4) {
			fname = "seen.txt";
			split1 = "Read:";
			split2 = "Seen:";
			done = true;
		}
		String data = fs.readStringFromFile(fname);
		if (!data.equals("")) {
			Log.d("ne", "war");
			if (manga) {
				String data2 = data.split(split1)[0];
				data = split1+"\n";
				Log.d("ne", "war2");
				for (MediaObject item : list) {
					if (item!=null)
						data += item.getWriteable(done, manga) + "\n";
				}
				data=data2+"\n"+data;
			}
			else {
				String data2 = data.split(split1)[1];
				data = split2+"\n";
				Log.d("ne", "war2");
				for (MediaObject item : list) {
					if (item!=null)
						data += item.getWriteable(done, manga) + "\n";
				}
				data+="\n"+split1+data2;
			}
		}
		else {
			Log.d("datadump", "null");
			if (manga) {
				data = split1+"\n";
				for (MediaObject item : list) {
					if (item!=null)
						data += item.getWriteable(done, manga) + "\n";
				}
				data="\n"+data;
			}
			else {
				data = split2+"\n";
				for (MediaObject item : list) {
					if (item!=null)
						data += item.getWriteable(done, manga) + "\n";
				}
				data+=split2+"\n"+split1+"\n";
			}
		} 
		Log.d("writing",fs.writeStringToFile(data, fname)+"");
	}
	
	public static boolean isRegistered(String show, int type, Activity act) {
		/*String in = readRegistered(act);
	       //TODO fix
		return false;*/
		SQLiteOpenHelper ammData = new AMMDatabase(act);
		SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Registered", new String[]{"ID"}, "Name='"+ show +"' AND Type='"+ type +"'", null, null, null, null);
		
		return (c.getCount() > 0);
	}
	
	public static int getID(String show, Activity act, int type) {
		/*String[] in = readRegistered(act).split("\n");
		String mani = "";
		switch (type) {
	    	case (1):
	    		mani = "anime ";
	    		break;
	    	case (2):
	    		mani = "anime ";
	    		break;
	    	case (3):
	    		mani = "manga ";
	    		break;
	    	case (4):
	    		mani = "manga ";
	    		break;
		}
		int i = 0;
		for (String item : in) {
			if (item.contains("anime") || item.contains("manga")) {
				if ((mani+getHash(show)).equals(mani+item.split(" ")[1]))
					i = Integer.parseInt(item.split(" ")[2]);
				else
					Log.d("reject", mani+item.split(" ")[0] + " is not " + mani+getHash(show));
			}
		}
		return i;*/
		
		SQLiteOpenHelper ammData = new AMMDatabase(act);
		SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Registered", new String[]{"ID"}, "Name='"+ show +"' AND Type='"+ type +"'", null, null, null, null);
		
		
		if (c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(c.getColumnIndex("ID"));
		}
		else
			return 0;
		
	}
	
	public static void register(String show, int id, Activity act, int i) {
//		String in = readRegistered(act);
//		Log.d("old registers", in);
//		String type = "anime ";
//		switch (i) {
//			case (1):
//				type = "anime ";
//			case (2):
//				type = "anime ";
//			case (3):
//				type = "manga ";
//			case (4):
//				type = "manga ";
//		}
//		
//		in+="\n" + type + getHash(show) + " " + id;
//		writeRegistered(in, act);
//		Log.d("new registers", in);
//		Context context = act.getApplicationContext();
//		CharSequence text = "Registered " + show + " with id " + id;
//		int duration = Toast.LENGTH_SHORT;
//		
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
		SQLiteOpenHelper ammData = new AMMDatabase(act);
		SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		Cursor c = ammDatabase.query("Registered", new String[]{"Tracking", "Subber", "Keyword"}, "Name='"+ show +"' AND Type='"+ i +"' AND ID='" + id + "'", null, null, null, null);
		
		ContentValues cv = new ContentValues();
    	cv.put("Name", show);
    	cv.put("Type", i);
    	cv.put("ID", id);
		
		if (c.getCount() > 0) {
			c.moveToFirst();
			cv.put("Tracking", c.getInt(c.getColumnIndex("Tracking")));
	    	cv.put("Subber", c.getString(c.getColumnIndex("Subber")));
	    	cv.put("Keyword", c.getString(c.getColumnIndex("Keyword")));
		}
		cv.put("Tracking", false);
    	cv.put("Subber", "");
    	cv.put("Keyword", "");
		
		
    	
    	ammDatabase.delete("Registered", "Name='"+ show + "' AND ID=" + id + " AND Type='"+i+"'", null);
		ammDatabase.insert("Registered", null, cv);
	}
	
	public static void writeRegistered(String stream, Activity act) {

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(act.getFilesDir(), "registered"));
			fos.write(stream.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void writeToCache(String stream, String filename, Activity act) {
		FileOutputStream fos;
		try {
			File file = new File(act.getCacheDir(), filename.replace(filename.split("/")[filename.split("/").length-1], ""));
			if (!file.exists()) {
				file.mkdirs();
			}
			fos = new FileOutputStream(new File(act.getCacheDir(), filename));
			fos.write(stream.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}
	
	public static String readFromCache(String filename, Activity act) {
		BufferedInputStream buf;
		ByteArrayBuffer baf = null;
		 try {
			buf = new BufferedInputStream(new FileInputStream(new File(act.getCacheDir(), filename)));
			int current = 0;
			baf = new ByteArrayBuffer(1024);
			while ((current = buf.read()) != -1)  {
			    baf.append((byte) current);
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		 return new String(baf.toByteArray());
	}
	
	public static String getHash (String in) {
		try {
			in = URLEncoder.encode(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return in;
		
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    return sb.toString();
	}

	public static boolean writeToExternal(String stream, String filename, Activity act) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			File approot = act.getExternalFilesDir(null);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(approot, filename));
				fos.write(stream.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			return true;
		} else {
		    return false;
		}
		
	}
	
	public static boolean doesExternalFileExist(String filename, Activity act) {
		
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    File approot = act.getExternalFilesDir(null);
		    File file = new File(approot , filename);
		    if (file.exists()) {
		    	Log.d("exist check", "File " + file +" does exist");
		    	return true;
		    }
		    else
		    	return false;
		} else {
		    return false;
		}
	}

	public static String readFromExternal(String filename, Activity act) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    File approot = act.getExternalFilesDir(null);
		    File file = new File(approot , filename);
		    if (doesExternalFileExist(filename, act)) {
		    	BufferedInputStream buf;
				ByteArrayBuffer baf = null;
				 try {
					buf = new BufferedInputStream(new FileInputStream(file));
					int current = 0;
					baf = new ByteArrayBuffer(2048);
					while ((current = buf.read()) != -1)  {
					    baf.append((byte) current);
					}
				} catch (FileNotFoundException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
				 return new String(baf.toByteArray());
		    }
		    else
		    	return null;
		}
		else
			return null;
	}
	
	public static void cacheObject(Object item) {
		cached = item;
	}
	
	public static Object getCached() {
		return cached;
	}
	public static void cacheObject2(Object item) {
		cached2 = item;
	}
	
	public static Object getCached2() {
		return cached2;
	}
	public static void cacheObject3(Object item) {
		cached3 = item;
	}
	
	public static Object getCached3() {
		return cached3;
	}
	
	public static boolean isCached() {
		return (cached!=null);
	}
	public static boolean isCached2() {
		return (cached2!=null);
	}
	public static boolean isCached3() {
		return (cached3!=null);
	}

	public static OutputStream openOutputStreamToExternal(
			File externalFilesDir, String filename) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			    // We can read and write the media
			FileOutputStream fos = null;
			if (!externalFilesDir.exists()){
				externalFilesDir.mkdirs();
			}
				try {
					fos = new FileOutputStream(new File(externalFilesDir, filename));
				} catch (FileNotFoundException e) {
					// TODO handle exception
					e.printStackTrace();
				}
			return fos;
		}
		else
			return null;
	}
	
	 public static Bitmap loadImageFromExternal(String filename, Activity act) {
	      try {
	          File f = new File(act.getExternalFilesDir(null)+"/images/", filename);
	          Log.d("file ", f.toString());
	          if (!f.exists()) { Log.d("BitMapLoader", "File " + f + " Not Found"); return null; }
	          Bitmap tmp = BitmapFactory.decodeFile(f.toString());
	          return tmp;
	      } catch (Exception e) {
	    	  Log.d("BitMapLoader", "exception found");
	          return null;
	      }
	  }
	 
	 public static void clearCaches() {
		 cached =null;
		 cached2=null;
	 }

	public MediaObject[] getSeenAnime(Activity activ) {
		if (!fslive)
			iniateFS(activ);
		ArrayList<MediaObject> henk = formatArray(fs.readStringFromFile("seen.txt"), 2);
		if (henk != null) {
			list = henk.toArray(new MediaObject[0]);
			return list;
		}
		else
			return null;
	}

	public MediaObject getFullAnimeDetails(Activity act, int point) {
		writePos = true;
		list = getSeenAnime(act);
		return list[point];
	}

	public static void deleteExternalFile(String path, Activity activ) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		else {
			throw new IllegalArgumentException(
		            "File not found or IOexception occured while deleting " + path);
		}
		
	}

	public static void unregister(String title, int type, Activity act) {
		/*String wow = "";
		for (String item :readRegistered(act).split("\n")) {
			if (!item.contains(getHash(title)))
				wow+=item+"\n";
		}
		writeRegistered(wow, act);*/
		SQLiteOpenHelper ammData = new AMMDatabase(act);
		SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		ammDatabase.delete("Registered", "Name='"+ title + "' AND Type='" + type + "'", null);
	}
	
	public void DeleteAnimeDetails(Activity act, int point) {
		list[point] = null;
		try {
			writeAlltoFile(act, 1);
		} catch (IOException e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}


	public MediaObject[] getReadingManga(Activity activ) {
		if (!fslive) {
			iniateFS(activ);
		}
		ArrayList<MediaObject> henk = formatArray(fs.readStringFromFile("watching.txt"), 3);
		if (henk != null) {
			list = henk.toArray(new MediaObject[0]);
			return list;
		}
		else
			return null;
	}


	public MediaObject[] getReadManga(Activity activ) {
		if (!fslive) {
			iniateFS(activ);
		}
		ArrayList<MediaObject> henk = formatArray(fs.readStringFromFile("seen.txt"), 4);
		if (henk != null) {
			list = henk.toArray(new MediaObject[0]);
			return list;
		}
		else
			return null;
	}


	public MediaObject getMangaDetails(Activity activ, int point) {
		list = (MediaObject[]) getReadingManga(activ);
		return list[point];
	}


	public MediaObject getFullMangaDetails(Activity activ, int point) {
		list = (MediaObject[]) getReadManga(activ);
		return list[point];
	}

	 public static File[] listFiles(File file2){
		 
	        File directory = file2;
	        return directory.listFiles();
	    }


	public static void deleteAllMeta(Activity activ) {
		for (File file : listFiles(activ.getExternalFilesDir(null))) {
			deleteExternalFile(file.toString(), activ);
		}
		for (File file : listFiles(new File(activ.getExternalFilesDir(null) + "/images/"))) {
			deleteExternalFile(file.toString(), activ);
		}
	}


	public static int getMangaChapters(String title) {
		File f = new File("/storage/sdcard0/.searchmanga/"+title+"/");
		int max = 0;
		if (f.exists())
		{
			
			for (File files : f.listFiles()) {
				Log.d("test", files.toString());
				 try  
				  {  
					 int l = Integer.parseInt(files.toString().split("/")[files.toString().split("/").length-1]);
						if (l>max)
							max=l;
				  }  
				  catch(NumberFormatException nfe)  
				  {  
				  }  
			}
		}
		return max;
	}


	public static String getWatchingfileLocation() {
		return watchingfileLocation;
	}


	public static void setWatchingfileLocation(String watchingfileLocation) {
		DataManage.watchingfileLocation = watchingfileLocation;
	}


	public static String getSeenfileLocation() {
		return seenfileLocation;
	}


	public static void setSeenfileLocation(String seenfileLocation) {
		DataManage.seenfileLocation = seenfileLocation;
	}


	public static Object getSession() {
		return session;
	}


	public static void setSession(Object session) {
		DataManage.session = session;
	}
	
	
	public static boolean isConnected(Activity act) {
		ConnectivityManager connectivityManager 
		     = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


	public static Object getCached4() {
		return cached4;
	}


	public static void setCached4(Object cached4) {
		DataManage.cached4 = cached4;
	}


	public void setList(MediaObject[] list2) {
		this.list = list2;
		
	}
	
	
	
}
