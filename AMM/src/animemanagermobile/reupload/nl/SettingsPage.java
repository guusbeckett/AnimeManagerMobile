package animemanagermobile.reupload.nl;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import animemanagermobile.reupload.nl.optionfragments.FileLocationFrag;
import animemanagermobile.reupload.nl.optionfragments.MangaSelectionFragment;
import animemanagermobile.reupload.nl.optionfragments.MetadataFragment;
import animemanagermobile.reupload.nl.optionfragments.SubbersFragment;

public class SettingsPage extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.list_location),
                FileLocationFrag.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.fansub_list),
        		SubbersFragment.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.meta_storage),
        		MetadataFragment.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText("Manga Storage"),
        		MangaSelectionFragment.class, null);
	}
	
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

