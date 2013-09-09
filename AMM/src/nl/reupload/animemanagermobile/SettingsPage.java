AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program
package nl.reupload.animemanagermobile;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.optionfragments.DevOptFragment;
import nl.reupload.animemanagermobile.optionfragments.FileLocationFrag;
import nl.reupload.animemanagermobile.optionfragments.MangaSelectionFragment;
import nl.reupload.animemanagermobile.optionfragments.MetadataFragment;
import nl.reupload.animemanagermobile.optionfragments.SubbersFragment;

public class SettingsPage extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentTabHost mTabHost;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
	        super.onCreate(savedInstanceState);
	        ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	
	
	        mViewPager = new ViewPager(this);
	        mViewPager.setId(R.id.pager);
	        setContentView(mViewPager);
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
	
	        TabsAdapter mTabsAdapter = new TabsAdapter(this, mViewPager);
	        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.list_location),
	                FileLocationFrag.class, null);
	        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.fansub_list),
	        		SubbersFragment.class, null);
	        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.meta_storage),
	        		MetadataFragment.class, null);
	        mTabsAdapter.addTab(actionBar.newTab().setText("Manga Storage"),
	        		MangaSelectionFragment.class, null);
	        mTabsAdapter.addTab(actionBar.newTab().setText("Dev Options"),
	        		DevOptFragment.class, null);
		}
		else onCreateOld(savedInstanceState);
	}
	
	protected void onCreateOld(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   
        mTabHost = new FragmentTabHost(this);
        mTabHost.setup(this, getSupportFragmentManager());
        mTabHost.addTab(mTabHost.newTabSpec("listloc").setIndicator("List location"),
                FileLocationFrag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fansub").setIndicator("Fansub List"),
        		SubbersFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("stor").setIndicator("Storage"),
        		MetadataFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("mangstor").setIndicator("Manga storage"),
        		MangaSelectionFragment.class, null);
        setContentView(mTabHost);
        
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MainMenu.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}

