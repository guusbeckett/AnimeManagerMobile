package jim.reupload.nl.animemanagermobile;

import com.dropbox.sync.android.DbxAccountManager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class FileLocationFrag extends Fragment implements OnItemSelectedListener {

	private View v;
	private LinearLayout masll;
	private LinearLayout ll;
	private DbxAccountManager mDbxAcctMgr;
	static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_settings_location, container, false);
        masll = (LinearLayout) v.findViewById(R.id.setting_scroll_location);
        ll = new LinearLayout(getActivity());
	    ll.setOrientation(LinearLayout.VERTICAL);
       /* for (int i=0;i<99;i++) {
	        TextView tv = new TextView(getActivity());
	        tv.setText("my text " + i);
	        ll.addView(tv);
        }*/
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
	            R.array.spinner_storage, android.R.layout.simple_spinner_item);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(this);
	    SharedPreferences settings = getActivity().getSharedPreferences("AMMprefs", 0);
        int storageMethod = settings.getInt("storageMethod", 0);
	    spinner.setSelection(storageMethod);
	    masll.addView(ll);
	    mDbxAcctMgr = DbxAccountManager.getInstance(getActivity().getApplicationContext(), this.getString(R.string.DROPBOX_APP_KEY), this.getString(R.string.DROPBOX_APP_SECRET));
        return v;
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		// We need an Editor object to make preference changes.
	    // All objects are from android.context.Context
		SharedPreferences settings = getActivity().getSharedPreferences("AMMprefs", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt("storageMethod", pos);
        // Commit the edits!
	    

		
		
		switch (pos) {
			case (1):
				Button but = new Button(getActivity());
				TextView tv = new TextView(getActivity());
				if (mDbxAcctMgr.getLinkedAccount() == null) {
					but.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	mDbxAcctMgr.startLink(getActivity(), REQUEST_LINK_TO_DBX);
			            	
			            }
			        });
					but.setText("Link Dropbox");
					tv.setText("Status: unlinked");
				}
				else {
					but.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	mDbxAcctMgr.unlink();
			            	
			            }
			        });
					but.setText("unlink Dropbox");
					tv.setText("Status: linked");
				}
				//TextView tv = new TextView(getActivity());
				//tv.setText("Dropbox");
				ll.removeAllViews();
				ll.addView(tv);
				ll.addView(but);
				
				break;
			case (2):
				TextView tv1 = new TextView(getActivity());
				tv1.setText("Skydrive");
				ll.removeAllViews();
				ll.addView(tv1);
				break;
			case (3):
				TextView tv2 = new TextView(getActivity());
				tv2.setText("Google Drive");
				ll.removeAllViews();
				ll.addView(tv2);
				break;
		}
		editor.commit();
		
    }

	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            Log.d("Dropbox Link", "Success");
	        } else {
	            
	        	Log.d("Dropbox Link", "Success");
	        }            
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	



}