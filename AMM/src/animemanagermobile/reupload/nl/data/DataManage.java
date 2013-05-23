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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;
import animemanagermobile.reupload.nl.AnimeObject;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.R;
import animemanagermobile.reupload.nl.skydrive.JsonKeys;
import animemanagermobile.reupload.nl.skydrive.SkyDriveFile;
import animemanagermobile.reupload.nl.skydrive.SkyDriveObject;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;
import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveOperationListener;
import com.microsoft.live.LiveStatus;

public class DataManage {

	
	private static Object cached;
	private DbxAccountManager mDbxAcctMgr;
	private LiveConnectClient mClient;
	private static boolean fslive;
	private final String live_client_id = "000000004C0EC966";
	private static Object cached2;
	private static Object cached3;
	private DbxFileSystem dbxFs;
	private AnimeObject[] list;
	private static final String HOME_FOLDER = "me/skydrive";
	private boolean writePos;
	private LiveAuthClient mAuthClient;
	private int connect;
	protected LiveConnectSession livesession;
	protected ArrayList<SkyDriveObject> skydriveO;
	private Object mInitializeDialog;
	private skydrive skydrivelisten;
	protected SkyDriveFile file;
	protected String folderId2;
	
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
				fslive = true;
        		break;
        	case (2):
        		skyDriveInit(activ);
        		skydrivelisten = (skydrive) activ;
        		
        		fslive = true;
        		/*if (connect == 1) {
	        		((Dialog) mInitializeDialog).dismiss();
	        		Log.d("fs start", "sky client open");
	        		mClient = new LiveConnectClient(livesession);
	        		Log.d("fs start", "sky client live");
	        		openSkydriveFolder(HOME_FOLDER, activ);
	        		Log.d("fs start", "sky folder read");
	        	}*/
	        	Log.d("fs start", "sky authed");
        		break;
        }
        
	}
	public boolean isAsync(Activity activ) {
		int type = activ.getSharedPreferences("AMMprefs", 0).getInt("storageMethod", 0);
		if (type == 2) {
			return true;
		}
		else
			return false;
	}
	
	public void findWatchingFile (Activity activ) {
		openSkydriveFolder(folderId2, activ, 1);
	}
	
	public void openSkyDriveFile (String folder, final String name, final Activity activ) {
		mClient.getAsync(folder + name, new LiveOperationListener() {
			@Override
             public void onComplete(LiveOperation operation) {
                JSONObject result = operation.getResult();
                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String message = error.optString(JsonKeys.MESSAGE);
                    String code = error.optString(JsonKeys.CODE);
                    Toast.makeText(activ, code + ": " + message, Toast.LENGTH_LONG).show();
                    boolean exist = false;
                    return;
                }

                file = null;
                JSONArray data = result.optJSONArray(JsonKeys.DATA);
                for (int i = 0; i < data.length(); i++) {
                	if (SkyDriveObject.create(data.optJSONObject(i)).getName().equals(name)) {
                		file = (SkyDriveFile) SkyDriveObject.create(data.optJSONObject(i));
                	}
                }
                skydrivelisten.fileReady();

            }
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
            	Toast.makeText(activ, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
	}
	
	public void openSkydriveFolder(String folderId, final Activity activ, int i) {
		mClient.getAsync(folderId + "/files", new LiveOperationListener() {
            private ArrayList<SkyDriveObject> skyDriveObjs = new ArrayList<SkyDriveObject>();
			@Override
            public void onComplete(LiveOperation operation) {
                JSONObject result = operation.getResult();
                if (result.has(JsonKeys.ERROR)) {
                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                    String message = error.optString(JsonKeys.MESSAGE);
                    String code = error.optString(JsonKeys.CODE);
                    Toast.makeText(activ, code + ": " + message, Toast.LENGTH_LONG).show();
                    
                    return;
                }

                
                
                JSONArray data = result.optJSONArray(JsonKeys.DATA);
                for (int i = 0; i < data.length(); i++) {
                    SkyDriveObject skyDriveObj = SkyDriveObject.create(data.optJSONObject(i));
                    skyDriveObjs.add(skyDriveObj);
                    switch (1) {
                    	case (0):
		                    if (skyDriveObj.getName().contains("AMM"))
		                    	folderId2 = skyDriveObj.getId();
                    		break;
                    	case (1):
                    		if (skyDriveObj.getName().contains("watching.txt"))
                    			skydrivelisten.filefound(skyDriveObj.getId());
                    }
                    Log.d("skydrive", skyDriveObj.getName() + " & " + skyDriveObj.getId());
                }
                
                boolean exist = false;
                if (connect == 1) {
                	if (skydriveO != null) {
                		exist = true;
	        		}
	        		if (!exist) {
		        		Map<String, String> folder = new HashMap<String, String>();
						folder.put(JsonKeys.NAME, "AMM");
						folder.put(JsonKeys.DESCRIPTION, "This the folder where your Anime Manager Mobile files will be stored");
	
						/*final ProgressDialog progressDialog = showProgressDialog(
								"", "Saving. Please wait...", true);
						progressDialog.show();
						*/
						mClient.postAsync(HOME_FOLDER, new JSONObject(
								folder), new LiveOperationListener() {
							@Override
							public void onError(
									LiveOperationException exception,
									LiveOperation operation) {
								Log.d("skydrive makefolder", "Something went horribly wrong");
							}
	
							@Override
							public void onComplete(LiveOperation operation) {
								JSONObject result = operation.getResult();
								if (result.has(JsonKeys.ERROR)) {
									JSONObject error = result
											.optJSONObject(JsonKeys.ERROR);
									String message = error
											.optString(JsonKeys.MESSAGE);
									String code = error
											.optString(JsonKeys.CODE);
									Log.d("skydrive makefolder", "Something went horribly wrong here as well");
								} else {
									Log.d("skydrive makefolder", "Something went pretty good");
								}
							}
						});
	        		}
	        		else
	        			Log.d("skydrive folder check", "AMM exists");
	        		
                }
               	skydriveO = skyDriveObjs;

            }
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
            	Toast.makeText(activ, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
	}
	
	public interface skydrive {
		public void initdone();
		public void fileReady();
		public void filefound(String fileid);
	}
	
	public void skyDriveInit(final Activity activ) {
		Log.d("fs start", "sky init");
		mAuthClient = new LiveAuthClient(activ.getApplication(), live_client_id);
		Log.d("fs start", "sky login");
		final String[] scopes = new String[] {"wl.basic", "wl.offline_access", "wl.skydrive", "wl.skydrive_update"};
		connect = 0;
		Toast.makeText(activ, "Live connect complete", Toast.LENGTH_LONG).show();
		mInitializeDialog = ProgressDialog.show(activ, "", "Initializing. Please wait...", true);
    	mAuthClient.initialize(Arrays.asList(scopes), new LiveAuthListener() {

			@Override
            public void onAuthError(LiveAuthException exception, Object userState) {
				((Dialog) mInitializeDialog).dismiss();
				Log.d("fs start", "sky client died");
            	//Toast.makeText(activ, exception.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthComplete(LiveStatus status,
                                       LiveConnectSession session,
                                       Object userState) {
            	((Dialog) mInitializeDialog).dismiss();
                if (status == LiveStatus.CONNECTED) {
                	connect = 1;
                	livesession = session;
                	Log.d("fs start", "sky client connect");
                	fslive = true;
                	mClient = new LiveConnectClient(livesession);
	        		Log.d("fs start", "sky client live");
	        		openSkydriveFolder(HOME_FOLDER, activ, 0);
	        		boolean exist = false;
	        		Log.d("fs start", "sky client sky");
	        		skydrivelisten.initdone();
					
                	//Toast.makeText(activ, "Live connect complete", Toast.LENGTH_LONG).show();
                } else {
                	Log.d("fs start", "sky connect failure");
                	connect = 2;
                   // Toast.makeText(activ, "Live connect fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    	/*while (connect==0) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Log.d("waiting", "nothing yet");
    	}
    	*/
	}

	public AnimeObject[] getWatchingAnime(Activity activ) {
		if (!fslive) {
			Log.d("what", fslive+"");
			iniateFS(activ);
		}
		ArrayList<AnimeObject> henk = null;
		switch (activ.getSharedPreferences("AMMprefs", 0).getInt("storageMethod", 0)) {
			case (1):
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
				try {
					henk = formatArray(testFile.readString(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testFile.close();
				break;
			case (2):
				if (file == null) {
					Log.d("AMM", "file is NULL");
					//openSkyDriveFile(folderId2, "watching.txt", activ);
				}
				else {
					Log.d("AMM", "file is not NULL");
					Log.d("amm", file.getLink());
				}
				break;
		}
		if (henk != null)
			return henk.toArray(new AnimeObject[0]);
		else
			return new AnimeObject[] {new AnimeObject("no items")};
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
				break;
			case (3):
				for (String nya : lel.split("Reading:")[1].split("\n")) {
					if (!nya.isEmpty())
					{
						String prog = nya.split(" ch ")[1].split("\\.")[0];
						if (prog.length() == 1) {
							list.add(new AnimeObject(nya.split(" ch ")[0], Integer.parseInt(prog)));
						}
						else {
							list.add(new AnimeObject(nya.split(" ch ")[0], 0));
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
	
	public void addNewAnime(Activity act, AnimeObject item) {
		if (list == null)
			list = getWatchingAnime(act);
		AnimeObject[] list2 = new AnimeObject[list.length+1];
		int i = 0;
		for (AnimeObject object : list) {
			list2[i] = object;
			i++;
		}
		list2[list.length] = item;
		list = list2;
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
		String data = null;
		switch (act.getSharedPreferences("AMMprefs", 0).getInt("storageMethod", 0)) {
			case (1):
				
				try {
					testFile = dbxFs.open(new DbxPath("watching.txt"));
				} catch (InvalidPathException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				data = testFile.readString();
				break;
			case (2):
				break;
		}
		if (data!=null) {
			Log.d("ne", "war");
			String data2 = data.split("Reading:")[1];
			data = "Watching:\n";
			Log.d("ne", "war2");
			for (AnimeObject item : list) {
				if (item!=null)
					data += item.getWriteable() + "\n";
			}
			data+="\nReading:"+data2;
			Log.d("ne", "war3");
			//DbxFile testFile = dbxFs.open(new DbxPath("watching.txt"));
			testFile.writeString(data);
			//ArrayList<AnimeObject> henk = null;
			testFile.close();
		}
		
	}
	
	public static boolean isRegistered(String show, Activity act) {
		String in = readRegistered(act);
	       
		return false;
	}
	
	public static int getID(String show, Activity act, int type) {
		String[] in = readRegistered(act).split("\n");
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
		return i;
		
	}
	
	public static void register(String show, int id, Activity act, int i) {
		String in = readRegistered(act);
		Log.d("old registers", in);
		String type = "anime ";
		switch (i) {
			case (1):
				type = "anime ";
			case (2):
				type = "anime ";
			case (3):
				type = "manga ";
			case (4):
				type = "manga ";
		}
		
		in+="\n" + type + getHash(show) + " " + id;
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


	public MediaObject[] getReadingManga(Activity activ) {
		if (!fslive) {
			Log.d("what", fslive+"");
			iniateFS(activ);
		}
		ArrayList<AnimeObject> henk = null;
		switch (activ.getSharedPreferences("AMMprefs", 0).getInt("storageMethod", 0)) {
			case (1):
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
				try {
					henk = formatArray(testFile.readString(), 3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testFile.close();
				break;
			case (2):
				if (file == null) {
					Log.d("AMM", "file is NULL");
					//openSkyDriveFile(folderId2, "watching.txt", activ);
				}
				else {
					Log.d("AMM", "file is not NULL");
					Log.d("amm", file.getLink());
				}
				break;
		}
		if (henk != null)
			return henk.toArray(new AnimeObject[0]);
		else
			return new AnimeObject[] {new AnimeObject("no items")};
	}


	public MediaObject[] getReadManga(Activity activ) {
		if (!fslive) {
			Log.d("what", fslive+"");
			iniateFS(activ);
		}
		ArrayList<AnimeObject> henk = null;
		switch (activ.getSharedPreferences("AMMprefs", 0).getInt("storageMethod", 0)) {
			case (1):
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
				try {
					henk = formatArray(testFile.readString(), 4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testFile.close();
				break;
			case (2):
				if (file == null) {
					Log.d("AMM", "file is NULL");
					//openSkyDriveFile(folderId2, "watching.txt", activ);
				}
				else {
					Log.d("AMM", "file is not NULL");
					Log.d("amm", file.getLink());
				}
				break;
		}
		if (henk != null)
			return henk.toArray(new AnimeObject[0]);
		else
			return new AnimeObject[] {new AnimeObject("no items")};
	}


	public MediaObject getMangaDetails(Activity activ, int point) {
		list = (AnimeObject[]) getReadingManga(activ);
		return list[point];
	}


	public MediaObject getFullMangaDetails(Activity activ, int point) {
		list = (AnimeObject[]) getReadManga(activ);
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
	
	
	
	
}
