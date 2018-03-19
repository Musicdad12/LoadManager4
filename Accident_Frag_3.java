package com.jrschugel.loadmanager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class Accident_Frag_3 extends Fragment
{
    public static Accident_Frag_3 newInstance() {
        return new Accident_Frag_3();
    }
    EditText DrvName;
    EditText TruckNo;
    EditText TrlrNo;
    EditText TripNo;
    EditText Injured1Name;
    EditText Injured1Phone;
    EditText Injured1Addr;
    EditText Injured1Age;
    EditText Injured2Name;
    EditText Injured2Phone;
    EditText Injured2Addr;
    EditText Injured2Age;
    EditText Injured3Name;
    EditText Injured3Phone;
    EditText Injured3Addr;
    EditText Injured3Age;
    EditText Injured4Name;
    EditText Injured4Phone;
    EditText Injured4Addr;
    EditText Injured4Age;
    
    Context context;
    private INavActivity2 mINavActivity;
    Long AccidentID;
    DatabaseHelper myDb;

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
        View rootView = inflater.inflate(R.layout.fragment_sub__accident03, container,
                false);
        DrvName = rootView.findViewById(R.id.DriverName);
        TruckNo = rootView.findViewById(R.id.TruckNo);
        TrlrNo = rootView.findViewById(R.id.TrailerNo);
        TripNo = rootView.findViewById(R.id.TripNo);
        Injured1Name = rootView.findViewById(R.id.InjuredName1);
        Injured1Phone = rootView.findViewById(R.id.InjuredPhone1);
        Injured1Addr = rootView.findViewById(R.id.InjuredAddress1);
        Injured1Age = rootView.findViewById(R.id.InjuredAge1);
        Injured2Name = rootView.findViewById(R.id.InjuredName2);
        Injured2Phone = rootView.findViewById(R.id.InjuredPhone2);
        Injured2Addr = rootView.findViewById(R.id.InjuredAddress2);
        Injured2Age = rootView.findViewById(R.id.InjuredAge2);
        Injured3Name = rootView.findViewById(R.id.InjuredName3);
        Injured3Phone = rootView.findViewById(R.id.InjuredPhone3);
        Injured3Addr = rootView.findViewById(R.id.InjuredAddress3);
        Injured3Age = rootView.findViewById(R.id.InjuredAge3);
        Injured4Name = rootView.findViewById(R.id.InjuredName4);
        Injured4Phone = rootView.findViewById(R.id.InjuredPhone4);
        Injured4Addr = rootView.findViewById(R.id.InjuredAddress4);
        Injured4Age = rootView.findViewById(R.id.InjuredAge4);

        context = getActivity();
        myDb = new DatabaseHelper(context);
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);

        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();
        DrvName.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRVRNAME)));
        TruckNo.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCTRUCKNUMBER)));
        TrlrNo.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCTRLRNUMBER)));
        TripNo.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCLOADNUMBER)));
        Injured1Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED1NAME)));
        Injured1Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED1PHONE)));
        Injured1Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED1ADDRESS)));
        Injured1Age.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED1AGE)));
        Injured2Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED2NAME)));
        Injured2Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED2PHONE)));
        Injured2Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED2ADDRESS)));
        Injured2Age.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED2AGE)));
        Injured3Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED3NAME)));
        Injured3Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED3PHONE)));
        Injured3Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED3ADDRESS)));
        Injured3Age.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED3AGE)));
        Injured4Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED4NAME)));
        Injured4Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED4PHONE)));
        Injured4Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED4ADDRESS)));
        Injured4Age.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCINJURED4AGE)));


        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_4 fragment = new Accident_Frag_4();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 4", bundle);
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
                Accident_Frag_2 fragment = new Accident_Frag_2();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 2", bundle);
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

    void SaveData () {
        String drivername = DrvName.getText().toString();
        String trucknumber = TruckNo.getText().toString();
        String trailernumber = TrlrNo.getText().toString();
        String loadnumber = TripNo.getText().toString();
        String name1 = Injured1Name.getText().toString();
        String address1 = Injured1Addr.getText().toString();
        String phone1 = Injured1Phone.getText().toString();
        String age1 = Injured1Age.getText().toString();
        String name2 = Injured2Name.getText().toString();
        String address2 = Injured2Addr.getText().toString();
        String phone2 = Injured2Phone.getText().toString();
        String age2 = Injured2Age.getText().toString();
        String name3 = Injured3Name.getText().toString();
        String address3 = Injured3Addr.getText().toString();
        String phone3 = Injured3Phone.getText().toString();
        String age3 = Injured3Age.getText().toString();
        String name4 = Injured4Name.getText().toString();
        String address4 = Injured4Addr.getText().toString();
        String phone4 = Injured4Phone.getText().toString();
        String age4 = Injured4Age.getText().toString();

        myDb.saveAccidentPage3(AccidentID, drivername, trucknumber, trailernumber, loadnumber, name1, address1, phone1, age1,
                name2, address2, phone2, age2, name3, address3, phone3, age3, name4, address4, phone4, age4);
    }
}


