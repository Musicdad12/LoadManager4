package com.jrschugel.loadmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.santalu.maskedittext.MaskEditText;

/**
 * Created by seanm on 1/24/2018.
 * Copyright 2017. All rights reserved.
 */

public class AddCustomerDialog extends AppCompatDialogFragment {
    private EditText etCustName, etCustAddr1, etCustAddr2, etCustCity, etCustContact, etCustComment;
    private Spinner spinCustState;
    private MaskEditText etCustPhone;
    private AddCustomerListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_customer, null);

        builder.setView(view)
                .setTitle("Add Customer")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String custName = etCustName.getText().toString();
                        String custAddr1 = etCustAddr1.getText().toString();
                        String custAddr2 = etCustAddr2.getText().toString();
                        String custCity = etCustCity.getText().toString();
                        String custState = spinCustState.getSelectedItem().toString();
                        String custPhone = etCustPhone.getRawText();
                        String custContact = etCustContact.getText().toString();
                        String custComment = etCustComment.getText().toString();

                        listener.PassCustomerInfo(custName, custAddr1, custAddr2, custCity, custState, custPhone, custContact, custComment);
                    }
                });

        etCustName = view.findViewById(R.id.etCustomerName);
        etCustAddr1 = view.findViewById(R.id.etAddress1);
        etCustAddr2 = view.findViewById(R.id.etAddress2);
        etCustCity = view.findViewById(R.id.etCity);
        spinCustState = view.findViewById(R.id.spinState);
        etCustPhone = view.findViewById(R.id.etPhone);
        etCustContact = view.findViewById(R.id.etContact);
        etCustComment = view.findViewById(R.id.etCustComments);

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddCustomerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement AddCustomerListener");
        }
    }

    public interface AddCustomerListener{
        void PassCustomerInfo(String CustomerName, String CustomerAddr1, String CustomerAddr2, String CustomerCity,
                              String CustomerState, String CustomerPhone, String CustomerContact, String CustomerComment);

    }
}
