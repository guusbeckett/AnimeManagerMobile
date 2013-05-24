package animemanagermobile.reupload.nl;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import animemanagermobile.reupload.nl.animefragmens.FragmentCategories;
import animemanagermobile.reupload.nl.animefragmens.FragmentCharacters;
import animemanagermobile.reupload.nl.animefragmens.FragmentDescription;
import animemanagermobile.reupload.nl.animefragmens.FragmentEpisodes;
import animemanagermobile.reupload.nl.animefragmens.FragmentGeneral;
import animemanagermobile.reupload.nl.animefragmens.FragmentMangaRead;
import animemanagermobile.reupload.nl.animefragmens.FragmentRelease;
import animemanagermobile.reupload.nl.animefragmens.FragmentTags;
import animemanagermobile.reupload.nl.data.AniDBWrapper;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.data.MangaUpdatesClient;
import animemanagermobile.reupload.nl.dialogs.ShowPickerDialog;
import animemanagermobile.reupload.nl.dialogs.ShowPickerDialog.OnDialogSelectorListener;

public class MediaPage extends FragmentActivity implements OnDialogSelectorListener {
	
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
	private String term;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new DataManage();
        point = getIntent().getIntExtra("point", 0);
        Log.d("chehck", "1");
        type = getIntent().getIntExtra("type", 0);
        switch (type) {
        	case (1):
        		media = data.getAnimeDetails(this, point);
        		break;
        	case (2):
        		media = data.getFullAnimeDetails(this, point);
        		break;
        	case (3):
        		media = data.getMangaDetails(this, point);
        		break;
        	case (4):
        		media = data.getFullMangaDetails(this, point);
        		break;
        }
        id = DataManage.getID(media.getTitle(), this, type);
        DataManage.clearCaches();
        Log.d("chehck", "2");
        if (id!=0)
        	media.setId(id);
        DataManage.cacheObject(media);
        Log.d("chechk", "3");
        if (id != 0) {
        	if (type == 1 || type == 2) {
	        	if (AniDBWrapper.doesAniDBfileExist(id, this)){
	        		metadataParse = AniDBWrapper.parseAniDBfile(id, this);
	        		metadataParse[16]=type+"";
	        		DataManage.cacheObject2(metadataParse);
	        	}
        	}
        	else {
        		if (MangaUpdatesClient.doesMangaUpdatesfileExist(id, this)){
	        		metadataParse = MangaUpdatesClient.parseMangaUpdatesfile(id, this);
	        		metadataParse[16]=type+"";
	        		DataManage.cacheObject2(metadataParse);
	        	}
        	}
        		
        }
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.slider);
        Log.d("chehck", "5");
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
        if (type == 1 || type == 2) {
        	mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_tags),
            		FragmentTags.class, null);
            mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_eps),
            		FragmentEpisodes.class, null);
            mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_chars),
            		FragmentCharacters.class, null);
            if (type == 1)
        	mTabsAdapter.addTab(actionBar.newTab().setText("Releases"),
        			FragmentRelease.class, null);
        }
        else if (type == 3 || type == 4)
        	mTabsAdapter.addTab(actionBar.newTab().setText("Manga Reader"),
        			FragmentMangaRead.class, null);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_view, menu);
        if (id != 0) {
        	menu.add(0, 7, 0, "Refresh Metadata");
        	menu.add(0, 5, 0, "Unlink Metadata"); 
        }
        menu.add(0, 6, 0, "Delete series"); 
        menu.add(0, 8, 0, "Search custom"); 
		return true;

    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.fetch_metadata:
                //newGame();
            	switch (type) {
            		case (1):
            			handleAnimeMetadata(null);
            			break;
            		case (2):
            			handleAnimeMetadata(null);
            			break;
            		case (3):
            			handleMangaMetadata(null);
            			break;
            		case (4):
            			handleMangaMetadata(null);
            			break;
            	}
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
                    public void onClick(DialogInterface dialog, int whichButton) {
                      term = et1.getText().toString();
                      if (term != null) {
      	            	switch (type) {
      		        		case (1):
      		        			handleAnimeMetadata(term);
      		        			break;
      		        		case (2):
      		        			handleAnimeMetadata(term);
      		        			break;
      		        		case (3):
      		        			handleMangaMetadata(term);
      		        			break;
      		        		case (4):
      		        			handleMangaMetadata(term);
      		        			break;
      	            	}
                      }
                      }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel.
                      }
                    });
                alert.show();
               
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void handleMangaMetadata(String term) {
		AlertDialog mInitializeDialog = ProgressDialog.show(this, "", "Fetching metadata. Please wait...", true);
		if (media.getId() == 0)
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
        		DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this, 1);
        	MangaUpdatesClient.grabMangaMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
        	mInitializeDialog.dismiss();
        	this.recreate();
        }
        else {
        	temptype = 1;
        	mInitializeDialog.dismiss();
        	DialogFragment newFragment = new ShowPickerDialog();
        	((ShowPickerDialog) newFragment).setData(title);
        	((ShowPickerDialog) newFragment).setTitle(media.getTitle());
            newFragment.show(getSupportFragmentManager(), "show");
            //this.recreate();
            
        }
		
	}

	private void destroyMedia() {
		// TODO Auto-generated method stub
		data.DeleteAnimeDetails(this, point);
	}

	private void destroyMetadata() {
		// TODO Auto-generated method stub
		if (id!=0) {
			DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/images/" + metadataParse[10], this);
			DataManage.deleteExternalFile(this.getExternalFilesDir(null) + "/" + Integer.toString(id)+".xml", this);
			DataManage.unregister(media.getTitle(), this);
		}
	}

	private void handleAnimeMetadata(String term) {
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
        	if (id == 0)
        		DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this, 0);
        	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
        	this.recreate();
        }
        else {
        	temptype = 0;
        	DialogFragment newFragment = new ShowPickerDialog();
        	((ShowPickerDialog) newFragment).setData(title);
        	((ShowPickerDialog) newFragment).setTitle(media.getTitle());
            newFragment.show(getSupportFragmentManager(), "show");
            //this.recreate();
            
        }
        
	}

	@Override
	public void onSelectedOption(int selectedIndex) {
		if (selectedIndex != 0) {
			DataManage.register(media.getTitle(), Integer.parseInt(title[selectedIndex].split("\\^")[1]), this, temptype);
			if (temptype == 0)
				AniDBWrapper.grabAnimeMetadata(Integer.parseInt(title[selectedIndex].split("\\^")[1]), this);
			else
				MangaUpdatesClient.grabMangaMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
			this.recreate();
        	finish();
		}
		Log.d("select", selectedIndex+"");
		
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


	public int getCount() {
		return mTabs.size();
	}

	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}


	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}


	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}


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

	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {}
}
