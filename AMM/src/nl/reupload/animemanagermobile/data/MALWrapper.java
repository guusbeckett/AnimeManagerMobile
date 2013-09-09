AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.data;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

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
