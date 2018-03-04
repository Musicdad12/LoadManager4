package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class xFragmentLayout extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for fragment_layout.xml
        setContentView(R.layout.xfragment_layout);



    }

}