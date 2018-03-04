package com.jrschugel.loadmanager;

import android.os.Bundle;
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
    public static EditText AccDate;
    public static EditText AccTime;
    public static EditText AccLocation;
    public static EditText Driver2Name;
    public static EditText Driver2License;
    public static EditText Driver2Addr;
    public static EditText Driver2Plate;
    public static EditText Driver2YrMake;
    public static EditText Driver2Owner;
    public static EditText Driver2OwnerAddr;
    public static EditText Driver2OwnerPhone;
    public static EditText Driver2InsName;
    public static EditText Driver2InsPolicy;
    public static EditText Driver3Name;
    public static EditText Driver3License;
    public static EditText Driver3Addr;
    public static EditText Driver3Plate;
    public static EditText Driver3YrMake;
    public static EditText Driver3Owner;
    public static EditText Driver3OwnerAddr;
    public static EditText Driver3OwnerPhone;
    public static EditText Driver3InsName;
    public static EditText Driver3InsPolicy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident05, container,
                false);
        AccDate = view.findViewById(R.id.AccdtDate);
        AccTime = view.findViewById(R.id.AccdtTime);
        AccLocation = view.findViewById(R.id.AccdtLocation);
        Driver2Name = view.findViewById(R.id.AccDrv2Name);
        Driver2License = view.findViewById(R.id.AccDrv2License);
        Driver2Addr = view.findViewById(R.id.AccDrv2Addr);
        Driver2Plate = view.findViewById(R.id.AccDrv2Plate);
        Driver2YrMake = view.findViewById(R.id.AccDrv2YrMake);
        Driver2Owner = view.findViewById(R.id.AccDrv2Owner);
        Driver2OwnerAddr = view.findViewById(R.id.AccDrv2OwnerAddr);
        Driver2OwnerPhone = view.findViewById(R.id.AccDrv2OwnerPhone);
        Driver2InsName = view.findViewById(R.id.AccDrv2InsName);
        Driver2InsPolicy = view.findViewById(R.id.AccDrv2InsPolicy);
        Driver3Name = view.findViewById(R.id.AccDrv3Name);
        Driver3License = view.findViewById(R.id.AccDrv3License);
        Driver3Addr = view.findViewById(R.id.AccDrv3Addr);
        Driver3Plate = view.findViewById(R.id.AccDrv3Plate);
        Driver3YrMake = view.findViewById(R.id.AccDrv3YrMake);
        Driver3Owner = view.findViewById(R.id.AccDrv3Owner);
        Driver3OwnerAddr = view.findViewById(R.id.AccDrv3OwnerAddr);
        Driver3OwnerPhone = view.findViewById(R.id.AccDrv3OwnerPhone);
        Driver3InsName = view.findViewById(R.id.AccDrv3InsName);
        Driver3InsPolicy = view.findViewById(R.id.AccDrv3InsPolicy);

        return view;
    }
}


