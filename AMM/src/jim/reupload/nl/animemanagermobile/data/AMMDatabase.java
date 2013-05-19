package jim.reupload.nl.animemanagermobile.data;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS Registered (Name TEXT, ID INTEGER, Tracking BOOLEAN, Subber TEXT, Keyword TEXT);");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}