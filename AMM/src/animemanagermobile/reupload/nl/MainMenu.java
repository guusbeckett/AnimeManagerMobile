package animemanagermobile.reupload.nl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animemanagermobile.reupload.nl.releasetracker.ReleaseTrackingService;
import animemanagermobile.reupload.nl.storages.SkyDriveFS;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        startService(new Intent(this, ReleaseTrackingService.class));
        SharedPreferences settings = getSharedPreferences("AMMprefs", 0);
        int storageMethod = settings.getInt("storageMethod", 0);
        if (storageMethod == 2) {
	        SkyDriveFS lel = new SkyDriveFS(this);
	        lel.trulyInit();
        }
     // Get the SearchView and set the searchable configuration

        //lel.findWatchingFile(this);
        //MangaUpdatesClient.getMostLikelyID("lel", false);
     // Restore preferences
        if (storageMethod == 0) {
        	RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_relative);
        	rl.removeAllViews();
        	TextView tx = new TextView(this);
        	tx.setText("It seems you have not yet configured a storage method, please do so in the settings menu");
        	Button but = new Button(this);
        	but.setText("Open settings menu");
        	final Intent intent_settings = new Intent(this, SettingsPage.class);
        	but.setOnClickListener(new View.OnClickListener() {
	            @Override
				public void onClick(View v) {
	            	startActivity(intent_settings);
	            }
	        });
        	rl.addView(but);
        	rl.addView(tx);
        }
        else {
	        final Intent intent1 = new Intent(this, ViewList.class);
	        //final Intent intent3 = new Intent(this, SeenList.class);
	        RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_relative);
	        final Button editAnimeButton = (Button) findViewById(R.id.button1);
	        final Button editMangaButton = (Button) findViewById(R.id.button2);
	        final Button viewListButton = (Button) findViewById(R.id.button3);
	        final Button viewReadManga = (Button) findViewById(R.id.button4);
	        editAnimeButton.setOnClickListener(new View.OnClickListener() {
	            @Override
				public void onClick(View v) {
	            	intent1.putExtra("type", 1);
	            	startActivity(intent1);
	            }
	        });
	        editMangaButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
				public void onClick(View v) {
	        		intent1.putExtra("type", 3);
	        		startActivity(intent1);
	        	}
	        });
	        viewListButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
				public void onClick(View v) {
	        		intent1.putExtra("type", 2);
	        		startActivity(intent1);
	        	}
	        });
	        viewReadManga.setOnClickListener(new View.OnClickListener() {
	        	@Override
				public void onClick(View v) {
	        		intent1.putExtra("type", 4);
	        		startActivity(intent1);
	        	}
	        });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	final Intent intent_settings = new Intent(this, SettingsPage.class);
        switch (item.getItemId()) {
            case R.id.action_settings:
                //newGame();
            	startActivity(intent_settings);
                return true;
            case R.id.menu_search:
            	Intent i = new Intent(this, SearchAMM.class);
            	startActivity(i);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    
}

/**
 * This will be my general checklist from now on
 * 
 * Notable Bugs:
 * cannot save progress on unregistered shows, causes ANR
 * 
 *TODO
 * [ ] BackLog support - Should not be hard to implement given the way lists are now
 * [*] Make deleting items also unregister them
 * [ ] Make the mangaCrawler more intelligent and more useful
 * [ ] Make the main menu nicer looking
 * [ ] Make a mediafire client
 * [ ] Make it possible to download metadata in an asynctask
 * [ ] Make it possible to mass download metadata
 * [ ] Implemented a "sync services" system that works separate from the filesystem code for sites like aniDB MyList and MAL
 * [ ] Make notifications stack
 * [ ] Make proper RSS reader
 * [ ] Make page for notifcations that links to the show
 * [ ] Make the notification system nagging
 * [ ] Make first run wizard
 * [ ] Make mangareader downloaders as backup system
 * [ ] Make it possible to manually select folder for a separate manga
 * [ ] Add archive support to mangareader
 * [ ] Fix all tabs for MediaPage
 * [ ] Make it possible to save files in SkyDrive
 * [ ] Fix Google Drive support
 * [ ] Make pop-out thingy for images with zoom
 * [ ] YouTube/NicoVideo linking to OP/ED
 * [ ] Inline links for aniDB descriptions
 * [ ] Proper multiple metadata source system
 * [*] able to open shows temporary from metadata source
 * [*] link to related shows/manga
 * [ ] only write to external when user has chosen to do so, it is available
 * [ ] simple stats screen, like MAL stats
 * [ ] Make actual list adapter for related shows
 * [ ] Make going to related anime or a searched anime/manga check if anime/manga is in users list and if yes open that
 * [ ] Make new menu for temp openened series
 * [ ] REMINDER, REFACTOR
 * 
 */
