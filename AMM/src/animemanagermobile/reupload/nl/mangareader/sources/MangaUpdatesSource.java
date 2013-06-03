package animemanagermobile.reupload.nl.mangareader.sources;

import android.app.Activity;
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
			MangaUpdatesClient.getMangaReleaseInfo(id);
		}
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
	public void fetchFromSource() {
		// TODO Auto-generated method stub
	}

}
