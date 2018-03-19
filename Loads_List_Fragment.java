package com.jrschugel.loadmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Shows the title fragment which is a ListView
// When a ListView item is selected we will put the DetailsFragment in the
// Framelayout if we are in horizontal mode, or we will create a DetailsActivity // and switch to it if in portrait mode
public class Loads_List_Fragment extends Fragment {

    // True or False depending on if we are in horizontal or duel pane mode
    boolean mDuelPane;
    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    RecyclerView lstView;
    LoadsListAdapter loadsListAdapter;
    private static final String TAG = "Loads_List_Fragment";
    Context context;
    private INavActivity mINavActivity;
    View detailsFrame;
    View rootView;

    // Currently selected item in the ListView
    int mCurCheckPosition = 0;
    String mCurSelectedLoad;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mINavActivity = (INavActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView != null){
            if(rootView.getParent()!=null)
                ((ViewGroup)rootView.getParent()).removeView(rootView);
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_loads_list, container, false);

        context = getActivity();

        lstView = rootView.findViewById(R.id.custom_list);
        lstView.setLayoutManager(new LinearLayoutManager(context));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // An ArrayAdapter connects the array to our ListView
        // getActivity() returns a Context so we have the resources needed
        // We pass a default list item text view to put the data in and the
        // array
        myDb = new DatabaseHelper(getActivity());
        myDb2 = new DatabaseHelperStops(getActivity());

        JSONArray jsonArray = myDb2.getAllLoadsList(getActivity());
        ArrayList<LoadsList> listdata = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    Date newDate = dateFormat.parse(jsonObject.getString("ShipDate"));
                    Date newTime = timeFormat.parse(jsonObject.getString("ShipTime"));
                    dateFormat = new SimpleDateFormat("M/dd/yy", Locale.US);
                    timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
                    listdata.add(new LoadsList(jsonObject.getString("StopLoadNumber"),
                            myDb.getStatus(jsonObject.getString("StopLoadNumber")),
                            jsonObject.getString("ShipperName"),
                            jsonObject.getString("ShipperCity"),
                            jsonObject.getString("ShipperState"),
                            jsonObject.getString("ConsName"),
                            jsonObject.getString("ConsCity"),
                            jsonObject.getString("ConsState"),
                            dateFormat.format(newDate),
                            timeFormat.format(newTime)));

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        loadsListAdapter = new LoadsListAdapter(getActivity(), listdata);
        lstView.setAdapter(loadsListAdapter);
        loadsListAdapter.setOnItemClickListener(new LoadsListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                myDb.updateViewedLoad(Integer.parseInt(LoadsListAdapter.mCurrentLoadNumber));
                Integer NewLoads = myDb.NotificationLoadCount();
                me.leolin.shortcutbadger.ShortcutBadger.applyCount(context, NewLoads);
                showDetails(position, LoadsListAdapter.mCurrentLoadNumber);

            }
        });

        // Check if the FrameLayout with the id details exists
        detailsFrame = getActivity().findViewById(R.id.details);

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
            //lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            // Send the item selected to showDetails so the right hero info is shown
            showDetails(mCurCheckPosition, LoadsListAdapter.mCurrentLoadNumber);
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

    void showDetails(int index, String loadnumber) {

        mCurCheckPosition = index;

        if (mDuelPane) {

            // Create an object that represents the current FrameLayout
            //Loads_Details_Fragment details = (Loads_Details_Fragment)
            //        getFragmentManager().findFragmentById(R.id.details);

            // When a DetailsFragment is created by calling newInstance the index for the data
            // it is supposed to show is passed to it. If that index hasn't been assigned we must
            // assign it in the if block
            if (loadnumber != null) {

                // Make the details fragment and give it the currently selected index
                Loads_Details_Fragment details = Loads_Details_Fragment.newInstance(loadnumber);

                // Start Fragment transactions
                getFragmentManager().beginTransaction()
                // Replace any other Fragment with our new Details Fragment with the right data
                .replace(R.id.details, details)
                .addToBackStack(null)
                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
            }

        } else {
            mINavActivity.inflateFragment("Loads_Details_Fragment", LoadsListAdapter.mCurrentLoadNumber);
        }
    }


}