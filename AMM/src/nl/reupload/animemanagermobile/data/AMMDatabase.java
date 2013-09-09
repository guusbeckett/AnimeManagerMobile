/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AMMDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DICTIONARY_TABLE_NAME = "Subteams";
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + DICTIONARY_TABLE_NAME + " (" +
                "Name" + " TEXT, " +
                "RSS_URL" + " TEXT, " +
                "LastPost" + " TEXT);";

    public AMMDatabase(Context context) {
        super(context, "AMMDatabase", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
        db.execSQL("CREATE TABLE IF NOT EXISTS Registered (Name TEXT, Type INTEGER, ID INTEGER, Tracking BOOLEAN, Subber TEXT, Keyword TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Feeds (Title TEXT, Link TEXT, Description TEXT, Author TEXT, Category TEXT, Comments TEXT, Enclosure TEXT, guid TEXT, pubDate TEXT, Source TEXT, Content TEXT, Important BOOLEAN, feedname TEXT, Read BOOLEAN, _id INTEGER PRIMARY KEY);");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}