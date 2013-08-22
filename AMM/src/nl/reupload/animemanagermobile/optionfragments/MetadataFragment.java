package nl.reupload.animemanagermobile.optionfragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
import nl.reupload.animemanagermobile.data.DataManage;

public class MetadataFragment extends Fragment {

	
	private View v;
	private LinearLayout masll;
	private LinearLayout ll;

	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings , container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
	    TextView tv = new TextView(getActivity());
        tv.setText("Please select an option for storage");
        masll.addView(tv);
        Button destroyMeta = new Button(this.getActivity());
        final Activity act = this.getActivity();
        destroyMeta.setText("Delete ALL metadata");
        destroyMeta.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	DataManage.deleteAllMeta(act);
            	
            }
        });
        ll.addView(destroyMeta);
        Switch storageLoc = new Switch(this.getActivity());
        storageLoc.setText("Metadata storage");
        storageLoc.setTextOff("Internal");
        storageLoc.setTextOn("External");
        storageLoc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences settings = act.getSharedPreferences("AMMprefs", 0);
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("MetadataExtStorage", isChecked);
				
				
			}
		});
        ll.addView(storageLoc);
        masll.addView(ll);
        return v;
    }
}
