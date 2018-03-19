package com.jrschugel.loadmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

public class AccidentFragment extends Fragment {

    private FragmentAdapter adapter;
    private ViewPager pager;
    Context context;
    DatabaseHelper myDb;
    SwipeMenuListView lstAccidents;
    private static final String TAG = "AccidentFragment";
    long AccidentID;
    private INavActivity2 mINavActivity;
    AccidentListAdapter accListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mINavActivity = (INavActivity2) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_accident, container, false);
        context = getActivity();

        myDb = new DatabaseHelper(context);
        lstAccidents = rootView.findViewById(R.id.lstAccidents);

        Cursor accidentList = myDb.getAllAccidents();

        accListAdapter = new AccidentListAdapter(context, accidentList);

        lstAccidents.setAdapter(accListAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        lstAccidents.setMenuCreator(creator);
        lstAccidents.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //delete
                        Log.d(TAG, "onMenuItemClick: " + index);
                        View swipeView = inflater.inflate(R.layout.listview_message_layout, container, false);
                        TextView textview = swipeView.findViewById(R.id.tvMessageDateTime);
                        Long accID = accListAdapter.getItemId(position);
                        myDb.DeleteAccident(accID);
                        accListAdapter.notifyDataSetChanged();
                        lstAccidents.invalidateViews();
                        Cursor accidentList = myDb.getAllAccidents();
                        accListAdapter = new AccidentListAdapter(context, accidentList);
                        lstAccidents.setAdapter(accListAdapter);
                        Log.d(TAG, "onMenuItemClick: " + accID);

                        break;
                    case 1:
                        //Edit
                        Log.d(TAG, "onMenuItemClick: " + index);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lstAccidents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccidentID = id;
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_1 fragment = new Accident_Frag_1();
                mINavActivity.inflateFragment(fragment,"Accident Report pg 1", bundle);
            }
        });
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Start new accident report", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AccidentID = myDb.saveAccident();
                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_1 fragment = new Accident_Frag_1();
                mINavActivity.inflateFragment(fragment,"Accident Report pg 1", bundle);
            }
        });

            return rootView;
        }

}
