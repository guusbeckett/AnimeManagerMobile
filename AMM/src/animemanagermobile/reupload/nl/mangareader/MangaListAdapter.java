package animemanagermobile.reupload.nl.mangareader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import animemanagermobile.reupload.nl.R;
 
public class MangaListAdapter extends BaseAdapter {
 
    private Activity activity;
    private String title;
    private static LayoutInflater inflater=null;
    private int i;
    private boolean[] existence;
    private int total;
 
    public MangaListAdapter(Activity a, String title, int total) {
        activity = a;
        i=1;
        this.total = total;
        this.title = title;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkExistence();
    }
    
    public void checkExistence() {
    	existence = new boolean[total];
    	for (int o=0;o<total;o++) { 
    		File f = new File("/storage/sdcard0/.searchmanga/"+title+"/"+(o+1)+"/");
    		existence[o] = f.exists();
    		Log.d("exist", f.toString()+f.exists());
    	}
    }
 
    public int getCount() {
        return total;
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.manga_item, null);
 
        TextView title = (TextView)vi.findViewById(R.id.manga_chapter); // title
        TextView available = (TextView)vi.findViewById(R.id.manga_available); // artist name
        TextView local = (TextView)vi.findViewById(R.id.manga_local); // duration
 
        // Setting all values in listview
        title.setText(this.title + " ch " + (position+1));
        available.setText("no sources");
        local.setText((existence[position])?"available":"not available");
        return vi;
    }
}