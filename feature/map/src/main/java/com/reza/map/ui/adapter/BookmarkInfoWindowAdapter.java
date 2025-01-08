package com.reza.map.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.reza.map.databinding.ContentBookmarkInfoBinding;

public class BookmarkInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Activity context;
    private final ContentBookmarkInfoBinding binding;

    public BookmarkInfoWindowAdapter(Activity context) {
        this.context = context;
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

        // Handle potential ClassCastException for image
        if (marker.getTag() instanceof Bitmap) {
            binding.photo.setImageBitmap((Bitmap) marker.getTag());
        } else if (marker.getTag() instanceof BitmapDescriptor) {
            // Handle case where tag is a BitmapDescriptor (optional)
            // You could log a warning or take no action here.
        }

        binding.title.setText(title != null ? title : "");
        binding.phone.setText(snippet != null ? snippet : "");

        return binding.getRoot();
    }
}
