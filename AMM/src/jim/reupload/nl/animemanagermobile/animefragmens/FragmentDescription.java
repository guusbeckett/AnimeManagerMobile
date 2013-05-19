package jim.reupload.nl.animemanagermobile.animefragmens;

import jim.reupload.nl.animemanagermobile.MediaObject;
import jim.reupload.nl.animemanagermobile.R;
import jim.reupload.nl.animemanagermobile.data.DataManage;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentDescription extends Fragment {

	private MediaObject media;
	private LinearLayout linlay;

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
        
        TextView desc = new TextView(this.getActivity());
        if (metadata != null)
        	desc.setText(metadata[8]);
        else {
            desc.setText("No Metadata for " + media.getTitle());
            desc.setTypeface(null, Typeface.ITALIC);
        	desc.setTextSize(8);
        }
        linlay.addView(desc);
        
        return v;
    }
}
