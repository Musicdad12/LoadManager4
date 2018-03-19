package com.jrschugel.loadmanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kyanogen.signatureview.*;


public final class Accident_Frag_2 extends Fragment
{
    public static Accident_Frag_2 newInstance() {
        return new Accident_Frag_2();
    }

    public static SignatureView AccSignExonerate;   //change this to public static
    public static SignatureView AccSignExonWitness;
    public static EditText exonAddr;
    Context context;
    private INavActivity2 mINavActivity;
    DatabaseHelper myDb;
    Long AccidentID;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mINavActivity = (INavActivity2) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub__accident02, container,
                false);
        context = getActivity();
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        myDb = new DatabaseHelper(context);

        AccSignExonerate = rootView.findViewById(R.id.signature_exonerate);
        AccSignExonWitness = rootView.findViewById(R.id.signature_exonerate_witness);
        exonAddr = rootView.findViewById(R.id.exonAddress);

        Bitmap bitSignature = null, bitWitness = null;
        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();
        byte[] Signature = accList.getBlob( accList.getColumnIndex(Config.TAG_EXON_SIGNATURE) );
        byte[] Witness = accList.getBlob(accList.getColumnIndex(Config.TAG_EXON_WITNESS));

        if (Signature != null) {
            bitSignature = DbBitmapUtility.getImage(Signature);
        }
        if (Witness != null) {
            bitWitness = DbBitmapUtility.getImage(Witness);
        }
        Drawable drawSignature = new BitmapDrawable(getResources(), bitSignature);
        Drawable drawWitness = new BitmapDrawable(getResources(), bitWitness);
        AccSignExonerate.setBackground(drawSignature);
        AccSignExonWitness.setBackground(drawWitness);
        exonAddr.setText(accList.getString(accList.getColumnIndex(Config.TAG_EXON_ADDRESS)));

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_3 fragment = new Accident_Frag_3();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 3", bundle);
            }
        });

        FloatingActionButton fabPrev = rootView.findViewById(R.id.fabPrev);
        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Previous Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_1 fragment = new Accident_Frag_1();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 1", bundle);

            }
        });

        FloatingActionButton fabExit = rootView.findViewById(R.id.fabExit);
        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save / Exit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                AccidentFragment fragment = new AccidentFragment();
                mINavActivity.inflateFragment(fragment, "Accident Report", bundle);


            }
        });
        return rootView;
    }

    void SaveData () {
        Bitmap bitSignature = AccSignExonerate.getSignatureBitmap();
        Bitmap bitWitness = AccSignExonWitness.getSignatureBitmap();
        String Address = exonAddr.getText().toString();

        byte[] Signature = DbBitmapUtility.getBytes(bitSignature);
        byte[] Witness = DbBitmapUtility.getBytes(bitWitness);

        myDb.saveAccidentPage2(AccidentID, Signature, Witness, Address);
    }
}


