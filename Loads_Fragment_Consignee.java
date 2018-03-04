package com.jrschugel.loadmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

public class Loads_Fragment_Consignee extends Fragment {

    DatabaseHelperStops myDb2;
    Integer LoadNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sub__fragment03, container, false);

        Bundle b = getArguments();
        if (b != null) {
            LoadNumber = Integer.parseInt(b.getString("LoadNumber"));
        }

        myDb2 = new DatabaseHelperStops(getContext());

        JSONArray jsonArray = myDb2.getLoadsDataJSON(getContext(),2, LoadNumber);

        ListView LoadsList = rootView.findViewById(R.id.lstConsignee);

        JSONAdapterLoadsShip jSONAdapter = new JSONAdapterLoadsShip (getActivity(),jsonArray);//jArray is your json array

        //Set the above adapter as the adapter of choice for our list
        LoadsList.setAdapter(jSONAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public String getShownLoad() {
        // Returns the index assigned
        return LoadsListAdapter.mCurrentLoadNumber;
    }


}
