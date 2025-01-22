package com.reza.map.data.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class BookmarkMarker {
    @Nullable
    private Long id;

    private LatLng location;

    public BookmarkMarker(@Nullable Long id, LatLng location) {
        this.id = id;
        this.location = location;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
