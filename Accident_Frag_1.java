package com.jrschugel.loadmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class Accident_Frag_1 extends Fragment {

    Long AccidentID;
    DatabaseHelper myDb;
    Context context;
    private INavActivity2 mINavActivity;

    public static Accident_Frag_1 newInstance() {
        return new Accident_Frag_1();
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub__accident01, container, false);
        context = getActivity();

        Bundle b = getArguments();
        AccidentID = b.getLong(Config.TAG_ACCID);
        myDb = new DatabaseHelper(context);

        FloatingActionButton fabNext = rootView.findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                Accident_Frag_2 fragment = new Accident_Frag_2();
                mINavActivity.inflateFragment(fragment, "Accident Report pg 2", bundle);
            }
        });

        FloatingActionButton fabExit = rootView.findViewById(R.id.fabExit);
        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Next Page", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Bundle bundle = new Bundle();
                bundle.putLong(Config.TAG_ACCID, AccidentID);
                AccidentFragment fragment = new AccidentFragment();
                mINavActivity.inflateFragment(fragment, "Accident Report", bundle);
            }
        });
        return rootView;
    }
}


