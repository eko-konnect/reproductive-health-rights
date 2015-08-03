package org.ekokonnect.reprohealth.utils;

import org.ekokonnect.reprohealth.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
//import android.app.AlertDialog;

public class AlertDialogManager extends DialogFragment {


	public AlertDialogManager(){		
	}
	
	
	/**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public static void showAlertDialog(FragmentManager fm, String title, String message,
            Boolean status) {
        AlertDialogManager dialog = new AlertDialogManager();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
//        b.putBoolean("status", status);
        dialog.setArguments(b);
        dialog.show(fm, title);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
    	Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(args.getString("title"));
//        if(args.getBoolean("status") != null)
//            // Setting alert dialog icon
//            builder.setIcon((status) ? R.drawable.navigation_accept : R.drawable.navigation_cancel);
        builder.setMessage(args.getString("message"))
               .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
