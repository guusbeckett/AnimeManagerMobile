package nl.reupload.animemanagermobile.animefragmens.listadapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import nl.reupload.animemanagermobile.R;
 
public class EpisodeListAdapter extends BaseAdapter {
 
    private String title;
    private static LayoutInflater inflater=null;
    private int i;
	private String[][] items;
 
    public EpisodeListAdapter(Activity act, String[][] items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        i = 0;
    }
    
    @Override
	public int getCount() {
        return items.length;
    }
 
    @Override
	public Object getItem(int position) {
        return items[position];
    }
 
    @Override
	public long getItemId(int position) {
        return position;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.character_item, null);
 
        Log.d("ne", "e");
        String[] item = items[position];
        TextView title = (TextView)vi.findViewById(R.id.character_title); // title
        TextView type = (TextView)vi.findViewById(R.id.character_type); // artist name
        TextView gender = (TextView)vi.findViewById(R.id.GenderText); // duration
        TextView seiyuu = (TextView)vi.findViewById(R.id.SeiyuuText); // duration
 
        i++;
        // Setting all values in listview
        title.setText(item[0]);
        gender.setText(item[1]);
        type.setText(item[2]);
        seiyuu.setText(item[3]);
        //gender.setText((existence[position])?"available":"not available");
        return vi;
    }
}