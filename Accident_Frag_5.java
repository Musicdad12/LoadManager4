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

public final class Accident_Frag_5 extends Fragment
{
    public static Accident_Frag_5 newInstance() {
        return new Accident_Frag_5();
    }
    EditText AccDate;
    EditText AccTime;
    EditText AccLocation;
    EditText Driver2Name;
    EditText Driver2License;
    EditText Driver2Addr;
    EditText Driver2Plate;
    EditText Driver2YrMake;
    EditText Driver2Owner;
    EditText Driver2OwnerAddr;
    EditText Driver2OwnerPhone;
    EditText Driver2InsName;
    EditText Driver2InsPolicy;
    EditText Driver3Name;
    EditText Driver3License;
    EditText Driver3Addr;
    EditText Driver3Plate;
    EditText Driver3YrMake;
    EditText Driver3Owner;
    EditText Driver3OwnerAddr;
    EditText Driver3OwnerPhone;
    EditText Driver3InsName;
    EditText Driver3InsPolicy;

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
        View rootView = inflater.inflate(R.layout.fragment_sub__accident05, container,
                false);
        AccDate = rootView.findViewById(R.id.AccdtDate);
        AccTime = rootView.findViewById(R.id.AccdtTime);
        AccLocation = rootView.findViewById(R.id.AccdtLocation);
        Driver2Name = rootView.findViewById(R.id.AccDrv2Name);
        Driver2License = rootView.findViewById(R.id.AccDrv2License);
        Driver2Addr = rootView.findViewById(R.id.AccDrv2Addr);
        Driver2Plate = rootView.findViewById(R.id.AccDrv2Plate);
        Driver2YrMake = rootView.findViewById(R.id.AccDrv2YrMake);
        Driver2Owner = rootView.findViewById(R.id.AccDrv2Owner);
        Driver2OwnerAddr = rootView.findViewById(R.id.AccDrv2OwnerAddr);
        Driver2OwnerPhone = rootView.findViewById(R.id.AccDrv2OwnerPhone);
        Driver2InsName = rootView.findViewById(R.id.AccDrv2InsName);
        Driver2InsPolicy = rootView.findViewById(R.id.AccDrv2InsPolicy);
        Driver3Name = rootView.findViewById(R.id.AccDrv3Name);
        Driver3License = rootView.findViewById(R.id.AccDrv3License);
        Driver3Addr = rootView.findViewById(R.id.AccDrv3Addr);
        Driver3Plate = rootView.findViewById(R.id.AccDrv3Plate);
        Driver3YrMake = rootView.findViewById(R.id.AccDrv3YrMake);
        Driver3Owner = rootView.findViewById(R.id.AccDrv3Owner);
        Driver3OwnerAddr = rootView.findViewById(R.id.AccDrv3OwnerAddr);
        Driver3OwnerPhone = rootView.findViewById(R.id.AccDrv3OwnerPhone);
        Driver3InsName = rootView.findViewById(R.id.AccDrv3InsName);
        Driver3InsPolicy = rootView.findViewById(R.id.AccDrv3InsPolicy);

        context = getActivity();
        myDb = new DatabaseHelper(context);
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();

        AccDate.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCACCIDENTDATE)));
        AccTime.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCACCIDENTTIME)));
        AccLocation.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCACCIDENTLOCATION)));
        Driver2Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1NAME)));
        Driver2License.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1LICENSE)));
        Driver2Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1ADDRESS)));
        Driver2Plate.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1PLATE)));
        Driver2YrMake.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1YRMAKE)));
        Driver2Owner.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1OWNER)));
        Driver2OwnerAddr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1OWNERADDRESS)));
        Driver2OwnerPhone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1OWNERPHONE)));
        Driver2InsName.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1INSURANCENAME)));
        Driver2InsPolicy.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV1INSPOLICYNUMBER)));
        Driver3Name.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2NAME)));
        Driver3License.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2LICENSE)));
        Driver3Addr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2ADDRESS)));
        Driver3Plate.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2PLATE)));
        Driver3YrMake.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2YRMAKE)));
        Driver3Owner.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2OWNER)));
        Driver3OwnerAddr.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2OWNERADDRESS)));
        Driver3OwnerPhone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2OWNERPHONE)));
        Driver3InsName.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2INSURANCENAME)));
        Driver3InsPolicy.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCDRV2INSPOLICYNUMBER)));

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_6 fragment = new Accident_Frag_6();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 6", bundle);
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
                Accident_Frag_4 fragment = new Accident_Frag_4();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 4", bundle);
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
        String accDate = AccDate.getText().toString();
        String accTime = AccTime.getText().toString();
        String accLocation = AccLocation.getText().toString();
        String Drvr2Name = Driver2Name.getText().toString();
        String Drvr2License = Driver2License.getText().toString();
        String Drvr2Addr = Driver2Addr.getText().toString();
        String Drvr2Plate = Driver2Plate.getText().toString();
        String Drvr2YrMake = Driver2YrMake.getText().toString();
        String Drvr2Owner = Driver2Owner.getText().toString();
        String Drvr2OwnerAddr = Driver2OwnerAddr.getText().toString();
        String Drvr2OwnerPhone = Driver2OwnerPhone.getText().toString();
        String Drvr2InsName = Driver2InsName.getText().toString();
        String Drvr2InsPolicy = Driver2InsPolicy.getText().toString();
        String Drvr3Name = Driver3Name.getText().toString();
        String Drvr3License = Driver3License.getText().toString();
        String Drvr3Addr = Driver3Addr.getText().toString();
        String Drvr3Plate = Driver3Plate.getText().toString();
        String Drvr3YrMake = Driver3YrMake.getText().toString();
        String Drvr3Owner = Driver3Owner.getText().toString();
        String Drvr3OwnerAddr = Driver3OwnerAddr.getText().toString();
        String Drvr3OwnerPhone = Driver3OwnerPhone.getText().toString();
        String Drvr3InsName = Driver3InsName.getText().toString();
        String Drvr3InsPolicy = Driver3InsPolicy.getText().toString();

        myDb.saveAccidentPage5(AccidentID, accDate, accTime, accLocation,
                Drvr2Name, Drvr2License, Drvr2Addr, Drvr2Plate, Drvr2YrMake, Drvr2Owner, Drvr2OwnerAddr, Drvr2OwnerPhone, Drvr2InsName, Drvr2InsPolicy,
                Drvr3Name, Drvr3License, Drvr3Addr, Drvr3Plate, Drvr3YrMake, Drvr3Owner, Drvr3OwnerAddr, Drvr3OwnerPhone, Drvr3InsName, Drvr3InsPolicy);
    }
}


