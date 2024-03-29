/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile;

import java.io.File;
import java.util.ArrayList;

import nl.reupload.animemanagermobile.animefragmens.FragmentCategories;
import nl.reupload.animemanagermobile.animefragmens.FragmentCharacters;
import nl.reupload.animemanagermobile.animefragmens.FragmentDescription;
import nl.reupload.animemanagermobile.animefragmens.FragmentEpisodes;
import nl.reupload.animemanagermobile.animefragmens.FragmentGeneral;
import nl.reupload.animemanagermobile.animefragmens.FragmentMangaRead;
import nl.reupload.animemanagermobile.animefragmens.FragmentRelatedSeries;
import nl.reupload.animemanagermobile.animefragmens.FragmentRelease;
import nl.reupload.animemanagermobile.animefragmens.FragmentTags;
import nl.reupload.animemanagermobile.asynctasks.MetaDataFetcher;
import nl.reupload.animemanagermobile.data.AMMDatabase;
import nl.reupload.animemanagermobile.data.AniDBWrapper;
import nl.reupload.animemanagermobile.data.DataManage;
import nl.reupload.animemanagermobile.data.MangaUpdatesClient;
import nl.reupload.animemanagermobile.data.MetadataDatabase;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MediaPage extends FragmentActivity {
	
	private MediaObject media;
	private LinearLayout linlay;
	private int id;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private String[] title;
	private String[] metadataParse;
	private DataManage data;
	private int point;
	private int type;
	private int temptype;
	private MediaObject[] list;
	private String term;
	private boolean tempMode;
	private int origintype;
	private boolean standAlone;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        standAlone = getIntent().getBooleanExtra("standalone", false);
        tempMode = getIntent().getBooleanExtra("tempMode", false);
        Log.e("lel", "tempMode is "+tempMode);
        data = new DataManage();
        if (!tempMode && !standAlone)
        	list = DataManage.getList();
        else if (standAlone) {
        	int listNum = getIntent().getIntExtra("list", 0);
        	if (listNum != 0) 
        		list = data.getMediaList(this, listNum);
        	else {
        		Log.e("list 0 is rejected", "sorry");
        		finish();
        	}
        }
        point = getIntent().getIntExtra("point", 0);
        Log.d("chehck", "1");
        if (!standAlone)
        	type = getIntent().getIntExtra("type", 0);
        else
        	type = getIntent().getIntExtra("list", 0);
        if (!tempMode && !standAlone)
        	media = list[point];
        else if (standAlone) {
        	id = getIntent().getIntExtra("mediaID", 0);
        	String title = null;
        	if (id != 0) 
	        	title = DataManage.getTitleFromRegisteredID(id, this);
        	else {
        		title = getIntent().getStringExtra("title");
        		id = DataManage.getID(title, this, type);
        	}
        	for (MediaObject object : list) {
        		if (object.getTitle().equals(title)) {
        			media = object;
        			break;
        		}
        	}
        	if (media == null) {
    			Log.d("title", title + " is reject and has no results");
    			finish();
    		}
        }
        else
        	media = new MediaObject("");
        media.setType(type);
        if (!tempMode && !standAlone)
        	id = DataManage.getID(media.getTitle(), this, type);
        else  if (tempMode){
        	origintype = getIntent().getIntExtra("origin", 0);
        	media.setTitle(getIntent().getStringExtra("title"));
        	id = getIntent().getIntExtra("mediaID", 0);
        	if (id == 0)
        		finish();
        }
        DataManage.clearCaches();
        Log.d("chehck", "2");
       	media.setId(id);
        DataManage.cacheObject(media);
        Log.d("chechk", "3");
        if (!tempMode) {
	        if (id != 0) {
	        	if (type == 1 || type == 2 || type == 5) {
//		        	if (AniDBWrapper.doesAniDBfileExist(id, this)){
		        		metadataParse = AniDBWrapper.parseAniDBfile(id, this);
		        		if (metadataParse != null) {
			        		metadataParse[16]=type+"";
			        		metadataParse[17]="0";
			        		DataManage.cacheObject2(metadataParse);
		        		}
//		        	}
	        	}
	        	else {
	        		if (MangaUpdatesClient.doesMangaUpdatesfileExist(id, this)){
		        		metadataParse = MangaUpdatesClient.parseMangaUpdatesfile(id, this);
		        		if (metadataParse != null) {
			        		metadataParse[16]=type+"";
			        		metadataParse[17]="0";
			        		DataManage.cacheObject2(metadataParse);
		        		}
		        	}
	        	}
	        		
	        }
        }
        else {
        	switch (type) {
        		case (2):
        			if (!new File(getCacheDir(), "/tempmetadata/tempanime"+id+".xml").exists()) {
        				if (DataManage.isNetworkAvailable(this))
        					AniDBWrapper.grabAnimeMetadata(id, true, this);	
        				else {
        					Toast.makeText(this, "This feature requires an internet connection if the page has not been opened before", Toast.LENGTH_SHORT).show();
        					finish();
        					return;
        				}
        			}
	        		metadataParse = AniDBWrapper.parseAniDBfile(id, true, this);
	        		metadataParse[16]=type+"";
	        		metadataParse[17]="1";
	        		DataManage.cacheObject2(metadataParse);
        			break;
        		case (4):
        			if (!new File(getCacheDir(), "/tempmetadata/tempmanga"+id+".xml").exists()) {
        				if (DataManage.isNetworkAvailable(this))
        					MangaUpdatesClient.grabMangaMetadata(id, true, this);
        				else {
        					Toast.makeText(this, "This feature requires an internet connection if the page has not been opened before", Toast.LENGTH_SHORT).show();
        					finish();
        					return;
        				}
        			}
	        		metadataParse = MangaUpdatesClient.parseMangaUpdatesfile(id, true, this);
	        		metadataParse[16]=type+"";
	        		metadataParse[17]="1";
	        		DataManage.cacheObject2(metadataParse);
        			break;
        	}	
        }
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.slider);
        Log.d("chehck", "5");
        DataManage.setCached4(point);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_general),
                FragmentGeneral.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_desc),
        		FragmentDescription.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_cats),
        		FragmentCategories.class, null);
        if (type == 1 || type == 2 || type == 5) {
        	mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_tags),
            		FragmentTags.class, null);
            mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_eps),
            		FragmentEpisodes.class, null);
            mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_chars),
            		FragmentCharacters.class, null);
            mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_related),
            		FragmentRelatedSeries.class, null);
            if (type == 1)
        	mTabsAdapter.addTab(actionBar.newTab().setText("Releases"),
        			FragmentRelease.class, null);
        }
        else if (type == 3 || type == 4 && !tempMode)
        	mTabsAdapter.addTab(actionBar.newTab().setText("Manga Reader"),
        			FragmentMangaRead.class, null);
        
        Log.d("id", media.getId()+"");
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		if (!tempMode) {
	        getMenuInflater().inflate(R.menu.media_view, menu);
	        if (id != 0) {
	        	menu.add(0, 7, 0, "Refresh Metadata");
	        	menu.add(0, 5, 0, "Unlink Metadata"); 
	        }
	        switch (type) {
				case (1):
					menu.add(0, 9, 0, "Move to seen");
					break;
				case (2):
					menu.add(0, 9, 0, "Move to watching");
					break;
				case (3):
					menu.add(0, 9, 0, "Move to read");
					break;
				case (4):
					menu.add(0, 9, 0, "Move to seen reading");
					break;
	        }
	        menu.add(0, 6, 0, "Delete series"); 
	        menu.add(0, 8, 0, "Search custom"); 
	        if (metadataParse != null && (type==1||type==2||type==5))
	        	menu.add(0, 11, 0, "Change Title"); 
			return true;
		}
		else {
			switch (type) {
				case (2):
					menu.add(0, 1, 0, "Add to watching");
					menu.add(0, 2, 0, "Add to seen");
					menu.add(0, 3, 0, "Add to backlog");
					break;
				case (4):
					menu.add(0, 1, 0, "Add to reading");
					menu.add(0, 2, 0, "Add to read");
					menu.add(0, 3, 0, "Add to backlog");
					break;
			}
			return true;
		}

    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
		if (!tempMode) {
	        switch (item.getItemId()) {
	            case R.id.fetch_metadata:
	                //newGame();
	            	handleMetadata(null, type);
//	            	switch (type) {
//	            		case (1):
//	            			handleAnimeMetadata(null, type);
//	            			break;
//	            		case (2):
//	            			handleAnimeMetadata(null, type);
//	            			break;
//	            		case (3):
//	            			handleMangaMetadata(null, type);
//	            			break;
//	            		case (4):
//	            			handleMangaMetadata(null, type);
//	            			break;
//	            		case (5):
//	            			handleAnimeMetadata(null, type);
//	            		break;
//	            		case (6):
//	            			handleMangaMetadata(null, type);
//	            		break;
//	            	}
	                return true;
	            case android.R.id.home:
		            // app icon in action bar clicked; go home
	            	finish();
		            return true;
	            case 5:
	            	destroyMetadata();
	            	this.recreate();
	            	return true;
	            case 6:
	            	destroyMetadata();
	            	destroyMedia();
	            	this.finish();
	            	return true;
	            case 7:
	            	switch (type) {
		        		case (1):
		        			AniDBWrapper.grabAnimeMetadata(id, this);
		        			recreate();
		        			break;
		        		case (2):
		        			AniDBWrapper.grabAnimeMetadata(id, this);
		        			recreate();
		        			break;
		        		case (3):
		        			MangaUpdatesClient.grabMangaMetadata(id, this);
		        			recreate();
		        			break;
		        		case (4):
		        			MangaUpdatesClient.grabMangaMetadata(id, this);
		        			recreate();
		        			break;
	            	}
	            	return true;
	            case 8:
	            	term = null;
	            	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	            	alert.setTitle("Insert a searchterm: ");
	
	                LinearLayout ll = new LinearLayout(this);
	        	    ll.setOrientation(LinearLayout.VERTICAL);
	        	    final EditText et1 = new EditText(this);
	        	    et1.setHint("term");
	        	    ll.addView(et1);
	                alert.setView(ll);
	                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                    @Override
						public void onClick(DialogInterface dialog, int whichButton) {
	                      term = et1.getText().toString();
	                      if (term != null) {
	                    	  handleMetadata(term, type);
//	      	            	switch (type) {
//	      		        		case (1):
//	      		        			handleAnimeMetadata(term, type);
//	      		        			break;
//	      		        		case (2):
//	      		        			handleAnimeMetadata(term, type);
//	      		        			break;
//	      		        		case (3):
//	      		        			handleMangaMetadata(term, type);
//	      		        			break;
//	      		        		case (4):
//	      		        			handleMangaMetadata(term, type);
//	      		        			break;
//	      		        		case (5):
//	      		        			handleAnimeMetadata(term, type);
//	      		        			break;
//	      		        		case (6):
//	      		        			handleMangaMetadata(term, type);
//	      		        			break;
//	      	            	}
	                      }
	                      }
	                    });
	
	                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                      @Override
						public void onClick(DialogInterface dialog, int whichButton) {
	                        // Cancel.
	                      }
	                    });
	                alert.show();
	               
	            	return true;
	            case (9):
	            	MoveToOtherList();
	            	return true;
	            case (11):
	            	AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
	            	final String[] items = new String[metadataParse[3].split("|").length];
	            	int i = 0;
	            	for (String item1 : metadataParse[3].split("\\|")) {
	            		if (true)
	            		{
		            		items[i] = item1.split("\"\\] ")[1];
		            		i++;
	            		}
	            	}
	            	final Activity act = this;
	            	alert1.setItems(items, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							DataManage.unregister(media.getTitle(), type, act);
							media.setTitle(items[which]);
							DataManage.register(media.getTitle(), media.getId(), act, type);
							DataManage.setRefresh(true);
							data.writeSeriesDetails(act, media, point, type, list);
							recreate();
						}
					});
	            	alert1.show();	
	            	return true;
	            default:
	                return super.onOptionsItemSelected(item);
	            
	        }
		}
		boolean manga = (type==3||type==4||type==6);
		switch (item.getItemId()) {
			case (1):
				//Add to watching/reading
				addTempToPerm(((manga)?3:1));
				DataManage.register(media.getTitle(), media.getId(), this, ((manga)?3:1));
				DataManage.moveFile(getCacheDir()+"/tempmetadata/", ((manga)?"tempmanga":"tempanime")+media.getId()+".xml", getExternalFilesDir(null)+"/", ((manga)?"manga":"anime")+media.getId()+".xml");
				finish();
				return true;
			case (2):
				//Add to seen/read
				addTempToPerm(((manga)?4:2));
				DataManage.register(media.getTitle(), media.getId(), this, ((manga)?4:2));
				DataManage.moveFile(getCacheDir()+"/tempmetadata/", ((manga)?"tempmanga":"tempanime")+media.getId()+".xml", getExternalFilesDir(null)+"/", ((manga)?"manga":"anime")+media.getId()+".xml");
				finish();
				return true;
			case (3):
				//Add to backlog
				addTempToPerm(((manga)?6:5));
				DataManage.register(media.getTitle(), media.getId(), this, ((manga)?6:5));
				finish();
				return true;
			case android.R.id.home:
	            // app icon in action bar clicked; go home
	        	finish();
	            return true;
			default:
                return super.onOptionsItemSelected(item);
		}
    }
	
	public void addTempToPerm(int list) {
		MediaObject[] templist;
		templist = data.getMediaList(this, list);
		data.addNewSeries(this, media, list, templist);
		if (origintype == list)
			DataManage.setRefresh(true);
	}

	@Deprecated
	private void handleMangaMetadata(String term, final int type) {
		AlertDialog mInitializeDialog = ProgressDialog.show(this, "", "Fetching metadata. Please wait...", true);
		if (id == 0)
			if (term == null)
				title = MangaUpdatesClient.getMostLikelyID(media.getTitle(), false).toArray(new String[0]);
			else
				title = MangaUpdatesClient.getMostLikelyID(term, false).toArray(new String[0]);
		else
			title = new String[]{media.getTitle()+"^"+id};
		for (String item : title) {
			Log.d("heh", item);
		}
        if (title.length < 1) {
        	//title = MangaUpdatesClient.getMostLikelyID(media.getTitle(), true).toArray(new String[0]);
        	int duration = Toast.LENGTH_LONG;
    		Toast toast = Toast.makeText(this, "Sorry, no metadata found for " + media.getTitle(), duration);
    		toast.show();
    		mInitializeDialog.dismiss();
        }
        else if (title.length == 1) {
        	Log.d("new", "ling 21");
        	Log.d("nya", title[0]);
        	if (media.getId() == 0)
        		DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this, type);
        	MangaUpdatesClient.grabMangaMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
        	mInitializeDialog.dismiss();
        	this.recreate();
        }
        else {
//        	temptype = 1;
        	mInitializeDialog.dismiss();
        	QGramsDistance metric = new QGramsDistance();
        	float high = 0;
        	String win = "";
        	for (String item : title) {
        		float sim = metric.getSimilarity(item.split("\\^")[0], media.getTitle());
        		if (sim > high) {
        			high = sim;
        			win = item;
        		}
        		Log.d("sim", "simmilarity between " + media.getTitle() + " and " + item.split("\\^")[0] + " is " + sim);
        	}
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	    TextView tv = new TextView(this);
    	    tv.setText("did you mean \"" + win.split("\\^")[0] + "\"?");
            alert.setView(tv);
            final String won = win.toString();
            final Activity act = this;
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            	@Override
				public void onClick(DialogInterface dialog, int whichButton) {
            		if (media.getId() == 0)
                		DataManage.register(media.getTitle(), Integer.parseInt(won.split("\\^")[1]), act, type);
                	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(won.split("\\^")[1]), act);
                	act.recreate();
                }
            });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
				public void onClick(DialogInterface dialog, int whichButton) {
//                	DialogFragment newFragment = new ShowPickerDialog();
//                  	((ShowPickerDialog) newFragment).setData(title);
//                  	((ShowPickerDialog) newFragment).setTitle(media.getTitle());
//                    newFragment.show(getSupportFragmentManager(), "show");
                  }
                });
                alert.show();
            
        }
		
	}

	private void destroyMedia() {
		data.DeleteSeriesDetails(this, point, type, list);
		DataManage.setRefresh(true);
		SQLiteOpenHelper ammData = new AMMDatabase(this);
		SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
		ammDatabase.delete("Registered", "Name="+DatabaseUtils.sqlEscapeString(media.getTitle())+" AND Type='"+type+"'", null);
		finish();
	}
	private void MoveToOtherList() {
		data.DeleteSeriesDetails(this, point, type, list);
		MediaObject[] templist;
		boolean delete = false;
		int newType = 0;
		switch (type) {
			case (1):
				templist = data.getMediaList(this, 2);
				data.addNewSeries(this, media, 2, templist);
				delete = true;
				newType = 2;
				break;
			case (2):
				templist = data.getMediaList(this, 1);
				data.addNewSeries(this, media, 1, templist);
				delete = true;
				newType = 1;
			break;
			case (3):
				templist = data.getMediaList(this, 4);
				data.addNewSeries(this, media, 4, templist);
				delete = true;
				newType = 4;
			break;
			case (4):
				templist = data.getMediaList(this, 3);
				data.addNewSeries(this, media, 3, templist);
				delete = true;
				newType = 3;
			break;
		}
		if (delete) {
			SQLiteOpenHelper ammData = new AMMDatabase(this);
			SQLiteDatabase ammDatabase =  ammData.getWritableDatabase();
	//		Cursor c = ammDatabase.query("Registered", new String[]{"Name"}, "Name="+ DatabaseUtils.sqlEscapeString(media.getTitle())  +" AND Type='1'", null, null, null, null);
			ammDatabase.delete("Registered", "Name="+DatabaseUtils.sqlEscapeString(media.getTitle())+" AND Type='"+type+"'", null);
			ContentValues cv = new ContentValues();
			cv.put("Name", media.getTitle());
	    	cv.put("Type", newType);
	    	cv.put("ID", media.getId());
	    	cv.put("Tracking", false);
	    	cv.put("Subber", "");
	    	cv.put("Keyword", "");
			ammDatabase.insert("Registered", null, cv);
			DataManage.setRefresh(true);
		}
		finish();
	}

	private void destroyMetadata() {
		if (id!=0) {
			SQLiteOpenHelper metadatabase = new MetadataDatabase(this);
			SQLiteDatabase metaDB =  metadatabase.getWritableDatabase();
			int intID = 0;
			Cursor c = metaDB.query("MetaData", new String[]{"_id"}, "Type="+ DataManage.watchingAnime  +" AND Source='"+ DataManage.srcAniDB +"' AND ID='" + id + "'", null, null, null, null);
			if (c.getCount()>0) {
				c.moveToFirst();
				intID = c.getInt(c.getColumnIndex("_id"));
				metaDB.delete("MetaData", "Type="+ DataManage.watchingAnime  +" AND Source='"+ DataManage.srcAniDB +"' AND ID='" + intID + "'", null);
				DataManage.unregister(media.getTitle(), type, this);
			}
			
//			if (type == 1 || type == 2 || type == 5) {
//				DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/images/" + metadataParse[10], this);
//				DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/anime" + media.getId()+".xml", this);
//			}
//			else {
//				DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/images/" + metadataParse[10].split("/")[metadataParse[10].split("/").length-1], this);
//				DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/manga" + media.getId()+".xml", this);
//			}
			
			
		}
	}

	@Deprecated
	private void handleAnimeMetadata(String term, final int type2) {
		if (term == null)
			title = AniDBWrapper.getMostLikelyID(media.getTitle(), false).toArray(new String[0]);
		else
			title = AniDBWrapper.getMostLikelyID(term, false).toArray(new String[0]);
        if (title.length < 1) {
        	title = AniDBWrapper.getMostLikelyID(media.getTitle(), true).toArray(new String[0]);
        	/*int duration = Toast.LENGTH_LONG;
    		Toast toast = Toast.makeText(this, "Sorry, no metadata found for " + media.getTitle(), duration);
    		toast.show();*/
        }
        if (title.length == 1) {
        	if (media.getId() == 0)
        		DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this, type2);
        	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
        	this.recreate();
        }
        else {
        	QGramsDistance metric = new QGramsDistance();
        	float high = 0;
        	String win = "";
        	for (String item : title) {
        		float sim = metric.getSimilarity(item.split("\\^")[0], media.getTitle());
        		if (sim > high) {
        			high = sim;
        			win = item;
        		}
        		Log.d("sim", "simmilarity between " + media.getTitle() + " and " + item.split("\\^")[0] + " is " + sim);
        	}
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LinearLayout ll = new LinearLayout(this);
    	    TextView tv = new TextView(this);
    	    tv.setText("did you mean \"" + win.split("\\^")[0] + "\"?");
            alert.setView(tv);
            final String won = win.toString();
            final Activity act = this;
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            	@Override
				public void onClick(DialogInterface dialog, int whichButton) {
            		if (media.getId() == 0)
                		DataManage.register(media.getTitle(), Integer.parseInt(won.split("\\^")[1]), act, type2);
                	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(won.split("\\^")[1]), act);
                	act.recreate();
                }
            });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
				public void onClick(DialogInterface dialog, int whichButton) {
//                	DialogFragment newFragment = new ShowPickerDialog();
//                  	((ShowPickerDialog) newFragment).setData(title);
//                  	((ShowPickerDialog) newFragment).setTitle(media.getTitle());
//                    newFragment.show(getSupportFragmentManager(), "show");
                  }
                });
                alert.show();
        	
            //this.recreate();
            
        }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		DataManage.cacheObject2(metadataParse);
		DataManage.cacheObject(media);
		if (DataManage.getRefresh() && !tempMode)
			list = data.getMediaList(this, type);
	}

	public void handleMetadata(String term2, int type2) {
		final Activity act = this;
		MetaDataFetcher fetcher = new MetaDataFetcher(this, term2, media, type2);
		fetcher.execute(0);
		
	}
}
class TabsAdapter extends FragmentPagerAdapter
implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	private final Context mContext;
	private final ActionBar mActionBar;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	static final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(Class<?> _class, Bundle _args) {
			clss = _class;
			args = _args;
		}
	}

	public TabsAdapter(FragmentActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mActionBar = activity.getActionBar();
		mViewPager = pager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}

	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
		TabInfo info = new TabInfo(clss, args);
		tab.setTag(info);
		tab.setTabListener(this);
		mTabs.add(info);
		mActionBar.addTab(tab);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}


	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}


	@Override
	public void onPageScrollStateChanged(int state) {
	}


	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		Log.v("TAG", "clicked");
		Object tag = tab.getTag();
		for (int i=0; i<mTabs.size(); i++) {
			if (mTabs.get(i) == tag) {
				mViewPager.setCurrentItem(i);
			}
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {	
		Object tag = tab.getTag();
		for (int i=0; i<mTabs.size(); i++) {
			if (mTabs.get(i) == tag) {
				mViewPager.setCurrentItem(i);
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {}
}

