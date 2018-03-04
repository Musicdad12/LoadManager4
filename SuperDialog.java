package com.jrschugel.loadmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

/*
  Created by seanm on 7/21/2017.
  Copyright 2017. All rights reserved.
 */

public class SuperDialog extends Activity implements DialogInterface.OnCancelListener {

    //Edit these : Add as much dialogs as you want
    public final static int DIALOG_1 = 0;
    public final static int DIALOG_2 = 1;
    public final static int DIALOG_3 = 2;
    public final static int DIALOG_4 = 3;
    public final static int DIALOG_ERROR = 4;

    //Now edit this function
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
            case DIALOG_1:
                //create dialog1
                dialog = null;
                break;
            case DIALOG_2:
                //create dialog2
                dialog = new AlertDialog.Builder(this).setMessage("Success!!!\n Brought to you by Sherif").create();
                break;
            case DIALOG_3:
                //create dialog3
                dialog = null;
                break;
            case DIALOG_4:
                //create dialog4
                dialog = null;
                break;
            case DIALOG_ERROR:
                //create dialog
                dialog = new AlertDialog.Builder(this).setMessage("ERROR! This is a global dialog\n Brought to you by Sherif").create();
                break;
            default:
                //create a default dialog
                dialog = null;
        }
        if (dialog != null) {
            dialog.setOnCancelListener(this);
        }
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog(this.getIntent().getExtras().getInt("dialog"));

    }
    @Override
    public void onCancel(DialogInterface arg0) {
        // THIS IS VERY IMPORTANT TO REMOVE THE ACTIVITY WHEN THE DIALOG IS DISMISSED
        // IF NOT ADDED USER SCREEN WILL NOT RECEIVE ANY EVENTS BEFORE USER PRESSES BACK
        finish();
    }
}