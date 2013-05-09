package jim.reupload.nl.animemanagermobile;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MediaPage extends Activity {
	
	private MediaObject media;
	private RelativeLayout rl;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DataManage data = new DataManage();
        final int point = getIntent().getIntExtra("point", 0);
        media = data.getAnimeDetails(this, point);
        DataManage.isRegistered(media.getTitle(), this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.media_page);
        
        ScrollView view = (ScrollView) findViewById(R.id.media_relative);
        rl = new RelativeLayout(this);
        view.addView(rl);
        final TextView tv = new TextView(this);
        String regState = "This show has no registered AID";
        int aid = DataManage.getAID(media.getTitle());
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
        
        rl.addView(tv);
        rl.addView(but);
        
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
        	DataManage.register(media.getTitle(), Integer.parseInt(title[0].split("\\^")[1]), this);
        }
        TextView tv2 = new TextView(this);
        String lel = "";
        
        for (String item:title)
        	lel+="aniDB title: " + item.split("\\^")[0] + "\naniDB id: " + item.split("\\^")[1]+"\n";
        tv2.setText("\n\n\n\n\n" + lel);
        rl.addView(tv2);
	}
}
