package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Loads_Fragment extends Fragment {

    DatabaseHelper myDb;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView != null){
            if(rootView.getParent()!=null)
                ((ViewGroup)rootView.getParent()).removeView(rootView);
            return rootView;
        }
        return inflater.inflate(R.layout.fragment_loads, container, false);
    }
}