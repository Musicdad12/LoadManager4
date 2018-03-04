package com.jrschugel.loadmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User on 10/2/2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){

        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }

        Bitmap Image = getTruckstopIcon(title);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        ivIcon.setImageBitmap(Image);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    private Bitmap getTruckstopIcon (String Name) {
        if (Name.contains("Schugel")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo_new_jr);
        }
        if (Name.contains("Love")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.loves);
        }
        if (Name.contains("Pilot")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pilot);
        }
        if (Name.contains("Flying")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.flying_j);
        }
        if (Name.contains("Petro")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.petro);
        }
        if (Name.contains("T. A.")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.t_a);
        }
        if (Name.contains("Kwik")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.kwiktrip);
        }
        if (Name.contains("Quik")) {
            return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.quiktrip);
        }
        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.truck);
    }
}