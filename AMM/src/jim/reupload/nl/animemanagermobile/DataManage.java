package jim.reupload.nl.animemanagermobile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

	
	private DbxAccountManager mDbxAcctMgr;
	private boolean fslive;
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
		for (AnimeObject item : list)
			data += item.getWriteable() + "\n";
		data+="\nReading:"+data2;
		testFile.writeString(data);
		ArrayList<AnimeObject> henk = null;
		testFile.close();
	}
	
	public static boolean isRegistered(String show, Activity act) {
		String in = readRegistered();
	       
		return false;
	}
	
	public static int getAID(String show) {
		String[] in = readRegistered().split("\n");
		int i = 0;
		for (String item : in) {
			if (getMD5(show).equals(item.split(" ")[0]))
				i = Integer.parseInt(item.split(" ")[1]);
		}
		return i;
		
	}
	
	public static void register(String show, int id, Activity act) {
		String in = readRegistered();
		
		in+="\n" + getMD5(show) + " " + id;
		writeRegistered(in, act);
		Context context = act.getApplicationContext();
		CharSequence text = "Registered " + show + " with id " + id;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public static String readRegistered() {
		String out = "";
		FileInputStream fis;
		try {
			fis = new FileInputStream("registered");
			out = new Scanner(fis,"UTF-8").useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			//ignore
		}
	Log.d("wow", out);
	return out;
	}
	
	public static void writeRegistered(String stream, Activity act) {

		FileOutputStream fos;
		try {
			fos = act.openFileOutput("registered", Context.MODE_PRIVATE);
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
	
	public static byte[] getMD5 (String in) {
		byte[] bytesOfMessage = null;
		MessageDigest md = null;
		try {
			bytesOfMessage = in.getBytes("UTF-8");

			
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md.digest(bytesOfMessage);
		
	}
}
