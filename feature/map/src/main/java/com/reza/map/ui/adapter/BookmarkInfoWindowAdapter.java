package com.reza.map.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.reza.map.data.model.BookmarkMarker;
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

        Object tag = marker.getTag();

        if (tag instanceof PlaceInfo) {
            PlaceInfo placeInfo = (PlaceInfo) tag;
            if (placeInfo.getPhoto() != null) {
                binding.photo.setImageBitmap(placeInfo.getPhoto());
            }
        } else if (tag instanceof BookmarkMarker) {
            BookmarkMarker bookmark = (BookmarkMarker) tag;
            binding.photo.setImageBitmap(bookmark.getImage());
        } else {
            Log.e(TAG, "getInfoContents: unable to handle type");
        }

        binding.title.setText(title != null ? title : "");
        binding.phone.setText(snippet != null ? snippet : "");

        return binding.getRoot();
    }
}
