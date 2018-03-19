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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kyanogen.signatureview.SignatureView;

public final class Accident_Frag_6 extends Fragment
{
    public static Accident_Frag_6 newInstance() {
        return new Accident_Frag_6();
    }
    EditText PoliceDept;
    EditText OfficeName;
    EditText OfficerBadge;
    EditText OfficerPhone;
    EditText Citation;
    RadioGroup ReportMade;
    EditText ReportNo;
    SignatureView AccDrawing;
    
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
        View rootView = inflater.inflate(R.layout.fragment_sub__accident06, container,
                false);
        PoliceDept = rootView.findViewById(R.id.AccPoliceDept);
        OfficeName = rootView.findViewById(R.id.AccOfficerName);
        OfficerBadge = rootView.findViewById(R.id.AccOfficerBadge);
        OfficerPhone = rootView.findViewById(R.id.AccOfficerPhone);
        Citation = rootView.findViewById(R.id.AccCitation);
        ReportMade = rootView.findViewById(R.id.ReportMade);
        ReportNo = rootView.findViewById(R.id.AccReportNumber);
        AccDrawing = rootView.findViewById(R.id.accident_drawing);

        context = getActivity();
        myDb = new DatabaseHelper(context);
        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        Cursor accList = myDb.getAccidents(AccidentID);
        accList.moveToFirst();
        Bitmap bitDrawing = null;

        PoliceDept.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCPOLICEDEPT)));
        OfficeName.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCOFFICERNAME)));
        OfficerBadge.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCOFFICERBADGE)));
        OfficerPhone.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCOFFICERPHONE)));
        Citation.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCCITATION)));
        ReportMade.check(accList.getInt(accList.getColumnIndex(Config.TAG_ACCREPORTMADE)));
        ReportNo.setText(accList.getString(accList.getColumnIndex(Config.TAG_ACCREPORTNUMBER)));

        byte[] Drawing = accList.getBlob( accList.getColumnIndex(Config.TAG_ACCDRAWING) );
        if (Drawing != null) {
            bitDrawing = DbBitmapUtility.getImage(Drawing);
        }
        Drawable drawDrawing = new BitmapDrawable(getResources(), bitDrawing);
        AccDrawing.setBackground(drawDrawing);

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SaveData();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_7 fragment = new Accident_Frag_7();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 7", bundle);
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
                Accident_Frag_5 fragment = new Accident_Frag_5();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 5", bundle);
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

    void SaveData() {
        String police = PoliceDept.getText().toString();
        String offName = OfficeName.getText().toString();
        String offBadge = OfficerBadge.getText().toString();
        String offPhone = OfficerPhone.getText().toString();
        String citation = Citation.getText().toString();
        Integer reportYN = ReportMade.getCheckedRadioButtonId();
        String reportNo = ReportNo.getText().toString();
        Bitmap drawing = AccDrawing.getSignatureBitmap();
        byte[] sketch = DbBitmapUtility.getBytes(drawing);
        myDb.saveAccidentPage6(AccidentID, police, offName, offBadge, offPhone, citation, reportYN, reportNo, sketch);
    }
}


