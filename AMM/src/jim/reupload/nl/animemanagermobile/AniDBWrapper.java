package jim.reupload.nl.animemanagermobile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.StrictMode;
import android.util.Log;


public class AniDBWrapper {

	public static ArrayList<String> getMostLikelyID(String query, boolean desperate) {
		ArrayList<String> title = new ArrayList<String>();


		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		try {
			if (desperate)
				query = URLEncoder.encode(query, "UTF-8");
			else
				query = URLEncoder.encode("\\"+query, "UTF-8");
		    String getURL = "http://anisearch.outrance.pl/index.php?task=search&query=" + query + "&langs=" + URLEncoder.encode("x-jat", "UTF-8");
		    HttpEntity resEntityGet = httpget(getURL);
		    if (resEntityGet != null) {  
		        // do something with the response
		        String[] response = EntityUtils.toString(resEntityGet).split("<anime");
		        for (String item : response) {
		        	if (item.contains("lang") && item.contains("aid")) {
		        		title.add(item.split("CDATA\\[")[1].split("\\]")[0]+"^"+item.split("aid=\"")[1].split("\"")[0]);
		        	}
		        	else
		        		Log.i("reject", item);
		        }
		        if (title.size() < 1) {
		        	//try everything
		        	/*for (String item : response) {
			        	if (item.contains("lang") && item.contains("aid")) {
			        		title.add(item.split("CDATA\\[")[1].split("\\]")[0]+"^"+item.split("aid=\"")[1].split("\"")[0]);
			        	}
		        	}*/
		        }
		       
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		
		// Do what you want with that stream
		return title;
	}
	
	public static void grabAnimeMetadata(int aid, Activity act) {
		String getURL = "http://api.anidb.net:9001/httpapi?request=anime&client=animemanagermob&clientver=1&protover=1&aid=" + aid;
	    HttpEntity resEntityGet = httpget(getURL);
	    String parsed = "";
	    InputStream ungzip = null;
	    try {
			ungzip = AndroidHttpClient.getUngzippedContent(resEntityGet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedInputStream buf;
		ByteArrayBuffer baf = null;
		 try {
			buf = new BufferedInputStream(ungzip);
			int current = 0;
			baf = new ByteArrayBuffer(1024);
			while ((current = buf.read()) != -1)  {
			    baf.append((byte) current);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parsed = new String(baf.toByteArray());
	    Log.d("wow", parsed);
		DataManage.writeToExternal(parsed, aid+".xml", act);
	}
	
	public static HttpEntity httpget(String url) {
		HttpResponse responseGet = null;
		try {
			HttpClient client = new DefaultHttpClient();  
		    HttpGet get = new HttpGet(url);
			responseGet = client.execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    return responseGet.getEntity(); 
	}
	
	public static boolean doesAniDBfileExist(int aid, Activity act) {
		return DataManage.doesExternalFileExist(aid+".xml", act);
	}
	
	public static String[] parseAniDBfile(int aid, Activity act) {
		if (doesAniDBfileExist(aid, act)) {
			String stream = DataManage.readFromExternal(aid+".xml", act);
			String[] data = new String[16];
			/**
			 * Explanation for this array:
			 * I parse the xml per category of data with a total of 15 categories
			 * 
			 * category 1 is type of the show
			 * category 2 is the episode count of the show
			 * category 3 is the starting date of the show
			 * category 4 are the titles of the show
			 * category 5 are related anime to the show
			 * category 6 are anime similar to the show
			 * category 7 is the official URL of the show
			 * category 8 is the description of the show
			 * category 9 are the ratings for the show
			 * category 10 is the picture of the show
			 * category 11 are the categories of the show
			 * category 12 are the resources of the show?
			 * category 13 are the tags of the show
			 * category 14 are the characters of the show
			 * category 15 are the episodes of the show
			 * 
			 */
			data[0] = stream.split("<type>")[1].split("</type>")[0];
			Log.d("check", "0");
			data[1] = stream.split("<episodecount>")[1].split("</episodecount>")[0];
			data[2] = stream.split("<startdate>")[1].split("</startdate>")[0];
			Log.d("check", "2");
			for (String item : stream.split("<titles>")[1].split("</titles>")[0].split("<title")) {
			//	data[3] += "\n"+item.split("\">")[1].split("</title>")[0];
			}
			
			data[4] = "relatedanime";
			data[5] = "similaranime";
			Log.d("check", "3");
			data[6] = stream.split("<url>")[1].split("</url>")[0];
			data[7] = stream.split("<creators>")[1].split("</creators>")[0];
			data[8] = stream.split("<description>")[1].split("</description>")[0];
			data[9] = stream.split("<ratings>")[1].split("</ratings>")[0];
			Log.d("check", "4");
			data[10] = stream.split("<picture>")[1].split("</picture>")[0];
			data[11] = stream.split("<categories>")[1].split("</categories>")[0];
			data[12] = stream.split("<resources>")[1].split("</resources>")[0];
			data[13] = stream.split("<tags>")[1].split("</tags>")[0];
			Log.d("check", "5");
			data[14] = stream.split("<characters>")[1].split("</characters>")[0];
			data[15] = stream.split("<episodes>")[1].split("</episodes>")[0];
			Log.d("check", "6");
			return data;
		}
		else
			return null;
	}
	
}
