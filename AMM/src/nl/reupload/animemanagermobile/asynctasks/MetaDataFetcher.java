/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.asynctasks;

import nl.reupload.animemanagermobile.MediaObject;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MangaUpdatesClient;
import nl.reupload.animemanagermobile.mangareader.sources.MangaUpdatesSource;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MetaDataFetcher extends AsyncTask<Integer, Integer, Boolean> {

	private Activity act;
	private MediaObject media;
	private String[] titles;
	private MangaUpdatesSource source;
	private int type;
	private String term;
	private String won;
	private ProgressDialog mInitializeDialog;
	private boolean noClose;
	private boolean noBan;

	
	public MetaDataFetcher(Activity act, String term2, MediaObject media, int type) {
		this.act = act;
		this.media = media;
		this.type = type;
		this.term = term2;
		noClose = false;
		noBan = false;
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (!DataManage.getBanned())
		{
			try {
			handleMetadata(type, media.getId());
			} catch (Exception e) { 
				//TODO handle this	
			}
		}
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
//      setProgressPercent(progress[0]);
//		Toast.makeText(act, "link " + progress[0] + " found!", Toast.LENGTH_SHORT).show();
		switch (progress[0]) {
			case (0):
				if (!noClose)
					act.recreate();
				break;
			case (1):
				AlertDialog.Builder alert = new AlertDialog.Builder(act);
		  	    TextView tv = new TextView(act);
		  	    tv.setText("did you mean \"" + won.split("\\^")[0] + "\" instead of \"" + (term!=null?term:media.getTitle()) + "\"?");
		          alert.setView(tv);
		          alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		          	@Override
		          	public void onClick(DialogInterface dialog, int whichButton) {
		          		if (media.getId() == 0)
		              		DataManage.register(media.getTitle(), Integer.parseInt(won.split("\\^")[1]), act, type);
		              	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(won.split("\\^")[1]), act);
		              	if (!noClose)
		              		act.recreate();
		              }
		          });
	
		          alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
		        	  @Override
		        	  public void onClick(DialogInterface dialog, int whichButton) {
//		        		DialogFragment newFragment = new ShowPickerDialog();
//		              	((ShowPickerDialog) newFragment).setData(titles);
//		              	((ShowPickerDialog) newFragment).setTitle(media.getTitle());
//		              	((ShowPickerDialog) newFragment).setCallback(listen);
//		              	newFragment.show
		        		AlertDialog.Builder alert = new AlertDialog.Builder(act);
		        		alert.setItems(getCleanedItems(), new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which >= 0) {
									
									if (media.getId() == 0)
						      		DataManage.register(media.getTitle(), Integer.parseInt(titles[which].split("\\^")[1]), act, type);
									try {
									if (type == 1 || type == 2 || type == 5)
										AniDBWrapper.grabAnimeMetadata(Integer.parseInt(titles[which].split("\\^")[1]), act);
									else 
										MangaUpdatesClient.grabMangaMetadata(Integer.parseInt(titles[which].split("\\^")[1]), act);
									} catch (Exception e){}
									publishProgress(0);
								}
								Log.d("select", which+"");
								
							}
						});
		  		        alert.show();
		              	
		              }

					private CharSequence[] getCleanedItems() {
						String[] names = new String[titles.length];
						if (titles.length > 0) {
							int i = 0;
							for (String item : titles) {
								names[i] = item.split("\\^")[0];
								i++;
							}
						}
						return names;
					}
		          });
		          alert.show();
		          break;
			case (2):
				mInitializeDialog = ProgressDialog.show(act, "", "Fetching metadata for " + media.getTitle() +". Please wait...", true);
				break;
			case (3):
				mInitializeDialog.dismiss();
		}
		
  }

  protected void onPostExecute(Long result) {
	  if (noBan) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //ban prevention
	  }
//      showDialog("Downloaded " + result + " bytes");
  }
  
  private void handleMetadata(final int type, int id) {
	  	publishProgress(2);
		boolean manga = (type==3||type==4||type==6);
		if (id == 0)
			if (term == null)
				if (manga)
					titles = MangaUpdatesClient.getMostLikelyID(media.getTitle(), false).toArray(new String[0]);
				else
					titles = AniDBWrapper.getMostLikelyID(media.getTitle(), false).toArray(new String[0]);
			else
				if (manga)
					titles = MangaUpdatesClient.getMostLikelyID(term, false).toArray(new String[0]);
				else
					titles = AniDBWrapper.getMostLikelyID(term, false).toArray(new String[0]);
		else
			titles = new String[]{media.getTitle()+"^"+id};
		for (String item : titles) {
			Log.d("heh", item);
		}
      if (titles.length < 1) {
      	//title = MangaUpdatesClient.getMostLikelyID(media.getTitle(), true).toArray(new String[0]);
    	  if (!manga)
    		  titles = AniDBWrapper.getMostLikelyID(media.getTitle(), true).toArray(new String[0]);
    	  else
    		  return;
//  		Toast toast = Toast.makeText(this, "Sorry, no metadata found for " + media.getTitle(), duration);
//  		toast.show();
//  		mInitializeDialog.dismiss();
      }
      if (titles.length == 1) {
      	Log.d("new", "ling 21");
      	Log.d("nya", titles[0]);
      	if (media.getId() == 0)
      		DataManage.register(media.getTitle(), Integer.parseInt(titles[0].split("\\^")[1]), act, type);
      	if (manga)
      		MangaUpdatesClient.grabMangaMetadata(Integer.parseInt(titles[0].split("\\^")[1]), act);
      	else
      		AniDBWrapper.grabAnimeMetadata(Integer.parseInt(titles[0].split("\\^")[1]), act);
//      	this.recreate();
//      	fetch.dismiss(true);
      	publishProgress(0);
      }
      else {
//      	temptype = 1;
      	QGramsDistance metric = new QGramsDistance();
      	float high = 0;
      	String win = "";
      	for (String item : titles) {
      		float sim = metric.getSimilarity(item.split("\\^")[0], media.getTitle());
      		if (sim > high) {
      			high = sim;
      			win = item;
      		}
      		Log.d("sim", "simmilarity between " + media.getTitle() + " and " + item.split("\\^")[0] + " is " + sim);
      	}
      	won = win.toString();
      	publishProgress(1);
			
				
      }	
      publishProgress(3);
	}

	public void noClose() {
		this.noClose = true;
	}

	public void banPrevent() {
		noBan = true;
	}
}
