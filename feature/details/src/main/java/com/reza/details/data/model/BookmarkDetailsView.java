package com.reza.details.data.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class BookmarkDetailsView {
    @Nullable
    private final Long id;

    private final String address;

    private final String title;

    private final String notes;

    private final String phoneNumber;

    private Bitmap image;

    public BookmarkDetailsView(@Nullable Long id,
                               String address,
                               String notes,
                               String title,
                               String phoneNumber,
                               @Nullable Bitmap image) {
        this.id = id;
        this.address = address;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.notes = notes;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
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
