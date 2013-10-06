/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.data;

import java.io.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MetadataDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AMMMeta.db";

    public MetadataDatabase(Context context) {
    	super(context, getPath(context), null, DATABASE_VERSION);
    }
    
    public static String getPath(Context context) {
    	SharedPreferences settings = context.getSharedPreferences("AMMprefs", 0);
        if (settings.getBoolean("MetadataExtStorage", false))
        	return context.getExternalFilesDir(null).getAbsolutePath() + "/db/" + DATABASE_NAME;
        else
        	return DATABASE_NAME;
    }
    
    public static boolean moveDB(Context context,boolean internal)
    {
    	File path = context.getDatabasePath(DATABASE_NAME);
    	Log.d("he", path.toString());
    	if (internal) {
    		try {
    			copy(path, new File(context.getExternalFilesDir(null).getAbsolutePath() + "/db/" + DATABASE_NAME));
    			if (new File(context.getExternalFilesDir(null).getAbsolutePath() + "/db/" + DATABASE_NAME).canRead()) {
    				path.delete();
    				return true;
    			} else return false;
    		} catch (IOException e) {
    			return false;
    		}
    	} else {
    		try {
    			copy(new File(context.getExternalFilesDir(null).getAbsolutePath() + "/db/" + DATABASE_NAME), path);
    			if (path.canRead()) {
    				new File(context.getExternalFilesDir(null).getAbsolutePath() + "/db/" + DATABASE_NAME).delete();
    				return true;
    			} else return false;
    		} catch (IOException e) {
    			return false;
    		}
    	}
    }
    
    public static void copy(File src, File dst) throws IOException {
    	Log.d("MoveFile", "Moving from " + src + " to " + dst);
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MetaData (" +
	        		"Type INTEGER, " +
	        		"Source INTEGER, " +
	        		"ID INTEGER, " +
	        		"SeriesType TEXT, " +
	        		"EPorCHcnt INTEGER, " +
	        		"StartDate INTEGER, " +
	        		"EndDate INTEGER, " +
	        		"Titles TEXT, " +
	        		"Related TEXT, " +
	        		"Similar TEXT, " +
	        		"Reccomendations TEXT, " +
	        		"URL TEXT, " +
	        		"Creators TEXT, " +
	        		"Description TEXT, " +
	        		"Ratings TEXT, " +
	        		"Picture TEXT, " +
	        		"Categories TEXT, " +
	        		"Resources TEXT, " +
	        		"Tags TEXT, " +
	        		"Characters TEXT, " +
	        		"EPsorCHs TEXT, " +
	        		"_id INTEGER PRIMARY KEY" +
	        		");");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}