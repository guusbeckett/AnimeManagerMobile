package nl.reupload.animemanagermobile.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;

import android.content.Entity;
import android.util.Log;

public class VideoFetcher {
	
	public static String getURLOP (String query) {
		return getVideoURLFromYoutube(query + " OP HD", false)[0];
	}


	public static String[] getVideoURLFromYoutube(String query, boolean hdonly) {
		query = query.replace("Opening", "OP").replace("Ending", "ED");
		Log.d("YouTube URL finder", "Searching for " + query + ".hd is " + ((hdonly)?"":"not ") + "enforced");
		ArrayList<String> urls = new ArrayList<String>();
		try {
			String result = EntityUtils.toString(AniDBWrapper.httpget("https://gdata.youtube.com/feeds/api/videos?q=" + URLEncoder.encode(query, "UTF-8"), false));
			for (String item : result.split("<entry>")) {
				if (item.contains("<media:player url='")) {
					if (hdonly) {
						if (!item.contains("<yt:hd/>"))
							continue;
					}
					urls.add(item.split("<media:player url='")[1].split("'/>")[0]);//+"^"+item.split("<title type='text'>")[1].split("</title>")[0]);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urls.toArray(new String[0]);
		
	}
}
