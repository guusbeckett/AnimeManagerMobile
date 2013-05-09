package jim.reupload.nl.animemanagermobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
     // Restore preferences
        SharedPreferences settings = getSharedPreferences("AMMprefs", 0);
        int storageMethod = settings.getInt("storageMethod", 0);
        if (storageMethod == 0) {
        	RelativeLayout rl = (RelativeLayout) findViewById(R.id.main_relative);
        	rl.removeAllViews();
        	TextView tx = new TextView(this);
        	tx.setText("It seems you have not yet configured a storage method, please do so in the settings menu");
        	Button but = new Button(this);
        	but.setText("Open settings menu");
        	final Intent intent_settings = new Intent(this, SettingsPage.class);
        	but.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	startActivity(intent_settings);
	            }
	        });
        	rl.addView(but);
        	rl.addView(tx);
        }
        else {
	        final Intent intent1 = new Intent(this, EditWatch.class);
	        final Intent intent2 = new Intent(this, EditRead.class);
	        final Intent intent3 = new Intent(this, ViewList.class);
	        final Button editAnimeButton = (Button) findViewById(R.id.button1);
	        final Button editMangaButton = (Button) findViewById(R.id.button2);
	        final Button viewListButton = (Button) findViewById(R.id.button3);
	        editAnimeButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	startActivity(intent1);
	            }
	        });
	        editMangaButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		startActivity(intent2);
	        	}
	        });
	        viewListButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		startActivity(intent3);
	        	}
	        });
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}