package jim.reupload.nl.animemanagermobile.animefragmens;

import jim.reupload.nl.animemanagermobile.AniDBWrapper;
import jim.reupload.nl.animemanagermobile.AnimeObject;
import jim.reupload.nl.animemanagermobile.DataManage;
import jim.reupload.nl.animemanagermobile.MediaObject;
import jim.reupload.nl.animemanagermobile.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentGeneral extends Fragment {

	private LinearLayout linlay;
	private int aid;
	private MediaObject media;
	private DataManage data;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.anime_fragment_page , container, false);
		media = (MediaObject) DataManage.getCached();
		String[] metadata = null;
		if (DataManage.isCached2())
			metadata = (String[]) DataManage.getCached2();
		ScrollView view = (ScrollView) v.findViewById(R.id.anime_frag);
        linlay = new LinearLayout(this.getActivity());
        view.addView(linlay);
        linlay.setOrientation(LinearLayout.VERTICAL);
        Log.d("lel", "1");
        TextView title = new TextView(this.getActivity());
        title.setGravity(Gravity.CENTER);
    	title.setText(media.getTitle());
    	title.setTypeface(null, Typeface.BOLD);
    	linlay.addView(title);
    	Log.d("lel", "2");
        if (metadata != null) {
        	Bitmap bm = null;
        	if (DataManage.doesExternalFileExist(this.getActivity().getExternalFilesDir(null) + "/image/" + metadata[10], this.getActivity())) {
        		bm = DataManage.loadImageFromExternal(metadata[10], this.getActivity());
        	}
        	else {
        		AniDBWrapper.fetchImage(metadata[10], this.getActivity());
        		bm = DataManage.loadImageFromExternal(metadata[10], this.getActivity());
        	}
        	ImageView img = new ImageView(this.getActivity());
        	img.setImageBitmap(bm);
        	
        	linlay.addView(img);
        }
        
        else {
        	TextView nometa = new TextView(this.getActivity());
        	nometa.setText("No Metadata for " + media.getTitle());
        	nometa.setTypeface(null, Typeface.ITALIC);
        	nometa.setTextSize(8);
        	linlay.addView(nometa);
        }
        Log.d("lel", "3");
       /* final TextView tv = new TextView(this.getActivity());
        String regState = "This show has no registered AID";*/
        TextView prog1 = new TextView(this.getActivity());
        if (metadata != null)
        	prog1.setText("Progress: "+media.getProgress() + "/" + metadata[1]);
        else
        	prog1.setText("Progress: "+media.getProgress() + "/" + media.getTotal());
        aid = media.getId();
        prog1.setOnClickListener(new View.OnClickListener() {

        	  @Override
        	  public void onClick(View v) {
        	    // request your webservice here. Possible use of AsyncTask and ProgressDialog
        	    // show the result here - dialog or Toast
        	  }

        	});
        linlay.addView(prog1);
        
        Log.d("lel", "4");
      //  if (aid != 0)
        //	regState = "This show's AID is: " + aid;
        //tv.setText("This is the page of " + media.getTitle() + "\nProgress "  +  media.getProgress() + " of " + media.getTotal() + "\n" + regState);
       // Button but = new Button(this.getActivity());
        //but.setText("+");
        Log.d("lel", "5");
        Log.d("aid", aid+"");
        data = new DataManage();
       /* final Activity act = this.getActivity();
        but.setOnClickListener(new View.OnClickListener() {
	            private int point;

				public void onClick(View v) {
	            	media.setProgress(media.getProgress()+1);
	            	tv.setText("This is the page of " + media.getTitle() + "\nProgress "  +  media.getProgress() + " of " + media.getTotal() );
	            	data.writeAnimeDetails(act, (AnimeObject) media, point);
	            }
	        });
        Log.d("", "4");*/
        //linlay.addView(tv);
        //linlay.addView(but);
        return v;
        
    }
}
