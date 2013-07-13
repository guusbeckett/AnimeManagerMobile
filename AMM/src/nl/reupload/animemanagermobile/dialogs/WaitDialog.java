package nl.reupload.animemanagermobile.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import nl.reupload.animemanagermobile.R;

public class WaitDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.wait_fs);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}