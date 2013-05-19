package jim.reupload.nl.animemanagermobile.animefragmens;

import java.util.Calendar;

import jim.reupload.nl.animemanagermobile.AnimeObject;
import jim.reupload.nl.animemanagermobile.MediaObject;
import jim.reupload.nl.animemanagermobile.R;
import jim.reupload.nl.animemanagermobile.data.AniDBWrapper;
import jim.reupload.nl.animemanagermobile.data.DataManage;
import jim.reupload.nl.animemanagermobile.data.MangaUpdatesClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Font.Style;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentGeneral extends Fragment {

	private LinearLayout linlay;
	private int aid;
	private MediaObject media;
	private DataManage data;
	private int maxValue ;

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
    	final Activity act = this.getActivity();
    	Log.d("lel", "2");
    	int type = 0 ;
        if (metadata != null) {
        	Bitmap bm = null;
        	type = Integer.parseInt(metadata[16]);
        	if (type == 1 || type == 2) {
        		if (DataManage.doesExternalFileExist("/images/" + metadata[10], this.getActivity())) {
        			bm = DataManage.loadImageFromExternal(metadata[10], this.getActivity());
        			
            	}
        		else {
            		AniDBWrapper.fetchImage(metadata[10], this.getActivity());
            		bm = DataManage.loadImageFromExternal(metadata[10], this.getActivity());
            		//Log.d("hai", "FnF");
        		}
            	
        	}
        	else if (type != 0) {
        		if (DataManage.doesExternalFileExist("/images/" + metadata[10].split("/")[metadata[10].split("/").length-1], this.getActivity())) {
        			bm = DataManage.loadImageFromExternal(metadata[10].split("/")[metadata[10].split("/").length-1], this.getActivity());
            	}
        		else {
        			MangaUpdatesClient.fetchImage(metadata[10], this.getActivity());
        			bm = DataManage.loadImageFromExternal(metadata[10].split("/")[metadata[10].split("/").length-1], this.getActivity());
        		}
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
        if (metadata != null) {
        	prog1.setText("Progress: "+media.getProgress() + "/" + metadata[1]);
        	//maxValue = Integer.parseInt(metadata[1]);
        	maxValue = 0;
        }
        else {
        	prog1.setText("Progress: "+media.getProgress() + "/" + media.getTotal());
        	maxValue = media.getTotal();
        }
        if (maxValue == 0) {
        	maxValue = 99;
        }
        aid = media.getId();
        prog1.setOnClickListener(new View.OnClickListener() {

        	  @Override
        	  public void onClick(View v) {
//        	    NumberPicker numpick = new NumberPicker(act);
//        	    numpick.setMinValue(0);
//        	    numpick.setMaxValue(maxValue);
//        	    numpick.setOnClickListener(new OnClickListener() {
////					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
        	    AlertDialog.Builder alert = new AlertDialog.Builder(act);

                alert.setTitle("Select the value: ");

                NumberPicker np = new NumberPicker(act);
                np.setMinValue(0);
                np.setMaxValue(maxValue);
                np.setWrapSelectorWheel(false);
                //np.setDisplayedValues(nums);
                np.setValue(media.getProgress());
                alert.setView(np);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  // Do something with value!
                  }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Cancel.
                  }
                });
                alert.show();
        	  }

        	});
        linlay.addView(prog1);
        TextView prog2 = new TextView(this.getActivity());
        if (type == 1 || type == 2) {
        	Calendar now = Calendar.getInstance();
            String month = null;
            String day = null;
            if (now.get(Calendar.MONTH)+1 < 10)
            	month = "0"+(now.get(Calendar.MONTH)+1);
            else
            	month = (now.get(Calendar.MONTH)+1)+"";
            if (now.get(Calendar.DAY_OF_MONTH) < 10)
            	day = "0"+now.get(Calendar.DAY_OF_MONTH);
            else
            	day = now.get(Calendar.DAY_OF_MONTH)+"";
            String nextep = AniDBWrapper.findEpisodeNearestAfterDate(now.get(Calendar.YEAR) + "-" + month + "-" + day, metadata[15]);
            if (nextep != null) {
            	prog2.setText("Next airing episode: " + nextep.split("\\^")[0] + "\nAirdate: " + nextep.split("\\^")[1]);
            	prog2.setTypeface(null, Typeface.ITALIC);
            	prog2.setTextSize(11);
            }
        }
        linlay.addView(prog2);
        Log.d("lel", "4");
      //  if (aid != 0)
        //	regState = "This show's AID is: " + aid;
        //tv.setText("This is the page of " + media.getTitle() + "\nProgress "  +  media.getProgress() + " of " + media.getTotal() + "\n" + regState);
       // Button but = new Button(this.getActivity());
        //but.setText("+");
        Log.d("lel", "5");
        Log.d("aid", aid+"");
        //data = new DataManage();
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
