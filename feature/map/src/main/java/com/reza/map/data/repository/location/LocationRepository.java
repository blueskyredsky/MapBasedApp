package com.reza.map.data.repository.location;

import android.location.Location;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

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
}