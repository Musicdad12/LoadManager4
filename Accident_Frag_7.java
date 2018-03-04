package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class Accident_Frag_7 extends Fragment
{
    public static Accident_Frag_7 newInstance() {
        return new Accident_Frag_7();
    }
    public static EditText Weather;
    public static EditText Narrative;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident07, container,
                false);
        Weather = view.findViewById(R.id.AccConditions);
        Narrative = view.findViewById(R.id.AccNarrative);

        return view;
    }
}


