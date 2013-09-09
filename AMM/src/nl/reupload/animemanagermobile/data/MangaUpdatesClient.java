AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.util.Log;

public class MangaUpdatesClient {

	public static ArrayList<String> getMostLikelyID(String query, boolean desperate) {
		ArrayList<String> title = new ArrayList<String>();


		try {
			/*if (desperate)
				query = URLEncoder.encode(query, "UTF-8");
			else
				query = URLEncoder.encode("\\"+query, "UTF-8");*/
		    String postURL = "http://www.mangaupdates.com/search.html";
		    HttpEntity resEntityPost = AniDBWrapper.httpPostBakaUpdates(postURL, true, query);
		    if (resEntityPost != null) {  
		        // do something with the response
		        String[] response = EntityUtils.toString(resEntityPost).split("<h3>Releases</h3>")[1].split("<h3>Other Site Sections</h3>")[0].split("<tr >");
		        for (String item : response) {
		        	if (item.contains("series.html"))
		        		for (String line : item.split("\n")) {
		        			if (line.contains("series.html")) {
		        				String showname = line.split("Info\'>")[1].split("</a>")[0]+"^"+line.split("series.html\\?id=")[1].split("\' title")[0];
		        				try {
		        					int id = Integer.parseInt(showname.split("\\^")[1]);
			        				if (!title.contains(showname) && id > 0) {
			        					title.add(StringEscapeUtils.unescapeHtml4(showname));
			        				}
		        				} catch (Exception e) {
									// TODO: handle exception
								}
		        			}
		        		}
		        }
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return title;
	}

	public static void grabMangaMetadata(int id, boolean temp, Activity activ) {
		String getURL = "http://www.mangaupdates.com/series.html?id=" + id;
	    HttpEntity resEntityGet = AniDBWrapper.httpget(getURL, true);
	    String parsed = null;
		try {
			parsed = EntityUtils.toString(resEntityGet);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    parsed = convertHTMLToXML(parsed);
	    if (!temp)
	    	DataManage.writeToExternal(parsed, "manga"+id+".xml", activ);
	    else
	    	DataManage.writeToCache(parsed, "/tempmetadata/tempmanga"+id+".xml", activ);
		
	}
	public static void grabMangaMetadata(int id, Activity activ) {
		grabMangaMetadata(id, false, activ);
	}

	private static String convertHTMLToXML(String unparsed) {
		// TODO Auto-generated method stub
		String XML= "";
		unparsed = unparsed.split("<span class=\"releasestitle tabletitle\">")[1].split("<!-- End:Series Info-->")[0];
		unparsed.replace("\n", "");
		XML+="<title>"+unparsed.split("</span>")[0]+"</title>";
		XML+="\n<description>"+unparsed.split("Description")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div>")[0]+"</description>";
		XML+="\n<type>"+unparsed.split("<b>Type</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</type>";
		XML+="\n<titles>"+unparsed.split("<b>Associated Names</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</titles>";
		XML+="\n<scanlators>"+unparsed.split("<b>Groups Scanlating</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</scanlators>";
		if (unparsed.contains("<b>Image</b>"))
			XML+="\n<picture>"+unparsed.split("<b>Image</b>")[1].split("<div class=\"sContent\"")[1].split("src=\'")[1].split("\'")[0]+"</picture>";
		XML+="\n<genre>"+unparsed.split("<b>Genre</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</genre>";
		XML+="\n<categories>"+unparsed.split("<b>Categories</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</categories>";
		//XML+="\n<catagories>"+parsed.split("<b>Categories</b>")[1].split("<div class=\"sContent\"")[1].split(">")[1].split("</div")[0]+"</catagories>";
		//Log.d("lel", parsed.split("<b>Author")[1]);
		return XML;
	}

	public static boolean doesMangaUpdatesfileExist(int id, Activity activ) {
		return DataManage.doesExternalFileExist("manga"+id+".xml", activ);
	}
	
	public static String[] parseMangaUpdatesfile(int id, boolean temp, Activity act) {
		if (doesMangaUpdatesfileExist(id, act) || temp) {
			String stream = null;
			if (!temp)
				stream = DataManage.readFromExternal("manga"+id+".xml", act);
			else
				stream = DataManage.readFromCache("/tempmetadata/tempmanga"+id+".xml", act);
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
			//data[1] = stream.split("<episodecount>")[1].split("</episodecount>")[0];
			//data[2] = stream.split("<startdate>")[1].split("</startdate>")[0];
			//Log.d("check", "2");
			/*for (String item : stream.split("<titles>")[1].split("</titles>")[0].split("<title")) {
			//	data[3] += "\n"+item.split("\">")[1].split("</title>")[0];
			}*/
			
			//data[4] = "relatedanime";
			//data[5] = "similaranime";
			//Log.d("check", "3");
			//data[6] = stream.split("<url>")[1].split("</url>")[0];
			//data[7] = stream.split("<creators>")[1].split("</creators>")[0];
			data[8] = stream.split("<description>")[1].split("</description")[0];
			//data[9] = stream.split("<ratings>")[1].split("</ratings>")[0];
			Log.d("check", "4");
			data[10] = stream.split("<picture>")[1].split("</picture>")[0];
			Log.d("check", "5");
			if (stream.contains("<categories>"))
				data[11] = stream.split("<categories>")[1].split("</categories>")[0];
			else
				data[11] = "No content";
			//data[12] = stream.split("<resources>")[1].split("</resources>")[0];
			//data[13] = stream.split("<tags>")[1].split("</tags>")[0];
			data[13] = "";
			//Log.d("check", "5");
			//data[14] = stream.split("<characters>")[1].split("</characters>")[0];
			data[14] = "";
			//data[15] = stream.split("<episodes>")[1].split("</episodes>")[0];
			data[15] = "";
			Log.d("check", "6");
			return data;
		}
		else
			return null;
	}
	public static String[] parseMangaUpdatesfile(int id, Activity act) {
		return parseMangaUpdatesfile(id, false, act);
	}
	
	public static void fetchImage(String filename, boolean temp, Activity act) {
		String url = filename;
	    HttpEntity resEntityGet = AniDBWrapper.httpget(url, true);
	    Log.d("filename", filename.split("/")[filename.split("/").length-1]);
	    Log.d("filename", "test");
	    try {
	    	if (!temp)
	    		resEntityGet.writeTo(DataManage.openOutputStreamToExternal(new File(act.getExternalFilesDir(null), "/images/"), filename.split("/")[filename.split("/").length-1]));
	    	else
	    		resEntityGet.writeTo(DataManage.openOutputStreamToCache(new File("/tempimages/"), filename.split("/")[filename.split("/").length-1], act));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fetchImage(String filename, Activity act) {
		fetchImage(filename, false, act);
	}

	public static String[][] getMangaReleaseInfo(int id) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		String getURL = "http://www.mangaupdates.com/releases.html?search="+id+"&stype=series&perpage=100";
	    HttpEntity resEntityGet = AniDBWrapper.httpget(getURL, true);
	    try {
			String toParse = EntityUtils.toString(resEntityGet).split("<!-- End:Center Content -->")[0].split("<!-- Start:Center Content -->")[1];
			for (String item : toParse.split("<tr>")) {
				if (item.contains("Group Info")) {
					for (String part : item.split("<tr >")) {
						String[] release = new String[5];
						int i = 0;
						for (String line : part.split("\n")) {
							if (line.contains("<td")) {
								try {
									release[i] = line.split("<td")[1].split(" >")[1].split("</td>")[0];
								} catch (Exception e) {
									
								}
								i++;
							}
						}
						list.add(release);
					}
				}
			}
			//Pattern.compile("<tag>(.+?)</tag>").matcher(toParse).find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return list.toArray(new String[0][]);
	    
	}
}
