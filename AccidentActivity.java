package com.jrschugel.loadmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AccidentActivity extends AppCompatActivity {

    private FragmentAdapter adapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        AccidentActivity.SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send Report to Safety Department", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Accident Report for Truck "+ Accident_Frag_3.TruckNo.getText().toString()+" Driven by "+Accident_Frag_3.DrvName.getText().toString());
                String Body = "This is an accident report by " + Accident_Frag_3.DrvName.getText().toString() + " concerning an accident that happened on " +
                        Accident_Frag_5.AccDate.getText().toString() + ". The truck number is " + Accident_Frag_3.TruckNo.getText().toString() + ", and the trailer involved was " +
                        Accident_Frag_3.TrlrNo.getText().toString() + ". The incident happened during Trip number " + Accident_Frag_3.TripNo.getText().toString() +
                        ". The rest of the details follow. \n\n ";
                if (!isEmpty(Accident_Frag_3.Injured1Name)) {
                    Body = Body + "Injured Person #1: " +
                            "\nName:    " + Accident_Frag_3.Injured1Name.getText().toString() +
                            "\nPhone:   " + Accident_Frag_3.Injured1Phone.getText().toString() +
                            "\nAddress: " + Accident_Frag_3.Injured1Addr.getText().toString() +
                            "\nAge:     " + Accident_Frag_3.Injured1Age.getText().toString() + "\n\n";
                }
                if (!isEmpty(Accident_Frag_3.Injured1Name)) {
                    String Injured2 = "\nName:    " + Accident_Frag_3.Injured2Name.getText().toString() +
                            "\nPhone:   " + Accident_Frag_3.Injured2Phone.getText().toString() +
                            "\nAddress: " + Accident_Frag_3.Injured2Addr.getText().toString() +
                            "\nAge:     " + Accident_Frag_3.Injured2Age.getText().toString() + "\n\n";
                    Body = Body + "Injured Person #2: " + Injured2;
                }
                if (!isEmpty(Accident_Frag_3.Injured3Name)) {
                    String Injured3 = "\nName:    " + Accident_Frag_3.Injured3Name.getText().toString() +
                                    "\nPhone:   " + Accident_Frag_3.Injured3Phone.getText().toString() +
                                    "\nAddress: " + Accident_Frag_3.Injured3Addr.getText().toString() +
                                    "\nAge:     " + Accident_Frag_3.Injured3Age.getText().toString() + "\n\n";
                    Body = Body + "Injured Person #3: " + Injured3;
                }
                if (!isEmpty(Accident_Frag_3.Injured4Name)) {
                    String Injured4 = "\nName:    " + Accident_Frag_3.Injured4Name.getText().toString() +
                            "\nPhone:   " + Accident_Frag_3.Injured4Phone.getText().toString() +
                            "\nAddress: " + Accident_Frag_3.Injured4Addr.getText().toString() +
                            "\nAge:     " + Accident_Frag_3.Injured4Age.getText().toString() + "\n\n";
                    Body = Body + "Injured Person #4: " + Injured4;
                }
                if (!isEmpty(Accident_Frag_4.PropName)){
                    Body = Body + "PROPERTY DAMAGED\nOwner: " +
                            Accident_Frag_4.PropName.getText().toString() +
                            "\nAddress: " + Accident_Frag_4.PropAddr.getText().toString() +
                            "\nWhat property os damaged? " + Accident_Frag_4.PropDamaged.getText().toString() + "\n\n";
                }
                if (!isEmpty(Accident_Frag_4.Witness1Name)) {
                    Body = Body + "Witness #1: \n Name:    " + Accident_Frag_4.Witness1Name.getText().toString() +
                            "\nPhone:   " + Accident_Frag_4.Witness1Phone.getText().toString() +
                            "\nAddress: " + Accident_Frag_4.Witness1Addr.getText().toString() + "\n\n";
                }
                if (!isEmpty(Accident_Frag_4.Witness2Name)) {
                    Body = Body + "Witness #2: \n Name:    " + Accident_Frag_4.Witness2Name.getText().toString() +
                            "\nPhone:   " + Accident_Frag_4.Witness2Phone.getText().toString() +
                            "\nAddress: " + Accident_Frag_4.Witness2Addr.getText().toString() + "\n\n";
                }
                Body = Body + "THE ACCIDENT\n\n" +
                        "Date: " + Accident_Frag_5.AccDate.getText().toString() + "          Time: " + Accident_Frag_5.AccTime.getText().toString() +
                        "\nAddress: " + Accident_Frag_5.AccLocation.getText().toString() + "\n\n";
                if (!isEmpty(Accident_Frag_5.Driver2Name)) {
                    Body = Body + "#2 Drivers Name: " + Accident_Frag_5.Driver2Name.getText().toString() +
                            "\nLicense No.: " + Accident_Frag_5.Driver2License.getText().toString() +
                            "\nAddress:     " + Accident_Frag_5.Driver2Addr.getText().toString() +
                            "\nVehicle License #: " + Accident_Frag_5.Driver2Plate.getText().toString() + "     Yr/Make veh: " + Accident_Frag_5.Driver2YrMake.getText().toString() +
                            "\nOwner:       " + Accident_Frag_5.Driver2Owner.getText().toString() +
                            "\nAddress:     " + Accident_Frag_5.Driver2OwnerAddr.getText().toString() +
                            "\nPhone:       " + Accident_Frag_5.Driver2OwnerPhone.getText().toString() +
                            "\nInsurance:   " + Accident_Frag_5.Driver2InsName.getText().toString() +
                            "\nPolicy #:    " + Accident_Frag_5.Driver2InsPolicy.getText().toString() + "\n\n";
                }
                if (!isEmpty(Accident_Frag_5.Driver3Name)) {
                    Body = Body + "#3 Drivers Name: " + Accident_Frag_5.Driver3Name.getText().toString() +
                            "\nLicense No.: " + Accident_Frag_5.Driver3License.getText().toString() +
                            "\nAddress:     " + Accident_Frag_5.Driver3Addr.getText().toString() +
                            "\nVehicle License #: " + Accident_Frag_5.Driver3Plate.getText().toString() + "     Yr/Make veh: " + Accident_Frag_5.Driver3YrMake.getText().toString() +
                            "\nOwner:       " + Accident_Frag_5.Driver3Owner.getText().toString() +
                            "\nAddress:     " + Accident_Frag_5.Driver3OwnerAddr.getText().toString() +
                            "\nPhone:       " + Accident_Frag_5.Driver3OwnerPhone.getText().toString() +
                            "\nInsurance:   " + Accident_Frag_5.Driver3InsName.getText().toString() +
                            "\nPolicy #:    " + Accident_Frag_5.Driver3InsPolicy.getText().toString() + "\n\n";
                }
                Body = Body + "Police Department: " + Accident_Frag_6.PoliceDept.getText().toString() +
                        "\nOfficer: " + Accident_Frag_6.OfficeName.getText().toString() + "     Badge #: " + Accident_Frag_6.OfficerBadge.getText().toString() +
                        "\nPhone:   " + Accident_Frag_6.OfficerPhone.getText().toString() +
                        "\nWas anyone given a citation or arrest? if so what were the charges? \n" +
                            Accident_Frag_6.Citation.getText().toString() + "\n";
                        if (Accident_Frag_6.ReportMade.getCheckedRadioButtonId() == R.id.AccReportYes){
                            Body = Body + "Yes, " + Accident_Frag_6.ReportNo.getText().toString() + "\n\n";
                        } else {
                            Body = Body + "No report was made.\n\n";
                        }
                Body = Body + "Weather Conditions (Snow, rain, fog, etc) : " + Accident_Frag_7.Weather.getText().toString() + "\n\n" +
                        "Explain in your own words what happened: " + Accident_Frag_7.Narrative.getText().toString() +"\n\n";

                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.setData(Uri.parse("mailto:musicdad2014@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
});
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;

    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Accident_Frag_1();
                case 1:
                    return new Accident_Frag_2();
                case 2:
                    return new Accident_Frag_3();
                case 3:
                    return new Accident_Frag_4();
                case 4:
                    return new Accident_Frag_5();
                case 5:
                    return new Accident_Frag_6();
                case 6:
                    return new Accident_Frag_7();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Instructions";
                case 1:
                    return "Exoneration";
                case 2:
                    return "Injured";
                case 3:
                    return "Witnesses";
                case 4:
                    return "Other Drivers";
                case 5:
                    return "Police";
                case 6:
                    return "Comments";
            }
            return null;
        }
    }
}
