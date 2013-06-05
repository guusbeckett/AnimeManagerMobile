package animemanagermobile.reupload.nl.mangareader.sources;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import animemanagermobile.reupload.nl.data.AniDBWrapper;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.data.MangaUpdatesClient;

public class MangaUpdatesSource extends SourceClient {
	
	private int id;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getActualURL(String url) {
		String contents = handleGet(url, true);
		return null;
	}

	@Override
	public String getDownloadPageFromScannerSite(String url) {
		String contents = handleGet(url, true);
		return null;
	}

	@Override
	public String getFileURLFromDownloadPage(String url) {
		String contents = handleGet(url, true);
		return null;
	}
	
	public String handleGet(String url, Boolean desktop) {
		String contents = null;
		try {
			contents = EntityUtils.toString(AniDBWrapper.httpget(url, true));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contents;
	}



}
