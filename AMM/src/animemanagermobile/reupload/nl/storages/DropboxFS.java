package animemanagermobile.reupload.nl.storages;

import java.io.IOException;

import android.app.Activity;
import animemanagermobile.reupload.nl.keysnstuff.KeysNStuff;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxException.Unauthorized;
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
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String readStringFromFile(String filename) {
		DbxFile file = openFile(filename);
		String content ="";
		try {
			content  = file.readString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO handle exceptions
		file.close();
		return content;
	}
	
	public DbxFile openFile(String path) {
		DbxFile testFile = null;
		try {
			testFile = dbxFs.open(new DbxPath(path));
		} catch (InvalidPathException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
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
