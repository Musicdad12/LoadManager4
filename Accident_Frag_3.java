package com.jrschugel.loadmanager;

import android.os.Bundle;
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
    public static EditText DrvName;
    public static EditText TruckNo;
    public static EditText TrlrNo;
    public static EditText TripNo;
    public static EditText Injured1Name;
    public static EditText Injured1Phone;
    public static EditText Injured1Addr;
    public static EditText Injured1Age;
    public static EditText Injured2Name;
    public static EditText Injured2Phone;
    public static EditText Injured2Addr;
    public static EditText Injured2Age;
    public static EditText Injured3Name;
    public static EditText Injured3Phone;
    public static EditText Injured3Addr;
    public static EditText Injured3Age;
    public static EditText Injured4Name;
    public static EditText Injured4Phone;
    public static EditText Injured4Addr;
    public static EditText Injured4Age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident03, container,
                false);
        DrvName = view.findViewById(R.id.DriverName);
        TruckNo = view.findViewById(R.id.TruckNo);
        TrlrNo = view.findViewById(R.id.TrailerNo);
        TripNo = view.findViewById(R.id.TripNo);
        Injured1Name = view.findViewById(R.id.InjuredName1);
        Injured1Phone = view.findViewById(R.id.InjuredPhone1);
        Injured1Addr = view.findViewById(R.id.InjuredAddress1);
        Injured1Age = view.findViewById(R.id.InjuredAge1);
        Injured2Name = view.findViewById(R.id.InjuredName2);
        Injured2Phone = view.findViewById(R.id.InjuredPhone2);
        Injured2Addr = view.findViewById(R.id.InjuredAddress2);
        Injured2Age = view.findViewById(R.id.InjuredAge2);
        Injured3Name = view.findViewById(R.id.InjuredName3);
        Injured3Phone = view.findViewById(R.id.InjuredPhone3);
        Injured3Addr = view.findViewById(R.id.InjuredAddress3);
        Injured3Age = view.findViewById(R.id.InjuredAge3);
        Injured4Name = view.findViewById(R.id.InjuredName4);
        Injured4Phone = view.findViewById(R.id.InjuredPhone4);
        Injured4Addr = view.findViewById(R.id.InjuredAddress4);
        Injured4Age = view.findViewById(R.id.InjuredAge4);

        return view;
    }
}


