package com.reza.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark")
public class BookmarkEntity {

    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Long id;

    @ColumnInfo(name = "place_id")
    @Nullable
    private final String placeId;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "address")
    private final String address;

    @ColumnInfo(name = "latitude")
    private final double latitude;

    @ColumnInfo(name = "longitude")
    private final double longitude;

    @ColumnInfo(name = "phone")
    private final String phone;

    @ColumnInfo(name = "notes")
    private final String notes;

    @ColumnInfo(name = "category")
    private final String category;

    public BookmarkEntity(
            @Nullable Long id,
            @Nullable String placeId,
            String name,
            String address,
            double latitude,
            double longitude,
            String phone,
            String notes,
            String category) {
        this.id = id;
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.notes = notes;
        this.category = category;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @Nullable
    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }

    public String getNotes() {
        return notes;
    }

    public String getCategory() {
        return category;
    }

    @NonNull
    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", phone='" + phone + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}