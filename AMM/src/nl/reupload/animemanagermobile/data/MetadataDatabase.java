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