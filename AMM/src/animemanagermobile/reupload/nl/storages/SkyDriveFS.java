package animemanagermobile.reupload.nl.storages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import animemanagermobile.reupload.nl.data.AniDBWrapper;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.keysnstuff.KeysNStuff;
import animemanagermobile.reupload.nl.skydrive.JsonKeys;
import animemanagermobile.reupload.nl.skydrive.SkyDriveFile;
import animemanagermobile.reupload.nl.skydrive.SkyDriveObject;

import com.microsoft.live.LiveAuthClient;
import com.microsoft.live.LiveAuthException;
import com.microsoft.live.LiveAuthListener;
import com.microsoft.live.LiveConnectClient;
import com.microsoft.live.LiveConnectSession;
import com.microsoft.live.LiveOperation;
import com.microsoft.live.LiveOperationException;
import com.microsoft.live.LiveStatus;


public class SkyDriveFS extends FileSystem {
	
	private LiveAuthClient mAuthClient;
	private LiveConnectClient mClient;
	private static final String HOME_FOLDER = "me/skydrive";
	private int connect;
	protected LiveConnectSession livesession;
	protected ArrayList<SkyDriveObject> skydriveO;
	private Object mInitializeDialog;
	protected SkyDriveFile file;
	protected String folderId2;
	private Activity activ;
	private boolean waitingForResponse = true;
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	@Override
	public boolean writeStringToFile(String data, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String readStringFromFile(String filename) {
		Log.d("request", filename);
		//Log.d("request", DataManage.getWatchingfileLocation());
		if (filename.equals("watching.txt")) {
			if (DataManage.isConnected(activ) && isOnline()) {
				String stream = openSkyDriveFile(DataManage.getWatchingfileLocation(), "", activ);
				DataManage.writeToCache(stream, "/skydrive_cache/watching.txt", activ);
				return stream;
			}
			else {
				String read = null;
				read = DataManage.readFromCache("/skydrive_cache/watching.txt", activ);
				if (read != null)
					return read;
				else return "";
			}
		}
		else if (filename.equals("seen.txt")) {
			if (DataManage.isConnected(activ) && isOnline()) {
				String stream = openSkyDriveFile(DataManage.getSeenfileLocation(), "", activ);
				DataManage.writeToCache(stream, "/skydrive_cache/seen.txt", activ);
				return stream;
			}
			else {
				String read = null;
				read = DataManage.readFromCache("/skydrive_cache/seen.txt", activ);
				if (read != null)
					return read;
				else return "";
			}
		}
		else
			return "";
	}
	
	@Override
	public boolean isOnline() {
		return !waitingForResponse;
	}

	
	public String openSkyDriveFile (String folder, final String name, final Activity activ) {
		if (folder != null) {
			if (android.os.Build.VERSION.SDK_INT > 9) {
			      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			      StrictMode.setThreadPolicy(policy);
			    }
			try {
				//LiveDownloadOperation file = mClient.download(folder);
				LiveOperation op = mClient.get(folder);
				JSONObject result = op.getResult();
	            if (result.has(JsonKeys.ERROR)) {
	                JSONObject error = result.optJSONObject(JsonKeys.ERROR);
	                String message = error.optString(JsonKeys.MESSAGE);
	                String code = error.optString(JsonKeys.CODE);
	                Toast.makeText(activ, code + ": " + message, Toast.LENGTH_LONG).show();
	                boolean exist = false;
	                return null;
	            }
	            else {
	            	Log.d("link","test");
		            //JSONArray data = result.optJSONArray(JsonKeys.SOURCE);
		            Log.d("link",result.get("source").toString());
		            HttpEntity httpresult = AniDBWrapper.httpget(result.get("source").toString(), false);
		            return EntityUtils.toString(httpresult).toString();
		            //SkyDriveFile file = (SkyDriveFile) data.get(0);
		            //Log.d("link",file.getSource());
	            }
				/*StringBuilder inputString)Builder = new StringBuilder();
		        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getStream(), "UTF-8"));
		        String line = bufferedReader.readLine();
		        while(line != null){
		            inputStringBuilder.append(line);inputStringBuilder.append('\n');
		            line = bufferedReader.readLine();
		        }
		        System.out.println(inputStringBuilder.toString());*/
	
	
				//file.getStream();
				//JSONObject result = file.get
			} catch (LiveOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		/*
		mClient.getAsync(folder+"/file", new LiveOperationListener() {
			@Override
             public void onComplete(LiveOperation operation) {
                JSONObject result = operation.getResult();
                Log.d("heh",result.toString());
                Log.d("heh","neh1");
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
//                	if (SkyDriveObject.create(data.optJSONObject(i)).getName().equals(name)) {
//                		file = (SkyDriveFile) SkyDriveObject.create(data.optJSONObject(i));
//                	}
                	SkyDriveFile file = new SkyDriveFile(data.optJSONObject(i));
                	Log.d("heh",file.getLink());
                	Log.d("heh","I tried");
                }

            }
            @Override
            public void onError(LiveOperationException exception, LiveOperation operation) {
            	Toast.makeText(activ, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/ catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		}
		else return null;
	}
	
	public void openSkydriveFolder(String folderId, final Activity activ, int i) {
		Log.d("no", "crash");
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		try {
			LiveOperation op = mClient.get(folderId + "/files");
			JSONObject result = op.getResult();
            if (result.has(JsonKeys.ERROR)) {
                JSONObject error = result.optJSONObject(JsonKeys.ERROR);
                String message = error.optString(JsonKeys.MESSAGE);
                String code = error.optString(JsonKeys.CODE);
                Toast.makeText(activ, code + ": " + message, Toast.LENGTH_LONG).show();
                
                return;
            }
            JSONArray data = result.optJSONArray(JsonKeys.DATA);
            for (int a = 0; a < data.length(); a++) {
                SkyDriveObject skyDriveObj = SkyDriveObject.create(data.optJSONObject(a));
                switch (0) {
                	case (0):
	                    if (skyDriveObj.getName().equals("AMM")) {
	                    	folderId2 = skyDriveObj.getId();
                			findWatchingFile(activ);
	                    }
	                    else if (skyDriveObj.getName().equals("watching.txt")) {
	                    	DataManage.setWatchingfileLocation(skyDriveObj.getId());
	                    	openSkyDriveFile(skyDriveObj.getId(), "", activ);
	                    }
	                    else if (skyDriveObj.getName().equals("seen.txt")) {
	                    	DataManage.setSeenfileLocation(skyDriveObj.getId());
	                    	openSkyDriveFile(skyDriveObj.getId(), "", activ);
	                    }
                		break;
                	case (1):
                		if (skyDriveObj.getName().contains("watching.txt"))
                			//skydrivelisten.filefound(skyDriveObj.getId());
                			break; //TODO intercept done
                }
                Log.d("skydrive", skyDriveObj.getName() + " & " + skyDriveObj.getId());
            }
		} catch (LiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		mClient.getAsync(folderId + "/files", new LiveOperationListener() {
//			
//            private ArrayList<SkyDriveObject> skyDriveObjs = new ArrayList<SkyDriveObject>();
//            
//			@Override
//            public void onComplete(LiveOperation operation) {
//                JSONObject result = operation.getResult();
//                if (result.has(JsonKeys.ERROR)) {
//                    JSONObject error = result.optJSONObject(JsonKeys.ERROR);
//                    String message = error.optString(JsonKeys.MESSAGE);
//                    String code = error.optString(JsonKeys.CODE);
//                    Toast.makeText(activ, code + ": " + message, Toast.LENGTH_LONG).show();
//                    
//                    return;
//                }
//
//                
//                
//                JSONArray data = result.optJSONArray(JsonKeys.DATA);
//                for (int i = 0; i < data.length(); i++) {
//                    SkyDriveObject skyDriveObj = SkyDriveObject.create(data.optJSONObject(i));
//                    skyDriveObjs.add(skyDriveObj);
//                    switch (0) {
//                    	case (0):
//		                    if (skyDriveObj.getName().equals("AMM")) {
//		                    	folderId2 = skyDriveObj.getId();
//                    			findWatchingFile(activ);
//		                    }
//		                    else if (skyDriveObj.getName().equals("watching.txt")) {
//		                    	DataManage.setWatchingfileLocation(skyDriveObj.getId());
//		                    	openSkyDriveFile(skyDriveObj.getId(), "", activ);
//		                    }
//                    		break;
//                    	case (1):
//                    		if (skyDriveObj.getName().contains("watching.txt"))
//                    			//skydrivelisten.filefound(skyDriveObj.getId());
//                    			break; //TODO intercept done
//                    }
//                    Log.d("skydrive", skyDriveObj.getName() + " & " + skyDriveObj.getId());
//                }
//                Log.d("no", "crash");
//                boolean exist = false;
//                if (connect == 1) {
//                	if (skydriveO != null) {
//                		exist = true;
//	        		}
//	        		if (!exist) {
//		        		Map<String, String> folder = new HashMap<String, String>();
//						folder.put(JsonKeys.NAME, "AMM");
//						folder.put(JsonKeys.DESCRIPTION, "This the folder where your Anime Manager Mobile files will be stored");
//	
//						/*final ProgressDialog progressDialog = showProgressDialog(
//								"", "Saving. Please wait...", true);
//						progressDialog.show();
//						*/
//						mClient.postAsync(HOME_FOLDER, new JSONObject(
//								folder), new LiveOperationListener() {
//							@Override
//							public void onError(
//									LiveOperationException exception,
//									LiveOperation operation) {
//								Log.d("skydrive makefolder", "Something went horribly wrong");
//							}
//	
//							@Override
//							public void onComplete(LiveOperation operation) {
//								JSONObject result = operation.getResult();
//								if (result.has(JsonKeys.ERROR)) {
//									JSONObject error = result
//											.optJSONObject(JsonKeys.ERROR);
//									String message = error
//											.optString(JsonKeys.MESSAGE);
//									String code = error
//											.optString(JsonKeys.CODE);
//									Log.d("skydrive makefolder", "Something went horribly wrong here as well");
//								} else {
//									Log.d("skydrive makefolder", "Something went pretty good");
//								}
//							}
//						});
//	        		}
//	        		else
//	        			Log.d("skydrive folder check", "AMM exists");
//	        		
//                }
//                Log.d("no", "crash");
//               	//skydriveO = skyDriveObjs;
//
//            }
//            @Override
//            public void onError(LiveOperationException exception, LiveOperation operation) {
//            	Log.d("no", "crash");
//            	Toast.makeText(activ, exception.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
	}
	
	public void skyDriveInit(final Activity activ) {
		lock.lock();
		 
		Log.d("fs start", "sky init");
		mAuthClient = new LiveAuthClient(activ.getApplication(), KeysNStuff.live_client_id);
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
                	mClient = new LiveConnectClient(livesession);
	        		Log.d("fs start", "sky client live");
	        		if (DataManage.getWatchingfileLocation() == null)
	        			openSkydriveFolder(HOME_FOLDER, activ, 0);
	        		waitingForResponse = false;
	        		DataManage.setSession(session);
	        		Log.d("fs start", "sky client sky");
					
                	//Toast.makeText(activ, "Live connect complete", Toast.LENGTH_LONG).show();
                } else {
                	Log.d("fs start", "sky connect failure");
                	connect = 2;
                   // Toast.makeText(activ, "Live connect fail", Toast.LENGTH_LONG).show();
                }
                try {
    			    condition.signalAll();
    			  } finally {
    			    lock.unlock();
    			  }
            }
        });
    	/*while (connect==0) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	Log.d("waiting", "nothing yet");
    	}
    	*/
    	 
	}
	
	public void trulyInit() {
		if (DataManage.getSession()==null)
			skyDriveInit(activ);
		else
			mClient = new LiveConnectClient((LiveConnectSession) DataManage.getSession());
		
	}
	
	public SkyDriveFS(Activity activ) {
		this.activ = activ;
	}
	
	
	public void findWatchingFile (Activity activ) {
		openSkydriveFolder(folderId2, activ, 1);
	}

}
