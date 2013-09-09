/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile;

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
