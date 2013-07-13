package nl.reupload.animemanagermobile.storages;

import nl.reupload.animemanagermobile.data.MALWrapper;

public class MyAnimeListFS extends FileSystem {

	@Override
	public boolean writeStringToFile(String data, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String readStringFromFile(String filename) {
		// TODO Auto-generated method stub
		MALWrapper.getAnimeList("loljimlol");
		return null;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

}
