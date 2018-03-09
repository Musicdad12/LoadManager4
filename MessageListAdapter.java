package com.jrschugel.loadmanager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MessageListAdapter extends CursorAdapter {

        public MessageListAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.listview_message_layout, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvDateTime = view.findViewById(R.id.tvMessageDateTime);
            TextView tvText = view.findViewById(R.id.tvTextMessage);
            TextView tvFrom = view.findViewById(R.id.tvMessageSender);
            // Extract properties from cursor
            String TimeDate = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_MESSTIMEDATE));
            String Text = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_MESSTEXT));
            String Sender = cursor.getString(cursor.getColumnIndexOrThrow(Config.TAG_MESSSENDER));
            // Populate fields with extracted properties
            tvDateTime.setText(TimeDate);
            tvText.setText(Text);
            tvFrom.setText(Sender);

        }
    }