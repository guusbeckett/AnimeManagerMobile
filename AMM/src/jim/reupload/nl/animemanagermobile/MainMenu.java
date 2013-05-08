package jim.reupload.nl.animemanagermobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

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
        return true;

    }
    
}
