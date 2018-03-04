package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.kyanogen.signatureview.SignatureView;

public final class Accident_Frag_6 extends Fragment
{
    public static Accident_Frag_6 newInstance() {
        return new Accident_Frag_6();
    }
    public static EditText PoliceDept;
    public static EditText OfficeName;
    public static EditText OfficerBadge;
    public static EditText OfficerPhone;
    public static EditText Citation;
    public static RadioGroup ReportMade;
    public static EditText ReportNo;
    public static SignatureView AccDrawing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub__accident06, container,
                false);
        PoliceDept = view.findViewById(R.id.AccPoliceDept);
        OfficeName = view.findViewById(R.id.AccOfficerName);
        OfficerBadge = view.findViewById(R.id.AccOfficerBadge);
        OfficerPhone = view.findViewById(R.id.AccOfficerPhone);
        Citation = view.findViewById(R.id.AccCitation);
        ReportMade = view.findViewById(R.id.ReportMade);
        ReportNo = view.findViewById(R.id.AccReportNumber);
        AccDrawing = view.findViewById(R.id.accident_drawing);

        return view;
    }
}


