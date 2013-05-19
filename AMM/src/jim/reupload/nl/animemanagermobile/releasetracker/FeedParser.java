package jim.reupload.nl.animemanagermobile.releasetracker;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import jim.reupload.nl.animemanagermobile.R;
import jim.reupload.nl.animemanagermobile.data.AMMDatabase;
import jim.reupload.nl.animemanagermobile.data.AniDBWrapper;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Vibrator;
import android.renderscript.RenderScript.Priority;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class FeedParser {
	
	private Cursor c;
	private SQLiteDatabase ammDatabase;

	public void test(Context ctx) {
		/*String feed = null;
		  try {
			feed = EntityUtils.toString(AniDBWrapper.httpget("http://fffansubs.org/?feed=rss2", false));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (feed != null)
			parseFeed(feed, ctx);*/
		SQLiteOpenHelper ammData = new AMMDatabase(ctx);
		ammDatabase =  ammData.getWritableDatabase();
		Log.d("data", ammDatabase.getPath());
		c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL", "LastPost"}, null, null, null, null, null);
		while (c.moveToNext()) {
			Log.d("nee", "data run 1");
			String feed = null;
			String url = c.getString(c.getColumnIndex("RSS_URL"));
			try {
				feed = EntityUtils.toString(AniDBWrapper.httpget(url, false));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Cursor c2 = ammDatabase.query("Registered", new String[]{"Name", "Keyword"}, "Tracking='1' AND Subber='" + c.getString(c.getColumnIndex("Name")) + "'", null, null, null, null);
			if (c2.getCount() >= 1) {
				String[] titles = new String[c2.getCount()];
				int i = 0;
				while (c2.moveToNext()) {
					if (!c2.getString(c2.getColumnIndex("Keyword")).equals(""))
						titles[i] = c2.getString(c2.getColumnIndex("Keyword"));
					else
						titles[i] = c2.getString(c2.getColumnIndex("Name"));
					i++;
				}
				parseFeed(feed, c.getString(c.getColumnIndex("Name")), titles, ctx);
			}
		}
		ammDatabase.close();
	}

	public void parseFeed(String rawfeed, String feedname, String[] titles, Context ctx) {
		String hash = c.getString(c.getColumnIndex("LastPost"));
		boolean first = true;
		for (String item : rawfeed.split("<item>")) {
//			if (item.contains("Nyarlko")) {
//				parseItem(item, ctx);
//				Log.d("ge", item);
			
//			}
			if (getHash(item.split("<title>")[1].split("</title>")[0]).equals(hash)) {
				Log.d("break", "ya blew it");
				break;
			}
			if (first) {
				ContentValues cv = new ContentValues();
		    	cv.put("Name", c.getString(c.getColumnIndex("Name")));
		    	cv.put("RSS_URL", c.getString(c.getColumnIndex("RSS_URL")));
		    	cv.put("LastPost", getHash(item.split("<title>")[1].split("</title>")[0]));
		    	ammDatabase.delete("Subteams", "Name='"+ c.getString(c.getColumnIndex("Name")) +"'", null);
				ammDatabase.insert("Subteams", null, cv);
			}
			first = false;
			for (String title : titles) {
				if (item.contains(title)) {
					parseItem(item, ctx);
				}
			}
		}
	}
	
	private String getHash(String string) {
		MessageDigest md = null;
		// TODO Auto-generated method stub
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(md.digest(string.getBytes()));
		//return null;
	}

	public void parseItem(String rawitem, Context ctx) {
		Log.d("ge", "ads");
		if (rawitem.contains("<title>") && rawitem.contains("<description>")) {
			String title = rawitem.split("<title>")[1].split("</title>")[0];
			//String description = rawitem.split("<description>")[1].split("</description>")[0];
			notifyUser(title, "moii", ctx);
		}
		
	}
	
	public void notifyUser(String title, String content, Context ctx) {
		Log.d("lel", "mnee");
		NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Release!")
                .setContentText(title)
				.setPriority(NotificationCompat.PRIORITY_HIGH);
        // Creates an explicit intent for an Activity in your app
        //Intent resultIntent = new Intent(this, ResultActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(this.getClass());
        // Adds the Intent that starts the Activity to the top of the stack
        //stackBuilder.addNextIntent(resultIntent);
        /*PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);*/
        NotificationManager mNotificationManager =
            (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify((int) (Math.random()*100), mBuilder.build());
        Vibrator vibrate =  (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		vibrate.vibrate(400);
		
	}
}
