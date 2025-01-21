package com.reza.map.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.reza.map.data.model.PlaceInfo;
import com.reza.map.databinding.ContentBookmarkInfoBinding;

import java.util.Objects;

public class BookmarkInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "BookmarkInfoWindowAdapterTag";
    private final ContentBookmarkInfoBinding binding;

    public BookmarkInfoWindowAdapter(Activity context) {
        binding = ContentBookmarkInfoBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // This function is required, but can return null if not replacing the entire info window
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        try {
            PlaceInfo placeInfo = (PlaceInfo) marker.getTag();
            if (placeInfo != null && placeInfo.getPhoto() != null) {
                binding.photo.setImageBitmap(placeInfo.getPhoto());
            }

        } catch (ClassCastException exception) {
            // todo set a default image
            Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "class cast exception");
        }
        binding.title.setText(title != null ? title : "");
        binding.phone.setText(snippet != null ? snippet : "");

        return binding.getRoot();
    }
}
