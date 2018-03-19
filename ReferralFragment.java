package com.jrschugel.loadmanager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;

public class ReferralFragment extends Fragment {
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_referral, container, false);
        context = getActivity();

        SessionManager session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();

        final String FullName = user.get(SessionManager.KEY_NAME);
        final String TruckNumber = user.get(SessionManager.KEY_TRUCK);
        final EditText RefName = rootView.findViewById(R.id.RefName);
        final EditText RefPhone = rootView.findViewById(R.id.RefPhone);
        final EditText RefComment = rootView.findViewById(R.id.RefComments);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send referral information to Recruiter", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                String Body = "This is a driver referral from " + FullName + " in Truck # " + TruckNumber + ".\n";
                Body = Body + "Please contact this person about a possible position as a driver for J&R Schugel.\n\n";
                Body = Body + "Referral Name: " + RefName.getText().toString();
                Body = Body + "\nPhone Number:  " + RefPhone.getText().toString();
                Body = Body + "\nComments:      " + RefComment.getText().toString();
                Body = Body + "\n\nIf this person if hired at J&R Schugel, please credit this driver with the referral.";
                Body = Body + "\n\nThank you";
                intent.putExtra(Intent.EXTRA_SUBJECT, "Driver referral from Driver " + FullName);
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.setDataAndType(Uri.parse("mailto:musicdad2014@gmail.com"), "text/plain"); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });

        return rootView;
    }
}
