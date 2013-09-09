AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.storages;

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
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
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
				return "";
			} catch (IOException e) {
				return "";
			}
			 return new String(baf.toByteArray());
		}
		else return "";
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
	}
	
	public LocalFS(Activity activ) {
		this.activ = activ;
	}

}
