/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
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
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.http.AndroidHttpClient;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;


public class UDPAniDBWrapper {

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
		        			title.add(StringEscapeUtils.unescapeHtml4(item.split("CDATA\\[")[1].split("\\]")[0]+"^"+id));
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
	    String raw = "";
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
		raw = new String(baf.toByteArray());
//	    Log.d("wow", raw);
	    if (raw.contains("Banned")) {
	    	Toast.makeText(act, "aniDB ban detected", Toast.LENGTH_SHORT);
	    	DataManage.setBanned(true);
	    	return;
	    }
	    if (!temp)
	    	//DataManage.writeToExternal(parsed, "anime"+aid+".xml", act);
	    	parseAndPutInMetaDB(raw, act, aid);
	    else
	    	DataManage.writeToCache(raw, "/tempmetadata/tempanime"+aid+".xml", act);
	}
	
	private static void parseAndPutInMetaDB(String raw, Activity act, int ID) {
		String[] vals = parseAniDBStream(raw);
		ContentValues cv = new ContentValues();
		cv.put("Type", DataManage.watchingAnime);
		cv.put("Source", DataManage.srcAniDB);
		cv.put("ID", ID);
		cv.put("SeriesType", vals[0]);
		cv.put("EPorCHcnt", vals[1]);
		cv.put("StartDate", vals[2].replace("-", ""));
//		cv.put("EndDate", vals[3].replace("-", ""));
		cv.put("Titles", vals[3]);
		cv.put("Related", vals[4]);
		cv.put("Similar", vals[5]);
		cv.put("Reccomendations", "null");
		cv.put("URL", vals[6]);
		cv.put("Creators", vals[7]);
		cv.put("Description", vals[8]);
//		cv.put("Ratings", vals[8]); TODO find out more about ratings
		cv.put("Picture", vals[10]);
		cv.put("Categories", vals[11]);
		cv.put("Resources", "null");
		cv.put("Tags", vals[13]);
		cv.put("Characters", vals[14]);
		cv.put("EPsorCHs", vals[15]);
		SQLiteOpenHelper metadataDB = new MetadataDatabase(act);
		SQLiteDatabase metaDB =  metadataDB.getWritableDatabase();
		Cursor c = metaDB.query("MetaData", new String[]{"_id"}, "Type="+ DataManage.watchingAnime  +" AND Source='"+ DataManage.srcAniDB +"' AND ID='" + ID + "'", null, null, null, null);
		if (c.getCount()>0) {
			c.moveToFirst();
			cv.put("_id", c.getInt(c.getColumnIndex("_id")));
		}
		metaDB.replace("MetaData", null, cv);
		metadataDB.close();
			
		
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
	
	public static String[] parseAniDBStream(String stream) {
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
		if (!stream.contains("Banned")) {
			data[0] = stream.split("<type>")[1].split("</type>")[0];
			Log.d("check", "0");
			data[1] = stream.split("<episodecount>")[1].split("</episodecount>")[0];
			if (stream.contains("<startdate>"))
				data[2] = stream.split("<startdate>")[1].split("</startdate>")[0];
			else
				data[2] = "";
			Log.d("check", "2");
			boolean first = true;
			for (String item : stream.split("<titles>")[1].split("</titles>")[0].split("<title")) {
				if (item.contains("</title>")) {
					if (!item.isEmpty()) {
						if (first) {
//							data[3] = item.split("\">")[1].split("</title")[0]+"^"+item.split("\"")[1]+"^"+item.split("\"")[3];
							data[3] = "[lang=\"" + item.split("\"")[1] + "\" type=\"" + item.split("\"")[3] + "\"] " + item.split("\">")[1].split("</title")[0];
							first = false;
						}
						else
//							data[3] += "\n"+item.split("\">")[1].split("</title")[0]+"^"+item.split("\"")[1]+"^"+item.split("\"")[3];
							data[3] += "| [lang=\"" + item.split("\"")[1] + "\" type=\"" + item.split("\"")[3] + "\"] " + item.split("\">")[1].split("</title")[0];
						Log.d("title", item);
					}
				}
			}
			if (stream.contains("<relatedanime>")) {
//				data[4] = stream.split("<relatedanime>")[1].split("</relatedanime>")[0];
				first = true;
				for (String show : stream.split("<relatedanime>")[1].split("</relatedanime>")[0].split("<anime id=\"")) {
					if (show.contains("anime")) {
						String show2 = show.split(">")[1].split("</anime")[0];
						if (!show2.split("\\^")[0].equals("")) {
							String[] bits = show.split("\"");
							if (first) {
								data[4] = "[ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" ] " + show2;
								first = false;
							}
							else
								data[4] += "| [ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" ] " + show2;
						}
					}
				}
			}
			
			else
				data[4] = "";
			if (stream.contains("<similaranime>")) {
				first = true;
				for (String show : stream.split("<similaranime>")[1].split("</similaranime>")[0].split("<anime id=\"")) {
					if (show.contains("anime")) {
						String show2 = show.split(">")[1].split("</anime")[0];
						if (!show2.split("\\^")[0].equals("")) {
							String[] bits = show.split("\"");
							if (first) {
								data[5] = "[ id=\"" + bits[0] + "\" approval=\"" + bits[2] + "\" total=\"" + bits[4] + "\" ] " + show2;
								first = false;
							}
							else
								data[5] += "| [ id=\"" + bits[0] + "\" approval=\"" + bits[2] + "\" total=\"" + bits[4] + "\" ] " + show2;
						}
					}
				}
			}
			Log.d("check", "3");
			if (stream.contains("<url>"))
				data[6] = stream.split("<url>")[1].split("</url>")[0];
			else
				data[6] = "";
			if (stream.contains("<creators>")) {
//				data[7] = stream.split("<creators>")[1].split("</creators>")[0];
				first = true;
				for (String show : stream.split("<creators>")[1].split("</creators>")[0].split("<name id=\"")) {
					if (show.contains("name")) {
						String show2 = show.split(">")[1].split("</name")[0];
						if (!show2.split("\\^")[0].equals("")) {
							String[] bits = show.split("\"");
							if (first) {
								data[7] = "[ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" ] " + show2;
								first = false;
							}
							else
								data[7] += "| [ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" ] " + show2;
						}
					}
				}
			}
			else
				data[7] = "";
			data[8] = stream.split("<description>")[1].split("</description>")[0];
			data[9] = stream.split("<ratings>")[1].split("</ratings>")[0];
			Log.d("check", "4");
			data[10] = stream.split("<picture>")[1].split("</picture>")[0];
			if (stream.contains("<categories>")) {
//				data[11] = stream.split("<categories>")[1].split("</categories>")[0];
				first = true;
				for (String show : stream.split("<categories>")[1].split("</categories>")[0].split("<category id=\"")) {
					if (show.contains("name")) {
						String show2 = show.split("name>")[1].split("</")[0];
						String show3 = show.split("description>")[1].split("</")[0].replace("&#13;", "\n");
						if (!show2.equals("")) {
							String[] bits = show.split("\"");
							if (first) {
								data[11] = "[ id=\"" + bits[0] + "\" parentid=\"" + bits[2] + "\" hentai=\"" + bits[4] + "\" weight=\"" + bits[6] + "\" title=\"" + show2 + "\" ] " + show3;
								first = false;
							}
							else
								data[11] += "| [ id=\"" + bits[0] + "\" parentid=\"" + bits[2] + "\" hentai=\"" + bits[4] + "\" weight=\"" + bits[6] + "\" title=\"" + show2 + "\" ] " + show3;
						}
					}
				}
			}
			else
				data[11] = "";
//			data[12] = stream.split("<resources>")[1].split("</resources>")[0];
			if (stream.contains("<tags>")) {
//				data[13] = stream.split("<tags>")[1].split("</tags>")[0];
				first = true;
				for (String show : stream.split("<tags>")[1].split("</tags>")[0].split("<tag id=\"")) {
					if (show.contains("name")) {
						String show2 = show.split("name>")[1].split("</")[0];
						String show4 = show.split("count>")[1].split("</")[0];
						String show3;
						if (show.contains("<description>"))
							show3 = show.split("description>")[1].split("</")[0];
						else
							show3 = "";
						if (!show2.equals("")) {
							String[] bits = show.split("\"");
							if (first) {
								data[13] = "[ id=\"" + bits[0] + "\" approval=\"" + bits[2] + "\" spoiler=\"" + bits[4] + "\" localspoiler=\"" + bits[6] + "\" globalspoiler=\"" + bits[8] + "\" update=\"" + bits[10] + "\" title=\"" + show2 + "\" count=\"" + show4 + "\" ] " + show3 + " ";
								first = false;
							}
							else
								data[13] += "| [ id=\"" + bits[0] + "\" parentid=\"" + bits[2] + "\" hentai=\"" + bits[4] + "\" localspoiler=\"" + bits[6] + "\" globalspoiler=\"" + bits[8] + "\" update=\"" + bits[10] + "\" title=\"" + show2 + "\" count=\"" + show4 + "\" ] " + show3 + " ";
						}
					}
				}
			}
			else
				data[13] = "";
			Log.d("check", "5");
			if (stream.contains("<characters>")) {
//				data[14] = stream.split("<characters>")[1].split("</characters>")[0];
				first = true;
				for (String show : stream.split("<characters>")[1].split("</characters>")[0].split("<character id=\"")) {
					if (show.contains("name")) {
						String show2 = show.split("name>")[1].split("</")[0];
						String show4 = "";
						if (show.contains("<rating"))
							show4 = show.split("rating")[1].split(">")[1].split("</")[0];
						String show3 = "";
						if (show.contains("<description"))
							show3 = show.split("description>")[1].split("</")[0];
						String show5 = show.split("charactertype")[1].split(">")[1].split("</")[0];
						String show6 = show.split("gender>")[1].split("</")[0];
						String show7 = "";
						if (show.contains("<picture>"))
							show7 = show.split("picture>")[1].split("</")[0];
						String show8 = "";
						if (show.contains("<seiyuu"))
							show8 = show.split("seiyuu")[1].split(">")[1].split("</")[0];
						if (!show2.equals("")) {
							String[] bits = show.replace(show3, "").split("\"");
							if (bits.length >= 9) {
								//TODO fix tempfix
								if (show.contains("votes=")) {
									if (first) {
										data[14] = "[ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" update=\"" + bits[4] + "\" votes=\"" + bits[6] + "\" charatypeid=\"" + bits[8] + (show.contains("<seiyuu")?"\" seiyuuid=\"" + bits[((show.contains("<rating"))?10:9)] + ((show.contains("seiyuupic="))?"\" seiyuupic=\"" + bits[((show.contains("<rating"))?12:11)]:""):"") + "\" name=\"" + show2 + "\" rating=\"" + show4 + "\" gender=\"" + show6 + "\" charactertype=\"" + show5 + "\" picture=\"" + show7 + "\" seiyuu=\"" + show8 + "\" ] " + show3;
										first = false;
									}
									else
										data[14] += "| [ id=\"" + bits[0] + "\" type=\"" + bits[2] + "\" update=\"" + bits[4] + "\" votes=\"" + bits[6] + "\" charatypeid=\"" + bits[8] + (show.contains("<seiyuu")?"\" seiyuuid=\"" + bits[((show.contains("<rating"))?10:9)] + ((show.contains("seiyuupic="))?"\" seiyuupic=\"" + bits[((show.contains("<rating"))?12:11)]:""):"")  + "\" name=\"" + show2 + "\" rating=\"" + show4 + "\" gender=\"" + show6 + "\" charactertype=\"" + show5 + "\" picture=\"" + show7 + "\" seiyuu=\"" + show8 + "\" ] " + show3;
								}
							}
						}
					}
				}
			}
//			data[15] = stream.split("<episodes>")[1].split("</episodes>")[0];
			first = true;
			for (String show : stream.split("<episodes>")[1].split("</episodes>")[0].split("<episode id=\"")) {
				if (show.contains("epno")) {
					String show2 = show.split("epno")[1].split(">")[1].split("</")[0];
					String show4 = show.split("length>")[1].split("</")[0];
					String show3;
					if (show.contains("<airdate>"))
						show3 = show.split("airdate>")[1].split("</")[0];
					else
						show3 = "";
					String show5;
					if (show.contains("<rating>"))
						show5 = show.split("rating>")[1].split("</")[0];
					else
						show5 = "";
					if (!show2.equals("")) {
						String[] bits = show.split("\"");
						String titles = null;
						for (String titleraw : show.split("<title")) {
							if (titleraw.contains("xml")) {
								if (titles==null)
									titles= titleraw.split("\"")[1] + "^" + titleraw.split("\">")[1].split("</title")[0];
								else
									titles+= "\n" + titleraw.split("\"")[1] + "^" + titleraw.split("\">")[1].split("</title")[0];
							}
						}
						if (first) {
							data[15] = "[ id=\"" + bits[0] + "\" update=\"" + bits[2] + "\" eptype=\"" + bits[4] + ((bits.length >= 6)?"\" ratingvotes=\"" + bits[6]:"") + "\" rating=\"" + show5 + "\" epno=\"" + show2 + "\" length=\"" + show4 + "\" airdate=\"" + show3 + "\" ] " + titles;
							first = false;
						}
						else
							data[15] += "| [ id=\"" + bits[0] + "\" update=\"" + bits[2] + "\" eptype=\"" + bits[4] + ((bits.length >= 6)?"\" ratingvotes=\"" + bits[6]:"") + "\" rating=\"" + show5 + "\" epno=\"" + show2 + "\" length=\"" + show4 + "\" airdate=\"" + show3 + "\" ] " + titles;
					}
				}
			}
			Log.d("check", "6");
			return data;
		} else return null;
//		} else {
//			//This is a ban, remove file
//			if (!temp) {
//				DataManage.deleteFromExternal("anime"+aid+".xml", act);
//			} else { DataManage.deleteFromCache("/tempmetadata/tempanime"+aid+".xml", act); }
//			//stop window from opening and return null
////			act.finish();
//			return null;
//		}
	}
	
	public static String[] parseAniDBfile(int aid, boolean temp, Activity act) {
		if (doesAniDBfileExist(aid, act) || temp) {
			String stream = null;
			if (!temp)
				stream = DataManage.readFromExternal("anime"+aid+".xml", act);
			else
				stream = DataManage.readFromCache("/tempmetadata/tempanime"+aid+".xml", act);
			String[] vals = parseAniDBStream(stream);
			if (vals!=null)
				return vals;
			else if (temp)
				DataManage.deleteFromCache("/tempmetadata/tempanime"+aid+".xml", act);
			return null;
		}
		else
			return null;
	}
	
	public static String[] parseAniDBfile(int aid, Activity act) {
//		return parseAniDBfile(aid, false, act); 
		//this is only called when it is already registered so it is safe to load from DB
		String[] data = new String[18];
		SQLiteOpenHelper metadataDB = new MetadataDatabase(act);
		SQLiteDatabase metaDB =  metadataDB.getWritableDatabase();
		Cursor c = metaDB.query("MetaData", new String[]{"SeriesType", "EPorCHcnt", "StartDate", "Titles", "Related", "Similar", "URL", "Creators", "Description", "Ratings", "Picture", "Categories", "Resources", "Tags", "Characters", "EPsorCHs"}, "Type="+ DataManage.watchingAnime  +" AND Source='"+ DataManage.srcAniDB +"' AND ID='" + aid + "'", null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			for (int i=0; i<16; i++) {
				data[i] = c.getString(i);
			}
		}
		metadataDB.close();
		return data;
	}
	
	public static void fetchImage(String filename, boolean temp, Activity act, String string) {
		String url = "http://img7.anidb.net/pics/anime/" + filename;
	    HttpEntity resEntityGet = httpget(url, false);
	    if (filename == null) {
	    	return;
	    }
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
		if (rawEpisodeList == null)
			return null;
		String nearest = null;
		for (String item : rawEpisodeList.split("\\| \\[ id=\"")) {
			if (item.contains("type=\"1\"")) {
				String itemdate = item.split("airdate=\"")[1].split("\"")[0];
				if ((Integer.parseInt(itemdate.split("-")[0]) >= Integer.parseInt(date.split("-")[0])) && (Integer.parseInt(itemdate.split("-")[1]) >= Integer.parseInt(date.split("-")[1])) && (Integer.parseInt(itemdate.split("-")[2]) >= Integer.parseInt(date.split("-")[2])))  {
					if (nearest == null) {
						nearest = item.split("epno=\"")[1].split("\"")[0] + "^" + itemdate;
					}
					else {
						if (Integer.parseInt(itemdate.split("-")[0]) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0]) && (Integer.parseInt(itemdate.split("-")[1])) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0]) && (Integer.parseInt(itemdate.split("-")[2])) <= Integer.parseInt(nearest.split("\\^")[1].split("-")[0])) {
							nearest = item.split("epno=\"")[1].split("\"")[0] + "^" + itemdate;
						}
					}
						
				}
			}
		}
		//TODO do stuff
		return nearest;
	}
	
	public void listenUDP() {
		try{
			BufferedReader inFromUser =
			         new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket clientSocket = new DatagramSocket();
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			InetAddress IPAddress = InetAddress.getByName("api.anidb.net");
			String sentence = "PING";
			sendData = sentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9000);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + modifiedSentence);
			clientSocket.close(); // - See more at: http://systembash.com/content/a-simple-java-udp-server-and-udp-client/#sthash.bClEj1F3.dpuf
		} catch (Exception e) {}
	}

}
