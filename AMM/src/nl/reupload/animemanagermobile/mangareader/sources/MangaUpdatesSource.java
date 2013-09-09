AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile.mangareader.sources;

import java.io.IOException;

import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MangaUpdatesClient;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.util.Log;

public class MangaUpdatesSource extends SourceClient {
	
	private static final int PHPBBPAGE = 200;
	private static final int WordPressSITE1 = 201;
	private int id;
	private Object sources;
	private int type;
	private String url;
	public MangaUpdatesSource(String title, Activity act) {
		super(title, act);
		id = DataManage.getID(title, this.getAct(), 3);
		if (id == 0)
			id = DataManage.getID(title, this.getAct(), 4);
		if (id != 0) {
			makeXML(MangaUpdatesClient.getMangaReleaseInfo(id));
		}
	}

	@Override
	public void makeXML(String[][] mangaReleaseInfo) {
		String xmlout = "<xml>\n\t<releases>";
		for (String[] lel : mangaReleaseInfo) {
			if (lel[0] != null && lel[3] != null && lel[4] != null) {
				xmlout+= "\n\t\t<release>" +
						"\n\t\t\t<chapter>" + lel[3] + "</chapter>" +
						"\n\t\t\t<date>" + lel[0] + "</date>" +
						"\n\t\t\t<releasername>" + lel[4].split(">")[1].split("</a")[0] + "</releasername>" +
						"\n\t\t\t<releaserinfourl>" + lel[4].split("href=\'")[1].split("\'")[0] + "</releaserinfourl>" +
						"\n\t\t</release>";
			}
		}
		xmlout+="\n\t</releases>\n</xml>";
		DataManage.writeToExternal(xmlout, "/mangareleases/"+DataManage.getHash(getTitle())+".xml", getAct());
		
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean[] availableFor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScannerByChapter(Integer chap) {
		String raw = DataManage.readFromExternal("/mangareleases/"+DataManage.getHash(title)+".xml", getAct());
		for (String item : raw.split("<release>")) {
			if (item.contains("<chapter>")) {
//				sources.put(item.split("<chapter>")[1].split("</chapter>")[0], item.split("<releasername>")[1].split("</releasername>")[0]);
				try {
					int ch = Integer.parseInt(item.split("<chapter>")[1].split("</chapter>")[0]);
					if (ch == chap) {
						return item.split("<releaserinfourl>")[1].split("</releaserinfourl>")[0];
					}
//					if (ch > total)
//						total = ch;
				} catch (Exception e) {}
			}
		}
		return null;
	}

	@Override
	public String getActualURL(String url) {
		String contents = handleGet(url, true);
		contents = contents.split("<!-- Start:Center Content -->")[1].split("<!-- End:Center Content -->")[0];
		return contents.split("<U>Website</U>")[1].split("href=\'")[1].split("\'")[0];
//		for (String lel : contents.split("\n"))
//			Log.d("lel", lel);
//		return null;
	}

	@Override
	public String getDownloadPageFromScannerSite(String url) {
		String contents = handleGet(url, true);
		if (contents.contains("phpBB"))
			BBforumHandler(contents, url);
		else if (contents.contains("WordPress.com"))
			WordpressHandler(contents, url);
//		for (String lel : contents.split("\n"))
//			Log.d("lel", lel);
		return null;
	}
	
	
	
	public String WordpressHandler(String contents, String url2) {
		this.type = WordPressSITE1;
		this.url = url2;
		return "";
		
	}

	public String BBforumHandler(String raw, String url) {
		for (String lel : raw.split("\n")) {
			if (lel.contains("Translation") || lel.contains("Scanlation"))
				if (!lel.contains("facebook")) {
					Log.d("lel", lel);
					lel = url + lel.split("href=\"")[1].split("\"")[0].replace("&amp;", "&").replace("./", "");
					Log.d("lel", lel);
					String contents = handleGet(lel, true);
					for (String item : contents.split("\n"))
						if (item.contains(title)) {
							Log.d("lel", item);
							item = url + item.split("href=\"")[1].split("\"")[0].replace("&amp;", "&").replace("./", "");
							Log.d("lel", item);//Item has content here
							this.type = PHPBBPAGE;
							this.url = item;//workaround for weird null bug
							return item.toString(); //suddenly NULL
						}
					
				}
		}
		return "";
	}

	@Override
	public String getFileURLFromDownloadPage(String url, String chap) {
		if (this.url != null)//workaround for weird null bug
			url = this.url;
		switch (this.type) {
			case (PHPBBPAGE):
				String contents = handleGet(url, true);
				for (String item : contents.split("\n"))
					if (item.contains(chap) && item.contains("Chapter"))
						for (String part : item.split("<br"))
							if (part.contains("<a")) {
								if (part.split("<a")[0].contains(chap) && part.split("<a")[0].contains("Chapter")) {
									Log.d("item", part);
									for (String link : part.split("<a")) {
										if (!link.contains("torrent") && link.contains("href"))
											return link.split("href=\"")[1].split("\"")[0];
									}
								}
							}
				break;
			case (WordPressSITE1):
				String contents1 = handleGet(url, true);
				for (String item : contents1.split("<div class=\"post")) {
//					Log.d("item", item);
					if (item.contains(chap) && item.contains("Chapter") || item.contains("Download")) {
						Log.d("hasChap", item);
						if (item.contains("<a")) {
							Log.d("haslink", item);
							if (item.split("<a")[0].contains(chap) && item.split("<a")[0].contains("Chapter")) {
								Log.d("hasactualink", item);
								for (String link : item.split("<a")) {
									if (!link.contains("torrent") && link.contains("href"))
										return link.split("href=\"")[1].split("\"")[0];
								}
							}
						}
					}
				}
				break;
		}
		return "";
	}
	
	public String handleGet(String url, Boolean desktop) {
		String contents = null;
		if (url != null) {
			try {
				contents = EntityUtils.toString(AniDBWrapper.httpget(url, true));
			} catch (ParseException e) {
				Log.e("AMM", "Failure to parse URL");
			} catch (IOException e) {
				Log.e("AMM", "IO Error at HTTP GET");
			}
		}
		return contents;
	}



}
