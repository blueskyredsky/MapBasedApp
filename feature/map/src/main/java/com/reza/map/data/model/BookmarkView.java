package com.reza.map.data.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class BookmarkView {
    @Nullable
    private final Long id;

    private final LatLng location;

    private final String title;

    private final String phoneNumber;

    private Bitmap image;

    private final Integer categoryResourceId;

    public BookmarkView(@Nullable Long id,
                        LatLng location,
                        String title,
                        String phoneNumber,
                        @Nullable Bitmap image,
                        Integer categoryResourceId) {
        this.id = id;
        this.location = location;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.categoryResourceId = categoryResourceId;
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

    public Integer getCategoryResourceId() {
        return categoryResourceId;
    }

    @Nullable
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
