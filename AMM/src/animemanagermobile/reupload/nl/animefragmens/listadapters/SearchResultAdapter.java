package animemanagermobile.reupload.nl.animefragmens.listadapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import animemanagermobile.reupload.nl.MediaObject;
import animemanagermobile.reupload.nl.R;
 
public class SearchResultAdapter extends BaseAdapter {
 
    private static LayoutInflater inflater=null;
	private MediaObject[] items;
 
    public SearchResultAdapter(Activity act, MediaObject[] items) {
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
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
            vi = inflater.inflate(R.layout.search_result, null);
 
        MediaObject item = items[position];
        TextView title = (TextView)vi.findViewById(R.id.search_title); // title
        TextView type = (TextView)vi.findViewById(R.id.search_type); // artist name
        TextView status = (TextView)vi.findViewById(R.id.search_status); // duration
 
        // Setting all values in listview
        title.setText(item.getTitle()+"");
        status.setText(item.getType()+"");
        type.setText("unused"+"");
        //gender.setText((existence[position])?"available":"not available");
        return vi;
    }
}