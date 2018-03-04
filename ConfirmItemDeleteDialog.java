package com.jrschugel.loadmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by seanm on 1/22/2018.
 * Copyright 2017. All rights reserved.
 */

public class ConfirmItemDeleteDialog extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialog", "You clicked OK");
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialog", "You clicked Cancel");
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
