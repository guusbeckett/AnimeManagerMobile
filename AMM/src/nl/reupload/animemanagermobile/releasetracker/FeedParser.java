/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.releasetracker;

import java.io.IOException;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.AMMDatabase;
import nl.reupload.animemanagermobile.data.AniDBWrapper;

public class FeedParser {
	
	private Cursor c;
	private SQLiteDatabase ammDatabase;
	private String feedname;
	private final String TAG = "AMM FeedParser";

	public void test(Context ctx) {
		Log.d(TAG, "starting");
		if (isOnline(ctx)) {
			SQLiteOpenHelper ammData = new AMMDatabase(ctx);
			ammDatabase =  ammData.getWritableDatabase();
			Log.d("data", ammDatabase.getPath());
			c = ammDatabase.query("Subteams", new String[]{"Name", "RSS_URL", "LastPost"}, null, null, null, null, null);
			while (c.moveToNext()) {
				Log.d(TAG, "doing a feed");
				if (c.isAfterLast() == true) {
					Log.d(TAG, "done handling feeds, there were " + c.getCount() + " feeds");
					break;
				}
				String feed = null;
				String url = c.getString(c.getColumnIndex("RSS_URL"));
				if (url.equals(""))
					continue;
				if (!url.contains("http://"))
					url = "http://" + url;
				Log.d("nee", "data run "+url);
				try {
					feed = EntityUtils.toString(AniDBWrapper.httpget(url, false));
				} catch (ParseException e) {
					Log.e(TAG, "ParseException occured");
				} catch (IOException e) {
					Log.e(TAG, "IOException occured");
				}
				feedname = c.getString(c.getColumnIndex("Name"));
				Log.d(TAG, feedname+" is being handled");
				Cursor c2 = ammDatabase.query("Registered", new String[]{"Name", "Keyword"}, "Tracking='1' AND Subber='" + feedname + "'", null, null, null, null);
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
				else {
					Log.d(TAG, feedname+" has no items");
					parseFeed(feed, c.getString(c.getColumnIndex("Name")), null, ctx);
				}
			}
			ammDatabase.close();
		}
		else
			Log.d(TAG, "service stopped because android reported being offline");
	}

	public void parseFeed(String rawfeed, String feedname, String[] titles, Context ctx) {
		String hash = c.getString(c.getColumnIndex("LastPost"));
		boolean first = true;
		String[] items =  reverseArray(rawfeed.split("<item>"), true);
		for (String item : items) {
			if (item.contains("<description>")) {
				if (getHash(item.split("<title>")[1].split("</title>")[0]).equals(hash)) {
					Log.d("break", "ya blew it");
					Log.d("lel", item.split("<title>")[1].split("</title>")[0]);
					break;
				}
				if (first) {
					ContentValues cv = new ContentValues();
			    	cv.put("Name", c.getString(c.getColumnIndex("Name")));
			    	cv.put("RSS_URL", c.getString(c.getColumnIndex("RSS_URL")));
			    	cv.put("LastPost", getHash(item.split("<title>")[1].split("</title>")[0]));
			    	ammDatabase.delete("Subteams", "Name='"+ c.getString(c.getColumnIndex("Name")) +"'", null);
					ammDatabase.insert("Subteams", null, cv);
					Log.d("lel", item.split("<title>")[1].split("</title>")[0]);
					first = false;
				}
				
				if (titles != null) {
					boolean parsed = false;
					for (String title : titles) {
						if (item.contains(title)) {
							parseItem(item, ctx, true);
							parsed = true;
						}
						if (!parsed)
							parseItem(item, ctx, false);
					}
				}
				else
					parseItem(item, ctx, false);
			}
		}
	}

	private String[] reverseArray(String[] array, boolean removeFirst) {
		int size = array.length-((removeFirst)?1:0);
		String[] newArray = new String[size];
		boolean first = true;
		for (int i=((removeFirst)?-1:0); i<size; i++) {
			if (first&&removeFirst) {
				first = false;
				continue;
			}
			else {
				newArray[i] = array[size-i];
			}
		}
		return newArray;
	}
	
	public boolean isOnline(Context ctx) {
	    ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	private String getHash(String string) {
		return Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
		//return null;
	}

	public void parseItem(String rawitem, Context ctx, boolean notify) {
		Log.d("ge", "ads");
		if (rawitem.contains("<title>") && rawitem.contains("<description>")) {
			String title = rawitem.split("<title>")[1].split("</title>")[0];
			ContentValues cv = new ContentValues();
			cv.put("Title", title);
			cv.put("Description", rawitem.split("<description>")[1].split("</description>")[0]);
			if (rawitem.contains("<link>"))
				cv.put("Link", rawitem.split("<link>")[1].split("</link>")[0]);
			if (rawitem.contains("<author>"))
				cv.put("Author", rawitem.split("<author>")[1].split("</author>")[0]);
			if (rawitem.contains("creator>"))
				cv.put("Author", rawitem.split("creator>")[1].split("</")[0]);
			String cats = null;
			for (String cat : rawitem.split("<category>")) {
				if (cat.contains("<category>"))
					if (cats == null)
						cats=rawitem.split("<category>")[1].split("</category>")[0];
					else
						cats+=" "+rawitem.split("<category>")[1].split("</category>")[0];
			}
			if (rawitem.contains("<pubDate>"))
				cv.put("pubDate", rawitem.split("<pubDate>")[1].split("</pubDate>")[0]);
			if (rawitem.contains("<enclosure>"))
				cv.put("Enclosure", rawitem.split("<enclosure>")[1].split("</enclosure>")[0]);
			if (rawitem.contains("<guid>"))
				cv.put("guid", rawitem.split("<guid")[1].split(">")[1].split("</guid")[0]);
			if (rawitem.contains("<source>"))
				cv.put("Source", rawitem.split("<source>")[1].split("</source>")[0]);
			if (rawitem.contains("<comments>"))
				cv.put("Comments", rawitem.split("<comments>")[1].split("</comments>")[0]);
			cv.put("Important", notify);
			cv.put("feedname", feedname);
			if (rawitem.contains("<content"))
				cv.put("Content", rawitem.split("<content:encoded>")[1].split("</content:encoded>")[0]);
			cv.put("Read", false);
			ammDatabase.insert("Feeds", null, cv);
			if (notify)
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
