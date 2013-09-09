/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MetadataDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AMMMeta.db";

    public MetadataDatabase(Context context) {
    	super(context, getPath(context), null, DATABASE_VERSION);
    }
    
    public static String getPath(Context context) {
    	SharedPreferences settings = context.getSharedPreferences("AMMprefs", 0);
        if (settings.getBoolean("MetadataExtStorage", false))
        	return context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME;
        else
        	return DATABASE_NAME;
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