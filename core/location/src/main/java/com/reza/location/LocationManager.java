package com.reza.location;

import android.location.Location;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface LocationManager {

    /**
     * Retrieves the last known location of the device.
     */
    Single<Location> getLastLocation();

    /**
     * Provides a stream of location updates.
     */
    Flowable<Location> getLocationUpdates();

    /**
     * Starts location updates.
     */
    void startLocationUpdates();

    /**
     * Stops location updates from the Fused Location Provider.
     */
    void stopLocationUpdates();
}
