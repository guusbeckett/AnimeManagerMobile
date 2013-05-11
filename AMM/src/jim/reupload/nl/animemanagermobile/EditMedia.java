package jim.reupload.nl.animemanagermobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ScrollView;

public class EditMedia extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anime_fragment_page);
        ScrollView sv = (ScrollView) findViewById(R.id.anime_relative);
    }


}
