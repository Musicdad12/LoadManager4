package com.jrschugel.loadmanager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

public class Loads_Frag_2 extends Fragment {

    DatabaseHelperStops myDb2;
    Integer LoadNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sub__fragment02, container, false);

        getResources().getConfiguration();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LoadNumber = Integer.parseInt(getShownLoad());
        } else {
            LoadNumber = Integer.parseInt(getShownLoad());
        }
        if (LoadNumber == null) {
            return rootView;
        }
        LoadNumber = Integer.parseInt(LoadsListAdapter.mCurrentLoadNumber);

        myDb2 = new DatabaseHelperStops(getContext());

        JSONArray jsonArray = myDb2.getLoadsDataJSON(getContext(),1, LoadNumber);

        ListView LoadsList = rootView.findViewById(R.id.lstShipper);

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
