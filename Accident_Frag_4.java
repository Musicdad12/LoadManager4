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

public final class Accident_Frag_4 extends Fragment
{
    public static Accident_Frag_4 newInstance() {
        return new Accident_Frag_4();
    }

    EditText PropName;
    EditText PropAddr;
    EditText PropDamaged;
    EditText Witness1Name;
    EditText Witness1Phone;
    EditText Witness1Addr;
    EditText Witness2Name;
    EditText Witness2Phone;
    EditText Witness2Addr;
    EditText Witness3Name;
    EditText Witness3Phone;
    EditText Witness3Addr;
    
    Context context;
    private INavActivity2 mINavActivity;
    DatabaseHelper myDb;
    Long AccidentID;

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
        View rootView = inflater.inflate(R.layout.fragment_sub__accident04, container,
                false);
        PropName = rootView.findViewById(R.id.PropOwner);
        PropAddr = rootView.findViewById(R.id.PropOwnerAddr);
        PropDamaged = rootView.findViewById(R.id.PropDamaged);
        Witness1Name = rootView.findViewById(R.id.WitnessName1);
        Witness1Phone = rootView.findViewById(R.id.WitnessPhone1);
        Witness1Addr = rootView.findViewById(R.id.WitnessAddress1);
        Witness2Name = rootView.findViewById(R.id.WitnessName2);
        Witness2Phone = rootView.findViewById(R.id.WitnessPhone2);
        Witness2Addr = rootView.findViewById(R.id.WitnessAddress2);
        Witness3Name = rootView.findViewById(R.id.WitnessName3);
        Witness3Phone = rootView.findViewById(R.id.WitnessPhone3);
        Witness3Addr = rootView.findViewById(R.id.WitnessAddress3);

        context = getActivity();
        myDb = new DatabaseHelper(context);
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();

        PropName.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCPROPNAME)));
        PropAddr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCPROPADDRESS)));
        PropDamaged.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCPROPDESC)));
        Witness1Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSNAME1)));
        Witness1Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSPHONE1)));
        Witness1Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSADDR1)));
        Witness2Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSNAME2)));
        Witness2Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSPHONE2)));
        Witness2Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSADDR2)));
        Witness3Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSNAME3)));
        Witness3Phone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSPHONE3)));
        Witness3Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCWITNESSADDR3)));

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_5 fragment = new Accident_Frag_5();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 5", bundle);
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
                Accident_Frag_3 fragment = new Accident_Frag_3();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 3", bundle);
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
        String propName = PropName.getText().toString();
        String propAddr = PropAddr.getText().toString();
        String propDamaged = PropDamaged.getText().toString();
        String name1 = Witness1Name.getText().toString();
        String addr1 = Witness1Addr.getText().toString();
        String phone1 = Witness1Phone.getText().toString();
        String name2 = Witness2Name.getText().toString();
        String addr2 = Witness2Addr.getText().toString();
        String phone2 = Witness2Phone.getText().toString();
        String name3 = Witness3Name.getText().toString();
        String addr3 = Witness3Addr.getText().toString();
        String phone3 = Witness3Phone.getText().toString();
        myDb.saveAccidentPage4(AccidentID, propName, propAddr, propDamaged, name1, phone1, addr1, name2, phone2, addr2, name3, phone3, addr3);
    }
}


