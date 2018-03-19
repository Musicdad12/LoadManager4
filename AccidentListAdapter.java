package com.jrschugel.loadmanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

class AccidentListAdapter extends CursorAdapter {

        public AccidentListAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.listview_accident_layout, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvDateTime = view.findViewById(R.id.tvAccidentDateTime);
            TextView tvLocation = view.findViewById(R.id.tvAccidentLocation);
            TextView tvLoadNumber = view.findViewById(R.id.tvAccidentLoadNumber);
            // Extract properties from cursor
            String TimeDate = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_ACCACCIDENTDATE));
            String Location = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_ACCACCIDENTLOCATION));
            String LoadNumber = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_ACCLOADNUMBER));
            // Populate fields with extracted properties
            if (TimeDate == null) {
                TimeDate = "Report Created but not completed.";
            }
            tvDateTime.setText(TimeDate);
            tvLocation.setText(Location);
            tvLoadNumber.setText(LoadNumber);

        }
    }