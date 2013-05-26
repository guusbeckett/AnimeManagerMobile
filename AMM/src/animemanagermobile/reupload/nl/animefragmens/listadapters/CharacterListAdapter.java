package animemanagermobile.reupload.nl.animefragmens.listadapters;

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
 
public class CharacterListAdapter extends BaseAdapter {
 
    private String title;
    private static LayoutInflater inflater=null;
    private int i;
	private String[][] items;
 
    public CharacterListAdapter(Activity act, String[][] items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        i = 0;
    }
    
    public int getCount() {
        return items.length;
    }
 
    public Object getItem(int position) {
        return items[position];
    }
 
    public long getItemId(int position) {
        return position;
    }
 
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
        title.setText(item[1]);
        gender.setText(item[2]);
        type.setText(item[0]);
        seiyuu.setText(item[5]);
        //gender.setText((existence[position])?"available":"not available");
        return vi;
    }
}