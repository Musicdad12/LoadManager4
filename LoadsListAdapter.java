package com.jrschugel.loadmanager;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by seanm on 2/24/2018.
 * Copyright 2017. All rights reserved.
 */
public class LoadsListAdapter extends RecyclerView.Adapter<LoadsListAdapter.CustomViewHolder> {

    Context context;
    ArrayList<LoadsList> loadList;
    boolean mDuelPane;
    private OnItemClickListener mListener;
    public static String mCurrentLoadNumber;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LoadsListAdapter (Context context, ArrayList<LoadsList> loadList) {
        this.context = context;
        this.loadList = loadList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_loads_layout, parent, false);
        return new CustomViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        LoadsList loadsList = loadList.get(position);

        holder.LoadNo.setText(loadsList.LoadNo);
        holder.Status.setText(loadsList.Status);
        holder.ShipName.setText(loadsList.ShipName);
        holder.ShipCity.setText(loadsList.ShipCity);
        holder.ShipState.setText(loadsList.ShipState);
        holder.ConsName.setText(loadsList.ConsName);
        holder.ConsCity.setText(loadsList.ConsCity);
        holder.ConsState.setText(loadsList.ConsState);
        holder.PickupDate.setText(loadsList.PickupDate);
        holder.PickupTime.setText(loadsList.PickupTime);

    }

    @Override
    public int getItemCount() {
        return loadList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView LoadNo;
        TextView Status;
        TextView ShipName;
        TextView ShipCity;
        TextView ShipState;
        TextView ConsName;
        TextView ConsCity;
        TextView ConsState;
        TextView PickupDate;
        TextView PickupTime;

        public CustomViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            LoadNo = view.findViewById(R.id.tvLoadNumber);
            Status = view.findViewById(R.id.tvStatus);
            ShipName = view.findViewById(R.id.tvShipperName);
            ShipCity = view.findViewById(R.id.tvShipperCity);
            ShipState = view.findViewById(R.id.tvShipperState);
            ConsName = view.findViewById(R.id.tvConsName);
            ConsCity = view.findViewById(R.id.tvConsCity);
            ConsState = view.findViewById(R.id.tvConsState);
            PickupDate = view.findViewById(R.id.tvPUDate);
            PickupTime = view.findViewById(R.id.tvPUTime);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mCurrentLoadNumber = LoadNo.getText().toString();
                            listener.OnItemClick(position);
                            DatabaseHelper myDb = new DatabaseHelper(context);
                            myDb.updateViewedLoad(Integer.parseInt(mCurrentLoadNumber));
                            ShortcutBadger.applyCount(context, myDb.NewLoadCount());

                        }
                    }
                }
            });
        }
    }
}
