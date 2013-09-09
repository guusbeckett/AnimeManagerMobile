/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.storages;

import com.google.api.services.drive.Drive;

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
