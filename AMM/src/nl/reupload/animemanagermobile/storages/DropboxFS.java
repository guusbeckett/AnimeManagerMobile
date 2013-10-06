/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.storages;

import java.io.IOException;

import nl.reupload.animemanagermobile.keysnstuff.KeysNStuff;
import android.app.Activity;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

public class DropboxFS extends FileSystem {

	private DbxAccountManager mDbxAcctMgr;
	private DbxFileSystem dbxFs;
	private boolean link;
	
	@Override
	public boolean writeStringToFile(String data, String filename) {
		
		
		DbxFile file = openFile(filename);
		try {
			file.writeString(data);
			file.close();
			return true;
		} catch (IOException e) {
			file.close();
			return false;
		}
		
	}

	@Override
	public String readStringFromFile(String filename) {
		DbxFile file = openFile(filename);
		if (file != null) {
			String content ="";
			try {
				content  = file.readString();
			} catch (IOException e) {
				content = null;
			}
			//TODO handle exceptions
			file.close();
			return content;
		}
		else return "";
	}
	
	public DbxFile openFile(String path) {
		try {
			if (!dbxFs.exists(new DbxPath(path)))
				dbxFs.create(new DbxPath(path));
			
		} catch (InvalidPathException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DbxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DbxFile testFile = null;
		try {
			testFile = dbxFs.open(new DbxPath(path));
		} catch (InvalidPathException e) {
			return null;
		} catch (DbxException e) {
			return null;
		}
		//TODO handle exceptions properly
		return testFile;
	}

	@Override
	public boolean isOnline() {
		return link;
	}
	
	public DropboxFS(Activity activ) {
		mDbxAcctMgr = DbxAccountManager.getInstance(activ.getApplicationContext(), KeysNStuff.Drbxkey, KeysNStuff.Drbxsecret);
		try {
			dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
			link = true;
		} catch (Unauthorized e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}
	

}
