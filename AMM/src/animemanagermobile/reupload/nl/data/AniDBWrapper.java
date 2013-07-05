package animemanagermobile.reupload.nl.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.StrictMode;
import android.util.Log;


public class AniDBWrapper {

	public static ArrayList<String> getMostLikelyID(String query, boolean desperate) {
		ArrayList<String> title = new ArrayList<String>();


		try {
			if (desperate)
				query = URLEncoder.encode(query, "UTF-8");
			else
				query = URLEncoder.encode("\\"+query, "UTF-8");
		    String getURL = "http://anisearch.outrance.pl/index.php?task=search&query=" + query + "&langs=" + URLEncoder.encode("x-jat", "UTF-8");
		    HttpEntity resEntityGet = httpget(getURL, false);
		    if (resEntityGet != null) {  
		        // do something with the response
		        String[] response = EntityUtils.toString(resEntityGet).split("<anime");
		        for (String item : response) {
		        	if (item.contains("lang") && item.contains("aid")) {
		        		String id = item.split("aid=\"")[1].split("\"")[0];
		        		if (!id.equals("0")) {
		        			title.add(item.split("CDATA\\[")[1].split("\\]")[0]+"^"+id);
		        		}
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
	
	public static void grabAnimeMetadata(int aid, boolean temp, Activity act) {
		String getURL = "http://api.anidb.net:9001/httpapi?request=anime&client=animemanagermob&clientver=1&protover=1&aid=" + aid;
	    HttpEntity resEntityGet = httpget(getURL, false);
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
	    if (!temp)
	    	DataManage.writeToExternal(parsed, "anime"+aid+".xml", act);
	    else
	    	DataManage.writeToCache(parsed, "/tempmetadata/tempanime"+aid+".xml", act);
	}
	
	public static void grabAnimeMetadata(int aid, Activity act) {
		grabAnimeMetadata(aid, false, act);
	}
	
	public static HttpEntity httpget(String url, boolean fakeDesktop) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		HttpResponse responseGet = null;
		try {
			HttpClient client = new DefaultHttpClient();  
		    HttpGet get = new HttpGet(url);
		    if (fakeDesktop)
		    	get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
			responseGet = client.execute(get);
		} catch (ClientProtocolException e) {
			Log.e("AMM", "Client Protocal Exception at HTTP GET");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("AMM", "IOException at HTTP GET");
		}  
		if (responseGet != null)
			return responseGet.getEntity(); 
		else return null;
	}
	
	public static HttpEntity httpPostBakaUpdates(String url, boolean fakeDesktop, String query) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		    }
		HttpResponse responseGet = null;
		try {
			HttpClient client = new DefaultHttpClient();  
		    HttpPost post  = new HttpPost(url);
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("x","6"));
		      nameValuePairs.add(new BasicNameValuePair("y","8"));
		      nameValuePairs.add(new BasicNameValuePair("search",query));
		      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    if (fakeDesktop)
		    	post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
			responseGet = client.execute(post);
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
		return DataManage.doesExternalFileExist("anime"+aid+".xml", act);
	}
	
	public static String[] parseAniDBfile(int aid, boolean temp, Activity act) {
		if (doesAniDBfileExist(aid, act) || temp) {
			String stream = null;
			if (!temp)
				stream = DataManage.readFromExternal("anime"+aid+".xml", act);
			else
				stream = DataManage.readFromCache("/tempmetadata/tempanime"+aid+".xml", act);
			String[] data = new String[18];
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
			if (stream.contains("<startdate>"))
				data[2] = stream.split("<startdate>")[1].split("</startdate>")[0];
			else
				data[2] = "";
			Log.d("check", "2");
			for (String item : stream.split("<titles>")[1].split("</titles>")[0].split("<title")) {
			//	data[3] += "\n"+item.split("\">")[1].split("</title>")[0];
			}
			if (stream.contains("<relatedanime>"))
				data[4] = stream.split("<relatedanime>")[1].split("</relatedanime>")[0];
			else
				data[4] = "";
			data[5] = "similaranime";
			Log.d("check", "3");
			if (stream.contains("<url>"))
				data[6] = stream.split("<url>")[1].split("</url>")[0];
			else
				data[6] = "";
			if (stream.contains("<creators>"))
				data[7] = stream.split("<creators>")[1].split("</creators>")[0];
			else
				data[7] = "";
			data[8] = stream.split("<description>")[1].split("</description>")[0];
			data[9] = stream.split("<ratings>")[1].split("</ratings>")[0];
			Log.d("check", "4");
			data[10] = stream.split("<picture>")[1].split("</picture>")[0];
			data[11] = stream.split("<categories>")[1].split("</categories>")[0];
			data[12] = stream.split("<resources>")[1].split("</resources>")[0];
			if (stream.contains("<tags>"))
				data[13] = stream.split("<tags>")[1].split("</tags>")[0];
			else
				data[13] = "";
			Log.d("check", "5");
			if (stream.contains("<characters>"))
				data[14] = stream.split("<characters>")[1].split("</characters>")[0];
			data[15] = stream.split("<episodes>")[1].split("</episodes>")[0];
			Log.d("check", "6");
			return data;
		}
		else
			return null;
	}
	
	public static String[] parseAniDBfile(int aid, Activity act) {
		return parseAniDBfile(aid, false, act);
	}
	
	public static void fetchImage(String filename, boolean temp, Activity act, String string) {
		String url = "http://img7.anidb.net/pics/anime/" + filename;
	    HttpEntity resEntityGet = httpget(url, false);

	    try {
	    	if (!temp)
	    		resEntityGet.writeTo(DataManage.openOutputStreamToExternal(new File(act.getExternalFilesDir(null), "/images/" + string), filename));
	    	else
	    		resEntityGet.writeTo(DataManage.openOutputStreamToCache(new File("/tempimages/" + string), filename, act));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fetchImage(String filename, Activity act, String string) {
		fetchImage(filename, false, act, string);
	}
	
	public static String findEpisodeNearestAfterDate(String date, String rawEpisodeList) {
		String nearest = null;
		for (String item : rawEpisodeList.split("<episode id")) {
			if (item.contains("<airdate>") && item.contains("<epno type=\"1\">")) {
				String itemdate = item.split("<airdate>")[1].split("</airdate>")[0];
				if ((Integer.parseInt(itemdate.split("-")[0]) >= Integer.parseInt(date.split("-")[0])) && (Integer.parseInt(itemdate.split("-")[1]) >= Integer.parseInt(date.split("-")[1])) && (Integer.parseInt(itemdate.split("-")[2]) >= Integer.parseInt(date.split("-")[2])))  {
					if (nearest == null) {
						nearest = item.split("<epno type=\"1\">")[1].split("</epno>")[0] + "^" + itemdate;
					}
					else {
						if (Integer.parseInt(itemdate.split("-")[0]) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0]) && (Integer.parseInt(itemdate.split("-")[1])) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0]) && (Integer.parseInt(itemdate.split("-")[2])) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0])) {
							nearest = item.split("<epno type=\"1\">")[1].split("</epno>")[0] + "^" + itemdate;
						}
					}
						
				}
			}
		}
		//TODO do stuff
		return nearest;
	}
	
	
}
