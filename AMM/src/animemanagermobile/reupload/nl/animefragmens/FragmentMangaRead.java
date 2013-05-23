package animemanagermobile.reupload.nl.animefragmens;

import jim.reupload.nl.animemanagermobile.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.data.DataManage;
import animemanagermobile.reupload.nl.mangareader.MangaListAdapter;
import animemanagermobile.reupload.nl.mangareader.MangaView;

public class FragmentMangaRead extends Fragment {

	private MediaObject media;
	//private LinearLayout linlay;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.activity_edit_anime , container, false);
		media = (MediaObject) DataManage.getCached();
		String[] metadata = null;
		if (DataManage.isCached2())
			metadata = (String[]) DataManage.getCached2();
		RelativeLayout view = (RelativeLayout) v.findViewById(R.id.anime_relative);
        //linlay = new LinearLayout(this.getActivity());
        //view.addView(linlay);
		final FragmentActivity activ = this.getActivity();
        //linlay.setOrientation(LinearLayout.VERTICAL);
        
        ListView lv = new ListView(this.getActivity());
        MangaListAdapter adapt = new MangaListAdapter(getActivity(), media.getTitle(), DataManage.getMangaChapters(media.getTitle()));
        lv.setAdapter(adapt);
        lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activ, MangaView.class);
				intent.putExtra("title", media.getTitle());
				intent.putExtra("chapter", arg2+1);
				startActivity(intent);
				Log.d("start", media.getTitle());
				
			}
		});
        view.addView(lv);
        return v;
    }
}
