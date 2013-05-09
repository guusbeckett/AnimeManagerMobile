package jim.reupload.nl.animemanagermobile;

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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
		    HttpClient client = new DefaultHttpClient();  
		    String getURL = "http://anisearch.outrance.pl/index.php?task=search&query=" + query + "&langs=" + URLEncoder.encode("x-jat", "UTF-8");
		    HttpGet get = new HttpGet(getURL);
		    HttpResponse responseGet = client.execute(get);  
		    HttpEntity resEntityGet = responseGet.getEntity();  
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
	
}
