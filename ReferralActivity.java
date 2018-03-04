package com.jrschugel.loadmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class ReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SessionManager session = new SessionManager(this);
        HashMap<String, String> user = session.getUserDetails();

        final String FullName = user.get(SessionManager.KEY_NAME);
        final String TruckNumber = user.get(SessionManager.KEY_TRUCK);
        final EditText RefName = findViewById(R.id.RefName);
        final EditText RefPhone = findViewById(R.id.RefPhone);
        final EditText RefComment = findViewById(R.id.RefComments);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send referral information to Recruiter", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                String Body = "This is a driver referral from " + FullName +" in Truck # " + TruckNumber + ".\n";
                Body = Body + "Please contact this person about a possible position as a driver for J&R Schugel.\n\n";
                Body = Body + "Referral Name: " + RefName.getText().toString();
                Body = Body + "\nPhone Number:  " + RefPhone.getText().toString();
                Body = Body + "\nComments:      " + RefComment.getText().toString();
                Body = Body + "\n\nIf this person if hired at J&R Schugel, please credit this driver with the referral.";
                Body = Body + "\n\nThank you";
                intent.putExtra(Intent.EXTRA_SUBJECT, "Driver referral from Driver " + FullName);
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.setData(Uri.parse("mailto:musicdad2014@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
                finish();
            }
        });
    }

}
