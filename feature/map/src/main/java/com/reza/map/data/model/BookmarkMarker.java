package com.reza.map.data.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class BookmarkMarker {
    @Nullable
    private final Long id;

    private final LatLng location;

    private final String title;

    private final String phoneNumber;

    public BookmarkMarker(@Nullable Long id, LatLng location, String title, String phoneNumber) {
        this.id = id;
        this.location = location;
        this.title = title;
        this.phoneNumber = phoneNumber;
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
}
