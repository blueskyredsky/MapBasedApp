package com.reza.details.data.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class BookmarkDetailsView {

    private final String address;

    private final String name;

    private final String notes;

    private final String phoneNumber;

    @Nullable
    private Bitmap image;

    public BookmarkDetailsView(
                               String address,
                               String notes,
                               String name,
                               String phoneNumber,
                               @Nullable Bitmap image) {
        this.address = address;
        this.notes = notes;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    @Nullable
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
