package animemanagermobile.reupload.nl.optionfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import animemanagermobile.reupload.nl.R;
import animemanagermobile.reupload.nl.data.DataManage;

public class MetadataFragment extends Fragment {

	
	private View v;
	private LinearLayout masll;
	private LinearLayout ll;

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
            public void onClick(View v) {
            	DataManage.deleteAllMeta(act);
            	
            }
        });
        ll.addView(destroyMeta);
        masll.addView(ll);
        return v;
    }
}
