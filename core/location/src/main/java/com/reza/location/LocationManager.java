package com.reza.location;

import io.reactivex.Flowable;
import io.reactivex.Single;

import android.location.Location;

public interface LocationManager {
    /**
     * Retrieves the last known location of the device.
     *
     * @return A {@link Single} that emits the last known location.
     */
    Single<Location> getLastLocation();

    /**
     * Retrieves a {@link Flowable} of location updates.
     *
     * @return A {@link Flowable} that emits location updates.
     */
    Flowable<Location> getLocationUpdates();
}
