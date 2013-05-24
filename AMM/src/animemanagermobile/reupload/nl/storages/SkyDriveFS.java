package animemanagermobile.reupload.nl.storages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;
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
import com.microsoft.live.LiveOperationListener;
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

	@Override
	public boolean writeStringToFile(String data, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String readStringFromFile(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
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
                    			//skydrivelisten.filefound(skyDriveObj.getId());
                    			break; //TODO intercept done
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
	
	public void skyDriveInit(final Activity activ) {
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
	        		openSkydriveFolder(HOME_FOLDER, activ, 0);
	        		boolean exist = false;
	        		Log.d("fs start", "sky client sky");
					
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
				e.printStackTrace();
			}
        	Log.d("waiting", "nothing yet");
    	}
    	*/
	}
	
	
	public void findWatchingFile (Activity activ) {
		openSkydriveFolder(folderId2, activ, 1);
	}

}
