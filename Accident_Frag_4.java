package com.jrschugel.loadmanager;

import android.os.Bundle;
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

    public static EditText PropName;
    public static EditText PropAddr;
    public static EditText PropDamaged;
    public static EditText Witness1Name;
    public static EditText Witness1Phone;
    public static EditText Witness1Addr;
    public static EditText Witness2Name;
    public static EditText Witness2Phone;
    public static EditText Witness2Addr;
    public static EditText Witness3Name;
    public static EditText Witness3Phone;
    public static EditText Witness3Addr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident04, container,
                false);
        PropName = view.findViewById(R.id.PropOwner);
        PropAddr = view.findViewById(R.id.PropOwnerAddr);
        PropDamaged = view.findViewById(R.id.PropDamaged);
        Witness1Name = view.findViewById(R.id.WitnessName1);
        Witness1Phone = view.findViewById(R.id.WitnessPhone1);
        Witness1Addr = view.findViewById(R.id.WitnessAddress1);
        Witness2Name = view.findViewById(R.id.WitnessName2);
        Witness2Phone = view.findViewById(R.id.WitnessPhone2);
        Witness2Addr = view.findViewById(R.id.WitnessAddress2);
        Witness3Name = view.findViewById(R.id.WitnessName3);
        Witness3Phone = view.findViewById(R.id.WitnessPhone3);
        Witness3Addr = view.findViewById(R.id.WitnessAddress3);

        return view;
    }
}


