package nl.reupload.animemanagermobile.dialogs;

import nl.reupload.animemanagermobile.dialogs.ShowPickerDialog.OnDialogSelectorListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

@Deprecated
public class ShowPickerDialog extends AlertDialog implements OnClickListener {

	protected ShowPickerDialog(Context context) {
		super(context);
	}

	private static int mResourceArray;
	private String[] names;
	private int[] ids;
	private String title;
	private static int mSelectedIndex;
	private OnDialogSelectorListener mDialogSelectorCallback;
	
	public interface OnDialogSelectorListener {
	    public void onSelectedOption(int selectedIndex);
	}
	
/*	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mDialogSelectorCallback = (OnDialogSelectorListener)activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDialogSelectorListener");
        }
    }*/
	
	public void setCallback(OnDialogSelectorListener listen) {
		mDialogSelectorCallback = listen;
	}

//	@Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        //builder.setMessage(R.string.show_options);
//       // for (String item : names)
//        //	Log.d(";_;", item);
//        final Activity ac = this.getActivity();
//        
//        builder.setItems(names, this);
//
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
	
	public void setData(String[] data) {
		names = new String[data.length];
		ids = new int[data.length];
		if (data.length > 0) {
			int i = 0;
			for (String item : data) {
				names[i] = item.split("\\^")[0];
				ids[i] = Integer.parseInt(item.split("\\^")[1]);
				i++;
				Log.d(";_;", item);
			}
		}
		else
			Log.d(";_;", "no shows");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		mDialogSelectorCallback.onSelectedOption(which);
		Log.d("na" , which+"");
		
	}

}

