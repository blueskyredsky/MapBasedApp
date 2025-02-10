package com.reza.map.data.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class BookmarkMarker {
    @Nullable
    private final Long id;

    private final LatLng location;

    private final String title;

    private final String phoneNumber;

    private Bitmap image;

    public BookmarkMarker(@Nullable Long id,
                          LatLng location,
                          String title,
                          String phoneNumber,
                          @Nullable Bitmap image) {
        this.id = id;
        this.location = location;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
