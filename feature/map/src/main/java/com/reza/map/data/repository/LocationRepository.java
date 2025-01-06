package com.reza.map.data.repository;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import android.location.Location;
import com.google.android.libraries.places.api.model.Place;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.util.List;

/**
 * A repository responsible for managing location data.
 */
public interface LocationRepository {

    /**
     * Gets the last known location of the device.
     */
    Single<Location> getLastLocation();

    /**
     * Starts receiving location updates.
     */
    Completable startLocationUpdates();

    /**
     * Stops receiving location updates.
     */
    Completable stopLocationUpdates();

    /**
     * Gets a stream of location updates.
     */
    Flowable<Location> getLocationUpdates();

    /**
     * Retrieves details for a place using its place ID.
     */
    Single<Place> getPlace(String placeId, List<Place.Field> placeFields);

    /**
     * Retrieves a photo for a given photo metadata.
     */
    Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight);
}