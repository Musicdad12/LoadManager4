package com.jrschugel.loadmanager;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

// Shows the title fragment which is a ListView
// When a ListView item is selected we will put the DetailsFragment in the
// Framelayout if we are in horizontal mode, or we will create a DetailsActivity // and switch to it if in portrait mode
public class zTitlesFragment extends Fragment {

    // True or False depending on if we are in horizontal or duel pane mode
    boolean mDuelPane;
    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    ListView lstView;
    RecyclerView recyclerView;
    Context context;
    private static final String TAG = "zTitlesFragment";

    // Currently selected item in the ListView
    int mCurCheckPosition = 0;
    String mCurSelectedLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.zfragment_titles, container, false);
        //lstView = rootView.findViewById(R.id.custom_list);
        recyclerView = rootView.findViewById(R.id.custom_list);
        context = getActivity();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // An ArrayAdapter connects the array to our ListView
        // getActivity() returns a Context so we have the resources needed
        // We pass a default list item text view to put the data in and the
        // array
        myDb2 = new DatabaseHelperStops(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<LoadsList> loadsLists = new ArrayList<>();


        JSONArray jsonArray = myDb2.getAllLoadsList(getActivity());
        ArrayList<String> listdata = new ArrayList<String>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Log.d(TAG, "onActivityCreated: " + jsonArray.getString(i));
                    listdata.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
/*

        JSONAdapterLoads jSONAdapter = new JSONAdapterLoads (getActivity(), getActivity(), jsonArray);//jArray is your json array

        //Set the above adapter as the adapter of choice for our list
        lstView.setAdapter(jSONAdapter);

        //Handle what happens when a load is selected from the list
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvLoadNumber = view.findViewById(R.id.tvLoadNumber);
                mCurSelectedLoad = tvLoadNumber.getText().toString();
                //myDb.updateViewedLoad(Integer.parseInt(mCurSelectedLoad), 1);

                //your code here
                showDetails(mCurCheckPosition);
            }

        });
*/

        // Check if the FrameLayout with the id details exists
        View detailsFrame = getActivity().findViewById(R.id.details);

        // Set mDuelPane based on whether you are in the horizontal layout
        // Check if the detailsFrame exists and if it is visible
        mDuelPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        // If the screen is rotated onSaveInstanceState() below will store the // hero most recently selected. Get the value attached to curChoice and // store it in mCurCheckPosition
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDuelPane) {
            // CHOICE_MODE_SINGLE allows one item in the ListView to be selected at a time
            // CHOICE_MODE_MULTIPLE allows multiple
            // CHOICE_MODE_NONE is the default and the item won't be highlighted in this case'
            lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Send the item selected to showDetails so the right hero info is shown
            showDetails(mCurCheckPosition);
        }
    }

    // Called every time the screen orientation changes or Android kills an Activity
    // to conserve resources
    // We save the last item selected in the list here and attach it to the key curChoice
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    // When a list item is clicked we want to change the hero info
    //@Override
    //public void onListItemClick(ListView l, View v, int position, long id) {
    //    showDetails(position);
    //}

    // Shows the hero data
    void showDetails(int index) {

        // The most recently selected hero in the ListView is sent
        mCurCheckPosition = index;

        // Check if we are in horizontal mode and if yes show the ListView and
        // the hero data
        if (mDuelPane) {

            // Make the currently selected item highlighted
            lstView.setItemChecked(index, true);

            // Create an object that represents the current FrameLayout that we will
            // put the hero data in
            zDetailsFragment details = (zDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);

            // When a DetailsFragment is created by calling newInstance the index for the data
            // it is supposed to show is passed to it. If that index hasn't been assigned we must
            // assign it in the if block
            if (details == null || details.getShownIndex() != index) {

                // Make the details fragment and give it the currently selected hero index
                details = zDetailsFragment.newInstance(index);

                // Start Fragment transactions
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                // Replace any other Fragment with our new Details Fragment with the right data
                ft.replace(R.id.details, details);

                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Launch a new Activity to show our DetailsFragment
            Intent intent = new Intent();

            // Define the class Activity to call
            intent.setClass(getActivity(), zDetailsActivity.class);

            // Pass along the currently selected index assigned to the keyword index
            intent.putExtra("index", index);

            // Call for the Activity to open
            startActivity(intent);
        }
    }
}