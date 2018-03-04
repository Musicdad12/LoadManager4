package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kyanogen.signatureview.SignatureView;


public final class Accident_Frag_2 extends Fragment
{
    public static Accident_Frag_2 newInstance() {
        return new Accident_Frag_2();
    }

    public static SignatureView AccSignExonerate;   //change this to public static
    public static SignatureView AccSignExonWitness;
    public static EditText exonAddr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident02, container,
                false);
        AccSignExonerate = view.findViewById(R.id.signature_exonerate);
        AccSignExonWitness = view.findViewById(R.id.signature_exonerate_witness);
        exonAddr = view.findViewById(R.id.exonAddress);

        return view;
    }
}


