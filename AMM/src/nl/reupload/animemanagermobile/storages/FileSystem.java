package nl.reupload.animemanagermobile.storages;

public abstract class FileSystem {

	
	public abstract boolean writeStringToFile(String data, String filename);
	
	public abstract String readStringFromFile(String filename);
	
	public abstract boolean isOnline();
	
}
