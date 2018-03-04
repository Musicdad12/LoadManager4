package com.jrschugel.loadmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.HashMap;

/**
 * Created by seanm on 2/11/2018.
 * Copyright 2017. All rights reserved.
 */
public class FuelStopDialog extends AppCompatDialogFragment {
    private Switch swUnlimited, swLimited, swReefer;
    private Switch swLoves, swPilot, swFlyingJ, swTA, swPetro, swKwikTrip, swQuikTrip, swInd;
    private FuelStopDialog.FilterListener listener;
    SessionManager session;
    private final String KEY_UNLIMITED = "filterUnlimited";
    private final String KEY_LIMITED = "filterLimited";
    private final String KEY_REEFER = "filterReefer";
    private final String KEY_LOVES = "filterLoves";
    private final String KEY_PILOT = "filterPilot";
    private final String KEY_FLYINGJ = "filterFlyingJ";
    private final String KEY_TA = "filterTA";
    private final String KEY_PETRO = "filterPetro";
    private final String KEY_KWIKTRIP = "filterKwikTrip";
    private final String KEY_QUIKTRIP = "filterQuikTrip";
    private final String KEY_INDEPENDENT = "filterIndependent";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_map, null);

        builder.setView(view)
                .setTitle("Filter Map View")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean booUnlimited = swUnlimited.isChecked();
                        Boolean booLimited = swLimited.isChecked();
                        Boolean booReefer = swReefer.isChecked();
                        Boolean booLoves = swLoves.isChecked();
                        Boolean booPilot = swPilot.isChecked();
                        Boolean booFlyingJ = swFlyingJ.isChecked();
                        Boolean booTA = swTA.isChecked();
                        Boolean booPetro = swPetro.isChecked();
                        Boolean booKwikTrip = swKwikTrip.isChecked();
                        Boolean booQuikTrip = swQuikTrip.isChecked();
                        Boolean booInd = swInd.isChecked();

                        listener.PassFilterInfo(booUnlimited, booLimited, booReefer, booLoves, booPilot, booFlyingJ,
                                                    booTA, booPetro, booKwikTrip, booQuikTrip, booInd);
                    }
                });

        swUnlimited = view.findViewById(R.id.swUnlimited);
        swLimited = view.findViewById(R.id.swLimited);
        swReefer = view.findViewById(R.id.swReefer);
        swLoves = view.findViewById(R.id.swLoves);
        swPilot = view.findViewById(R.id.swPilot);
        swFlyingJ = view.findViewById(R.id.swFlyingJ);
        swTA = view.findViewById(R.id.swTA);
        swPetro = view.findViewById(R.id.swPetro);
        swKwikTrip = view.findViewById(R.id.swKwikTrip);
        swQuikTrip = view.findViewById(R.id.swQuikTrip);
        swInd = view.findViewById(R.id.swInd);

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        swUnlimited.setChecked(Boolean.valueOf(user.get(KEY_UNLIMITED)));
        swLimited.setChecked(Boolean.valueOf(user.get(KEY_LIMITED)));
        swReefer.setChecked(Boolean.valueOf(user.get(KEY_REEFER)));
        swLoves.setChecked(Boolean.valueOf(user.get(KEY_LOVES)));
        swPilot.setChecked(Boolean.valueOf(user.get(KEY_PILOT)));
        swFlyingJ.setChecked(Boolean.valueOf(user.get(KEY_FLYINGJ)));
        swTA.setChecked(Boolean.valueOf(user.get(KEY_TA)));
        swPetro.setChecked(Boolean.valueOf(user.get(KEY_PETRO)));
        swKwikTrip.setChecked(Boolean.valueOf(user.get(KEY_KWIKTRIP)));
        swQuikTrip.setChecked(Boolean.valueOf(user.get(KEY_QUIKTRIP)));
        swInd.setChecked(Boolean.valueOf(user.get(KEY_INDEPENDENT)));

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FuelStopDialog.FilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement AddCustomerListener");
        }
    }

    public interface FilterListener{
        void PassFilterInfo(Boolean booUnlimited, Boolean booLimited, Boolean booReefer, Boolean booLoves, Boolean booPilot, Boolean booFlyingJ,
                              Boolean booTA, Boolean booPetro, Boolean booKwikTrip, Boolean booQuikTrip, Boolean booInd);

    }
}
