package animemanagermobile.reupload.nl.storages;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.os.Environment;

public class LocalFS extends FileSystem {

	private Activity activ;

	@Override
	public boolean writeStringToFile(String data, String filename) {
		if (isOnline()) {
			FileOutputStream fos;
			try {
				File file = new File(Environment.getExternalStorageDirectory(), "/AnimeManagerMobile/");
				if (!file.exists()) {
					file.mkdirs();
				}
				fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/AnimeManagerMobile/"+filename));
				fos.write(data.getBytes());
				fos.close();
				return true;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		else return false;
		
	}

	@Override
	public String readStringFromFile(String filename) {
		if (isOnline()) {
			BufferedInputStream buf;
			ByteArrayBuffer baf = null;
			 try {
				buf = new BufferedInputStream(new FileInputStream(new File(Environment.getExternalStorageDirectory(), "/AnimeManagerMobile/"+filename)));
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
		else return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED);
	}
	
	public LocalFS(Activity activ) {
		this.activ = activ;
	}

}
