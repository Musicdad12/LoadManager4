package com.jrschugel.loadmanager;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class Loads_Fragment_Info extends Fragment {

    DatabaseHelper myDb;
    DatabaseHelperStops myDb2;
    Integer LoadNumber;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub__fragment01, container, false);
        context = getActivity();

        Bundle b = getArguments();
        if (b != null) {
            LoadNumber = Integer.parseInt(b.getString("LoadNumber"));
        }

        myDb = new DatabaseHelper(context);
        myDb2 = new DatabaseHelperStops(context);

        Cursor cursor =  myDb.getLoadData(LoadNumber);

        String AddlStops = myDb2.GetStopCount(LoadNumber).toString();
        String CustomerPO = null;
        String Weight = null;
        String Pieces = null;
        String BOLNumber = null;
        String TrlrNumber = null;
        String DrvLoad = null;
        String DrvUnload = null;
        String Loaded = null;
        String Empty = null;
        String TempLow = null;
        String TempHi = null;
        String Preloaded = null;
        String DropHook = null;
        String Comments = null;

        if (cursor.moveToFirst()) {
            CustomerPO = cursor.getString(cursor.getColumnIndex(Config.TAG_CUSTPO));
            Weight = cursor.getString(cursor.getColumnIndex(Config.TAG_WEIGHT));
            Pieces = cursor.getString(cursor.getColumnIndex(Config.TAG_PIECES));
            BOLNumber = cursor.getString(cursor.getColumnIndex(Config.TAG_BOLNUMBER));
            TrlrNumber = cursor.getString(cursor.getColumnIndex(Config.TAG_TRLRNUMBER));
            DrvLoad = cursor.getString(cursor.getColumnIndex(Config.TAG_DRVLOAD));
            DrvUnload = cursor.getString(cursor.getColumnIndex(Config.TAG_DRVUNLOAD));
            Loaded = cursor.getString(cursor.getColumnIndex(Config.TAG_LOADED));
            Empty = cursor.getString(cursor.getColumnIndex(Config.TAG_EMPTY));
            TempLow = cursor.getString(cursor.getColumnIndex(Config.TAG_TEMPLOW));
            TempHi = cursor.getString(cursor.getColumnIndex(Config.TAG_TEMPHIGH));
            Preloaded = cursor.getString(cursor.getColumnIndex(Config.TAG_PRELOADED));
            DropHook = cursor.getString(cursor.getColumnIndex(Config.TAG_DROPHOOK));
            Comments = cursor.getString(cursor.getColumnIndex(Config.TAG_COMMENTS));
        }

        TextView tvLoadNumber = rootView.findViewById(R.id.textViewLoadNumber);
        TextView tvAddlStops = rootView.findViewById(R.id.textViewAddlStops);
        TextView tvCustPO = rootView.findViewById(R.id.textViewCustomerPO);
        TextView tvWeight = rootView.findViewById(R.id.textViewWeight);
        TextView tvPieces = rootView.findViewById(R.id.textViewPieces);
        TextView tvBOLNo = rootView.findViewById(R.id.textViewBLNumber);
        TextView tvTrlrNo = rootView.findViewById(R.id.textViewTrlrNumber);
        TextView tvDrvLoad = rootView.findViewById(R.id.textViewDrvLoad);
        TextView tvDrvUnload = rootView.findViewById(R.id.textViewDrvUnload);
        TextView tvLoaded = rootView.findViewById(R.id.textViewLoaded);
        TextView tvEmpty = rootView.findViewById(R.id.textViewEmpty);
        TextView tvTempLow = rootView.findViewById(R.id.textViewTempLow);
        TextView tvTempHi = rootView.findViewById(R.id.textViewTempHigh);
        TextView tvPreload = rootView.findViewById(R.id.textViewPreload);
        TextView tvDropHook = rootView.findViewById(R.id.textViewDropHook);
        TextView tvComments = rootView.findViewById(R.id.textViewComments);

        tvLoadNumber.setText(LoadNumber.toString());
        tvAddlStops.setText(AddlStops);
        tvCustPO.setText(CustomerPO);
        tvWeight.setText(Weight);
        tvPieces.setText(Pieces);
        tvBOLNo.setText(BOLNumber);
        tvTrlrNo.setText(TrlrNumber);
        tvDrvLoad.setText(DrvLoad);
        tvDrvUnload.setText(DrvUnload);
        tvLoaded.setText(Loaded);
        tvEmpty.setText(Empty);
        tvTempLow.setText(TempLow);
        tvTempHi.setText(TempHi);
        tvPreload.setText(Preloaded);
        tvDropHook.setText(DropHook);
        tvComments.setText(Comments);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public int getShownIndex() {

        // Returns the index assigned
        return getArguments().getInt("index", 0);
    }

    public String getShownLoad() {

        // Returns the index assigned
        return LoadsListAdapter.mCurrentLoadNumber;
    }

}


