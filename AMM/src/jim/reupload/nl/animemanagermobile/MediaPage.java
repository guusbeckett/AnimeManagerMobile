package jim.reupload.nl.animemanagermobile;

import java.util.ArrayList;

import jim.reupload.nl.animemanagermobile.animefragmens.FragmentCategories;
import jim.reupload.nl.animemanagermobile.animefragmens.FragmentCharacters;
import jim.reupload.nl.animemanagermobile.animefragmens.FragmentDescription;
import jim.reupload.nl.animemanagermobile.animefragmens.FragmentEpisodes;
import jim.reupload.nl.animemanagermobile.animefragmens.FragmentGeneral;
import jim.reupload.nl.animemanagermobile.animefragmens.FragmentTags;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MediaPage extends FragmentActivity {
	
	private MediaObject media;
	private LinearLayout linlay;
	private int aid;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DataManage data = new DataManage();
        final int point = getIntent().getIntExtra("point", 0);
        media = data.getAnimeDetails(this, point);
        aid = data.getAID(media.getTitle(), this);
        DataManage.clearCaches();
        DataManage.cacheObject(media);
        
        if (aid != 0) {
        	if (AniDBWrapper.doesAniDBfileExist(aid, this)){
        		DataManage.cacheObject2(AniDBWrapper.parseAniDBfile(aid, this));
        	}
        }
        
        //DataManage.;
        //DataManage.isRegistered(media.getTitle(), this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.slider);
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
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_chars),
        		FragmentCharacters.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_cats),
        		FragmentCategories.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_tags),
        		FragmentTags.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.frag_eps),
        		FragmentEpisodes.class, null);

        /*ScrollView view = (ScrollView) findViewById(R.id.media_relative);
        linlay = new LinearLayout(this);
        view.addView(linlay);
        linlay.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(this);
        String regState = "This show has no registered AID";
        aid = DataManage.getAID(media.getTitle(), this);
        if (aid != 0)
        	regState = "This show's AID is: " + aid;
        tv.setText("This is the page of " + media.getTitle() + "\nProgress "  +  media.getProgress() + " of " + media.getTotal() + "\n" + regState);
        Button but = new Button(this);
        but.setText("+");
        final Activity act = this;
        but.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	media.setProgress(media.getProgress()+1);
	            	tv.setText("This is the page of " + media.getTitle() + "\nProgress "  +  media.getProgress() + " of " + media.getTotal() );
	            	data.writeAnimeDetails(act, (AnimeObject) media, point);
	            }
	        });
        
        linlay.addView(tv);
        linlay.addView(but);
        if (aid != 0) {
        	if (AniDBWrapper.doesAniDBfileExist(aid, this)){
        		String stuff = "";
        		for (String part : AniDBWrapper.parseAniDBfile(aid, this)) {
        			stuff += "\n\n\n\n" + part;
        		}
        		TextView allMeta = new TextView(this);
        		allMeta.setText(stuff);
        		linlay.addView(allMeta);
        	}
        }*/
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_view, menu);
		return true;

    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.fetch_metadata:
                //newGame();
            	handleMetadata();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void handleMetadata() {
		String[] title = AniDBWrapper.getMostLikelyID(media.getTitle(), false).toArray(new String[0]);
        if (title.length < 1)
        	title = AniDBWrapper.getMostLikelyID(media.getTitle(), true).toArray(new String[0]);
        if (title.length == 1) {
        	if (aid == 0)
        		DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this);
        	AniDBWrapper.grabAnimeMetadata(Integer.parseInt(title[0].split("\\^")[1]), this);
        }
        
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

