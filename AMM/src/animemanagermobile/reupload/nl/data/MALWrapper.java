package animemanagermobile.reupload.nl.data;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.content.Entity;
import android.util.Log;

public class MALWrapper {

	private static final String baseURL =  "http://mal-api.com/";
	
	public static void getAnimeList(String username) {
		try {
			String raw = EntityUtils.toString(AniDBWrapper.httpget(baseURL+"/animelist/"+username, false));
			for (String line : raw.split("\"id\"")) {
				if (line.contains("title")) {
					for (String item : line.split(",\""))
						if (item.contains("\"title\""))
							Log.d("title", item.split("\"")[3]);
						else if (item.contains("\"watched_episodes\""))
							Log.d("watched_eps", item.split(":")[1]);
						else if (item.contains("\"watched_status\""))
							Log.d("watched_status", item.split("\"")[3]);
					}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
