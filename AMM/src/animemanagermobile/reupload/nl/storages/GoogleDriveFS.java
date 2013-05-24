package animemanagermobile.reupload.nl.storages;

import java.io.InputStreamReader;
import java.util.Collections;

import org.codehaus.jackson.JsonFactory;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.*;

public class GoogleDriveFS extends FileSystem {
	
	//TODO Understand this fucking bullshit
	

	  /** Global instance of the HTTP transport. */
	 //private static HttpTransport HTTP_TRANSPORT;

	  /** Global instance of the JSON factory. */
	 //private static final  JSON_FACTORY = new JacksonFactory();
	
	 private Drive drive;

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
	
	public GoogleDriveFS() {
		  /** Authorizes the installed application to access user's protected data. */
		    // load client secrets
		    /*GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
		        new InputStreamReader(DriveSample.class.getResourceAsStream("/client_secrets.json")));
		    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
		        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
		      System.out.println(
		          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
		          + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
		      System.exit(1);*/
	//	GoogleClientSecrets clientSecrets = GoogleClientSecrets;
		    // set up file credential store
		    //FileCredentialStore credentialStore = new FileCredentialStore(
		    //   new java.io.File(System.getProperty("user.home"), ".credentials/drive.json"), JSON_FACTORY);
		    // set up authorization code flow
		//    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		  //      HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
		    //    Collections.singleton(DriveScopes.DRIVE_FILE)).setCredentialStore(credentialStore).build();
		    // authorize
		    //new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

	}
	

}
