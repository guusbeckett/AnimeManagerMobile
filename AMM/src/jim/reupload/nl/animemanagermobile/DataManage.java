package jim.reupload.nl.animemanagermobile;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

public class DataManage {

	
	private static Object cached;
	private DbxAccountManager mDbxAcctMgr;
	private static boolean fslive;
	private static Object cached2;
	private static Object cached3;
	private DbxFileSystem dbxFs;
	private AnimeObject[] list;
	private boolean writePos;
	
	public DataManage() {
		fslive = false;
	}
	
	public void iniateFS(Activity activ)
	{
		SharedPreferences settings = activ.getSharedPreferences("AMMprefs", 0);
        int storageMethod = settings.getInt("storageMethod", 0);
        switch (storageMethod) {
        	case (0):
        		break;
        	case (1):
        		mDbxAcctMgr = DbxAccountManager.getInstance(activ.getApplicationContext(), activ.getString(R.string.DROPBOX_APP_KEY), activ.getString(R.string.DROPBOX_APP_SECRET));
				try {
					dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
				} catch (Unauthorized e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		break;
        	case (2):
        		break;
        }
        fslive = true;
	}

	public AnimeObject[] getWatchingAnime(Activity activ) {
		if (!fslive)
			iniateFS(activ);
		DbxFile testFile = null;
		try {
			testFile = dbxFs.open(new DbxPath("watching.txt"));
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<AnimeObject> henk = null;
		try {
			henk = formatArray(testFile.readString(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testFile.close();
		if (henk != null)
			return henk.toArray(new AnimeObject[0]);
		else
			return null;
	}

	public ArrayList<AnimeObject> formatArray(String lel, int i) {
		ArrayList<AnimeObject> list = new ArrayList<AnimeObject>();
		switch (i) {
			case (1):
				for (String nya : lel.split("Reading:")[0].split("Watching:")[1].split("\n")) {
					if (!nya.isEmpty())
					{
						String prog = nya.split(" ep ")[1].split("\\.")[0];
						if (prog.length() == 1) {
							list.add(new AnimeObject(nya.split(" ep ")[0], Integer.parseInt(prog)));
						}
						else {
							list.add(new AnimeObject(nya.split(" ep ")[0], 0));
						}
					}
						
				}
				break;
			case (2):
				for (String nya : lel.split("Read:")[0].split("Seen:")[1].split("\n")) {
					if (!nya.isEmpty())
					{
						String prog = nya;
						if (prog.length() == 1) {
							list.add(new AnimeObject(prog));
						}
						else {
							list.add(new AnimeObject(prog));
						}
					}
						
				}
		}
		
		return list;
	}
	
	public void setWatchingAnime(Activity activ, AnimeObject[] animu) {
		if (!fslive)
			iniateFS(activ);
	}
	
	public MediaObject getAnimeDetails(Activity act, int point) {
		writePos = true;
		list = getWatchingAnime(act);
		return list[point];
		
	}
	
	public void writeAnimeDetails(Activity act, AnimeObject item, int point) {
		list[point] = item;
		try {
			writeAllAnime(act);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeAllAnime(Activity act) throws IOException {
		if (!fslive)
			iniateFS(act);
		DbxFile testFile = null;
		try {
			testFile = dbxFs.open(new DbxPath("watching.txt"));
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data = testFile.readString();
		String data2 = data.split("Reading:")[1];
		data = "Watching:\n";
		for (AnimeObject item : list) {
			if (item!=null)
				data += item.getWriteable() + "\n";
		}
		data+="\nReading:"+data2;
		testFile.writeString(data);
		ArrayList<AnimeObject> henk = null;
		testFile.close();
	}
	
	public static boolean isRegistered(String show, Activity act) {
		String in = readRegistered(act);
	       
		return false;
	}
	
	public static int getAID(String show, Activity act) {
		String[] in = readRegistered(act).split("\n");
		int i = 0;
		for (String item : in) {
			if (getHash(show).equals(item.split(" ")[0]))
				i = Integer.parseInt(item.split(" ")[1]);
			else
				Log.d("reject", item.split(" ")[0] + " is not " + getHash(show));
		}
		return i;
		
	}
	
	public static void register(String show, int id, Activity act) {
		String in = readRegistered(act);
		Log.d("old registers", in);
		
		in+="\n" + getHash(show) + " " + id;
		writeRegistered(in, act);
		Log.d("new registers", in);
		Context context = act.getApplicationContext();
		CharSequence text = "Registered " + show + " with id " + id;
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public static String readRegistered(Activity act) {
		BufferedInputStream buf;
		ByteArrayBuffer baf = null;
		 try {
			buf = new BufferedInputStream(new FileInputStream(new File(act.getFilesDir(), "registered")));
			int current = 0;
			baf = new ByteArrayBuffer(1024);
			while ((current = buf.read()) != -1)  {
			    baf.append((byte) current);
			}
		} catch (FileNotFoundException e) {
			Log.e("internal error", "Register file does not exist");
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return new String(baf.toByteArray());
	}
	
	public static void writeRegistered(String stream, Activity act) {

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(act.getFilesDir(), "registered"));
			fos.write(stream.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getHash (String in) {
		try {
			in = URLEncoder.encode(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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

	public AnimeObject[] getSeenAnime(Activity activ) {
		if (!fslive)
			iniateFS(activ);
		DbxFile testFile = null;
		try {
			testFile = dbxFs.open(new DbxPath("seen.txt"));
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<AnimeObject> henk = null;
		try {
			henk = formatArray(testFile.readString(), 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testFile.close();
		if (henk != null)
			return henk.toArray(new AnimeObject[0]);
		else
			return null;
	}

	public MediaObject getFullAnimeDetails(Activity act, int point) {
		writePos = true;
		list = getSeenAnime(act);
		return list[point];
	}

	public static void deleteExternalFile(String path, MediaPage mediaPage) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		else {
			throw new IllegalArgumentException(
		            "File not found or IOexception occured while deleting " + path);
		}
		
	}

	public static void unregister(String name, Activity act) {
		String wow = "";
		for (String item :readRegistered(act).split("\n")) {
			if (!item.contains(getHash(name)))
				wow+=item+"\n";
		}
	}
	
	public void DeleteAnimeDetails(Activity act, int point) {
		list[point] = null;
		try {
			writeAllAnime(act);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
