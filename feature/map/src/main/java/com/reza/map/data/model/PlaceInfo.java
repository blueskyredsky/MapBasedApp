package com.reza.map.data.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.Place;

public class PlaceInfo {

    @Nullable
    private final Place place;

    @Nullable
    private final Bitmap photo;

    public PlaceInfo(@Nullable Place place, @Nullable Bitmap photo) {
        this.place = place;
        this.photo = photo;
    }

    @Nullable
    public Place getPlace() {
        return place;
    }

    @Nullable
    public Bitmap getPhoto() {
        return photo;
    }
}
