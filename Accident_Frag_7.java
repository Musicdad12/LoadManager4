package com.jrschugel.loadmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;

public final class Accident_Frag_7 extends Fragment {
    public static Accident_Frag_7 newInstance() {
        return new Accident_Frag_7();
    }

    EditText Weather;
    EditText Narrative;

    Context context;
    DatabaseHelper myDb;
    private INavActivity2 mINavActivity;
    Long AccidentID;
    SessionManager session;
    private static final String TAG = "Accident_Frag_7";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mINavActivity = (INavActivity2) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub__accident07, container,
                false);
        Weather = rootView.findViewById(R.id.AccConditions);
        Narrative = rootView.findViewById(R.id.AccNarrative);

        context = getActivity();
        myDb = new DatabaseHelper(context);
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();

        Weather.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWEATHER)));
        Narrative.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCNARRATIVE)));

        session = new SessionManager(context);
        session.getUserDetails();
        HashMap<String, String> user = session.getUserDetails();
        // Display welcome message and set user variables
        final String FullName = "Welcome " + user.get(SessionManager.KEY_NAME);
        final String TruckNumber = user.get(SessionManager.KEY_TRUCK);

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save / Email", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);

                Intent intent = new Intent(Intent.ACTION_SEND); // it's not ACTION_SEND
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"musicdad2014@gmail.com"}); // recipients
                StringBuilder Body = new StringBuilder("This is a driver accident report from " + FullName + " in Truck # " + TruckNumber + ".\n\n");
                Cursor cursorData = myDb.getAccidents(AccidentID);
                String Key, Data;
                cursorData.moveToFirst();
                for (int i = 0; i < cursorData.getColumnCount(); i++) {
                    switch (cursorData.getType(i)) {
                        case Cursor.FIELD_TYPE_BLOB:
                            Key = cursorData.getColumnName(i);
                            byte[] drawing = cursorData.getBlob(i);
                            Bitmap bitDrawing = DbBitmapUtility.getImage(drawing);
                            Body.append(Key).append("    ").append(bitDrawing).append("\n");
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            Key = cursorData.getColumnName(i);
                            Integer radioGroup = cursorData.getInt(i);
                            if (radioGroup == 1) {
                                Data = "Yes";
                            } else {
                                Data = "No";
                            }
                            Body.append(Key).append("    ").append(Data).append("\n");
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            Key = cursorData.getColumnName(i);
                            Data = cursorData.getString(i);
                            Body.append(Key).append("    ").append(Data).append("\n");
                            break;
                    }
                }
                intent.putExtra(Intent.EXTRA_SUBJECT, "Accident report from " + FullName);
                intent.putExtra(Intent.EXTRA_TEXT, Body.toString());
                //intent.setDataAndType(Uri.parse("mailto:musicdad2014@gmail.com"), "text/plain"); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                //context.startActivity(Intent.createChooser(intent, "Send Email"));
               /*
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
// The intent does not have a URI, so declare the "text/plain" MIME type
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                */
                PackageManager packageManager = context.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    Log.d(TAG, "onClick: app  found");
                } else {
                    Log.d(TAG, "onClick: No app");
                }
                mINavActivity.sendEmail(intent);
            }
        });

        FloatingActionButton fabPrev = rootView.findViewById(R.id.fabPrev);
        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Previous Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_6 fragment = new Accident_Frag_6();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 6", bundle);
            }
        });

        FloatingActionButton fabExit = rootView.findViewById(R.id.fabExit);
        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save / Exit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                AccidentFragment fragment = new AccidentFragment();
                mINavActivity.inflateFragment(fragment, "Accident Report", bundle);
            }
        });

        return rootView;
    }

    void SaveData() {
        String weather = Weather.getText().toString();
        String summary = Narrative.getText().toString();
        myDb.saveAccidentPage7(AccidentID, weather, summary);
    }
}


