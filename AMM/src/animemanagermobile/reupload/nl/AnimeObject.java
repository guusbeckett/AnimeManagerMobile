package animemanagermobile.reupload.nl;

public class AnimeObject extends MediaObject {

	public AnimeObject(String title, int progress, int total) {
		super(title, progress, total);
	}
	public AnimeObject(String title) {
		super(title, 0, 0);
	}
	public AnimeObject(String title, int progress) {
		super(title, progress, 0);
	}
	public String getWriteable() {
		// TODO Auto-generated method stub
		return getTitle() + " ep " + getProgress() + "...";
	}

}
